import { useState } from "react";
import {
    Container,
    TextField,
    Button,
    Typography,
    Box,
    Grid,
    Card,
    CardContent,
    Dialog,
    DialogContent,
    DialogActions,
} from "@mui/material";

import { useAuth, useUser, UserButton } from "@clerk/clerk-react";

export default function Generate() {
    const { user } = useUser();
    const { getToken } = useAuth();

    const [text, setText] = useState("");
    const [flashcards, setFlashcards] = useState([]);
    const [numFlashcards, setNumFlashcards] = useState(10);
    const [setName, setSetName] = useState("");
    const [dialogOpen, setDialogOpen] = useState(false);
    const [loading, setLoading] = useState(false);

    const backendUrl = "http://localhost:8080";

    const handleGenerate = async () => {
        if (!text.trim()) return alert("Enter text first.");

        setLoading(true);
        try {
            const token = await getToken();

            const res = await fetch(`${backendUrl}/api/ai/flashcards`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify({ text, numFlashcards }),
            });

            const data = await res.json();
            setFlashcards(data);
        } catch (err) {
            console.error(err);
            alert("Error generating flashcards.");
        } finally {
            setLoading(false);
        }
    };

    const handleSave = async () => {
        if (!setName.trim()) return alert("Enter a name.");

        try {
            const token = await getToken();

            const res = await fetch(`${backendUrl}/flashcards/set`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify({
                    name: setName,
                    userId: user.id,
                    flashcards,
                }),
            });

            if (!res.ok) throw new Error("Failed");

            alert("Saved!");
            setDialogOpen(false);
        } catch (err) {
            console.error(err);
            alert("Error saving.");
        }
    };

    return (
        <Container maxWidth="md" sx={{ py: 4 }}>
            <Box display="flex" justifyContent="space-between" mb={3}>
                <Typography variant="h4" fontWeight="bold">
                    Generate Flashcards
                </Typography>
                <UserButton />
            </Box>

            <TextField
                fullWidth
                label="Topic or text"
                multiline
                rows={3}
                value={text}
                onChange={(e) => setText(e.target.value)}
                sx={{ mb: 2 }}
            />

            <TextField
                fullWidth
                label="Number of flashcards"
                type="number"
                value={numFlashcards}
                onChange={(e) => setNumFlashcards(Number(e.target.value))}
                sx={{ mb: 2 }}
            />

            <Button
                variant="contained"
                fullWidth
                onClick={handleGenerate}
                disabled={loading}
            >
                {loading ? "Working..." : "Generate"}
            </Button>

            {flashcards.length > 0 && (
                <>
                    <Typography mt={4} mb={2} variant="h5">
                        Preview
                    </Typography>

                    <Grid container spacing={2}>
                        {flashcards.map((fc, i) => (
                            <Grid item xs={12} sm={6} key={i}>
                                <Card>
                                    <CardContent>
                                        <Typography fontWeight="bold">Front</Typography>
                                        {fc.front}
                                        <Typography mt={1} fontWeight="bold">
                                            Back
                                        </Typography>
                                        {fc.back}
                                    </CardContent>
                                </Card>
                            </Grid>
                        ))}
                    </Grid>

                    <Button
                        sx={{ mt: 3 }}
                        variant="contained"
                        onClick={() => setDialogOpen(true)}
                    >
                        Save Set
                    </Button>
                </>
            )}

            <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)}>
                <DialogContent>
                    <TextField
                        fullWidth
                        label="Flashcard Set Name"
                        value={setName}
                        onChange={(e) => setSetName(e.target.value)}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setDialogOpen(false)}>Cancel</Button>
                    <Button onClick={handleSave}>Save</Button>
                </DialogActions>
            </Dialog>
        </Container>
    );
}
