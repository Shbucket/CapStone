import { useState } from "react";
import {
    Container, TextField, Button, Typography, Box,
    Grid, Card, CardContent, Dialog, DialogContent, DialogActions
} from "@mui/material";
import { useAuth, useUser, UserButton } from "@clerk/clerk-react";
import {useNavigate} from "react-router-dom";


export default function Generate() {
    const { user } = useUser();
    const { getToken } = useAuth();

    const [topic, setTopic] = useState("");
    const [numFlashcards, setNumFlashcards] = useState(5);
    const [flashcards, setFlashcards] = useState([]);
    const [setName, setSetName] = useState("");
    const [dialogOpen, setDialogOpen] = useState(false);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const backendUrl = "http://localhost:8080";

    // Generate flashcards using AI
    const handleGenerate = async () => {
        if (!topic.trim()) return alert("Enter a topic.");

        setLoading(true);
        try {
            const token = await getToken();

            const res = await fetch(`${backendUrl}/api/ai/flashcards`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify({ text: topic, numFlashcards }),
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

    // Save flashcards as a named set
    const handleSaveSet = async () => {
        if (!setName.trim()) return alert("Enter a set name.");

        try {
            const token = await getToken();

            // Create the flashcard set
            const setRes = await fetch(`${backendUrl}/api/flashcards/sets?userId=${user.id}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify({ name: setName }),
            });

            if (!setRes.ok) throw new Error("Failed to create set");
            const savedSet = await setRes.json();

            // Save each flashcard under that set
            for (const fc of flashcards) {
                const flashRes = await fetch(`${backendUrl}/api/flashcards?userId=${user.id}&setId=${savedSet.id}`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${token}`,
                    },
                    body: JSON.stringify({
                        topic,
                        question: fc.front,
                        answer: fc.back,
                    }),
                });

                if (!flashRes.ok) throw new Error("Failed to save flashcard");
            }

            alert("Flashcards saved successfully!");
            setDialogOpen(false);
            setFlashcards([]);
            setSetName("");
            setTopic("");
        } catch (err) {
            console.error(err);
            alert("Error saving set.");
        }
    };

    return (
        <Container maxWidth="md" sx={{ py: 4 }}>
            <Box display="flex" justifyContent="space-between" mb={3}>
                <Typography variant="h4" fontWeight="bold">Generate Flashcards</Typography>
                <Button
                    variant="contained"
                    sx={{ mr: 2 }}
                    onClick={() => navigate("/Flashcards")}
                >
                    View your sets
                </Button>
                <UserButton />
            </Box>

            <TextField
                fullWidth label="Topic"
                value={topic} onChange={(e) => setTopic(e.target.value)}
                sx={{ mb: 2 }}
            />
            <TextField
                fullWidth label="Number of flashcards"
                type="number" value={numFlashcards}
                onChange={(e) => setNumFlashcards(Number(e.target.value))}
                sx={{ mb: 2 }}
            />

            <Button
                variant="contained" fullWidth
                onClick={handleGenerate} disabled={loading}
            >
                {loading ? "Generating..." : "Generate"}
            </Button>

            {flashcards.length > 0 && (
                <>
                    <Typography mt={4} mb={2} variant="h5">Preview</Typography>
                    <Grid container spacing={2}>
                        {flashcards.map((fc, i) => (
                            <Grid item xs={12} sm={6} key={i}>
                                <Card>
                                    <CardContent>
                                        <Typography fontWeight="bold">Front</Typography>
                                        {fc.front}
                                        <Typography mt={1} fontWeight="bold">Back</Typography>
                                        {fc.back}
                                    </CardContent>
                                </Card>
                            </Grid>
                        ))}
                    </Grid>

                    <Button sx={{ mt: 3 }} variant="contained" onClick={() => setDialogOpen(true)}>
                        Save Set
                    </Button>
                </>
            )}

            <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)}>
                <DialogContent>
                    <TextField
                        fullWidth label="Set Name"
                        value={setName} onChange={(e) => setSetName(e.target.value)}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setDialogOpen(false)}>Cancel</Button>
                    <Button onClick={handleSaveSet}>Save</Button>
                </DialogActions>
            </Dialog>
        </Container>
    );
}
