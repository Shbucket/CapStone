import { useState, useEffect } from "react";
import {
    Container, Typography, Grid, Card, CardContent, Accordion, AccordionSummary, AccordionDetails,
    CircularProgress, Button, Dialog, DialogContent, DialogActions, TextField
} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import { useUser, useAuth, UserButton } from "@clerk/clerk-react";
import { useNavigate } from "react-router-dom";

export default function FlashcardsPage() {
    const { isLoaded, user } = useUser();
    const { getToken } = useAuth();
    const navigate = useNavigate();
    const backendUrl = "http://localhost:8080";

    const [sets, setSets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchQuery, setSearchQuery] = useState("");
    const [summaries, setSummaries] = useState({});
    const [editFlashcard, setEditFlashcard] = useState(null);
    const [editDialogOpen, setEditDialogOpen] = useState(false);

    // Helper to get a valid token
    const getAuthToken = async () => {
        if (!isLoaded || !user) return null;
        return await getToken();
    };

    // Fetch all flashcard sets
    const fetchSets = async () => {
        setLoading(true);
        try {
            const token = await getAuthToken();
            if (!token) return;

            const res = await fetch(`${backendUrl}/api/flashcards/sets`, {
                headers: { Authorization: `Bearer ${token}` }
            });

            if (!res.ok) throw new Error("Failed to fetch sets");

            const data = await res.json();
            setSets(data);
        } catch (err) {
            console.error(err);
            alert("Error fetching flashcards.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (isLoaded && user) fetchSets();
    }, [isLoaded, user]);

    const handleDeleteSet = async (setId) => {
        if (!window.confirm("Delete this set?")) return;
        try {
            const token = await getAuthToken();
            if (!token) return;

            const res = await fetch(`${backendUrl}/api/flashcards/sets/${setId}`, {
                method: "DELETE",
                headers: { Authorization: `Bearer ${token}` },
            });

            if (!res.ok) throw new Error("Failed to delete set");
            fetchSets();
        } catch (err) {
            console.error(err);
            alert("Error deleting set.");
        }
    };

    const handleDeleteFlashcard = async (flashcardId) => {
        if (!window.confirm("Delete this flashcard?")) return;
        try {
            const token = await getAuthToken();
            if (!token) return;

            const res = await fetch(`${backendUrl}/api/flashcards/${flashcardId}`, {
                method: "DELETE",
                headers: { Authorization: `Bearer ${token}` },
            });

            if (!res.ok) throw new Error("Failed to delete flashcard");
            fetchSets();
        } catch (err) {
            console.error(err);
            alert("Error deleting flashcard.");
        }
    };

    const handleEditFlashcard = (fc) => {
        setEditFlashcard(fc);
        setEditDialogOpen(true);
    };

    const handleSaveEdit = async () => {
        if (!editFlashcard) return;
        try {
            const token = await getAuthToken();
            if (!token) return;

            const res = await fetch(`${backendUrl}/api/flashcards/${editFlashcard.flashcardId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
                body: JSON.stringify(editFlashcard),
            });

            if (!res.ok) throw new Error("Failed to update flashcard");
            setEditDialogOpen(false);
            fetchSets();
        } catch (err) {
            console.error(err);
            alert("Error updating flashcard.");
        }
    };

    const handleGenerateSummary = async (setId) => {
        try {
            const token = await getAuthToken();
            if (!token) return;

            const res = await fetch(`${backendUrl}/api/flashcards/summaries?setId=${setId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });

            if (!res.ok) throw new Error("Failed to fetch summaries");

            const data = await res.json();
            setSummaries(prev => ({ ...prev, [setId]: data }));
        } catch (err) {
            console.error(err);
            alert("Error generating summary.");
        }
    };

    const filteredSets = sets.filter(set => set.name.toLowerCase().includes(searchQuery.toLowerCase()));

    if (!isLoaded || loading) {
        return (
            <Container sx={{ py: 4, textAlign: "center" }}>
                <CircularProgress />
            </Container>
        );
    }

    return (
        <Container maxWidth="md" sx={{ py: 4 }}>
            <div style={{ display: "flex", justifyContent: "space-between", marginBottom: 24 }}>
                <Typography variant="h4" fontWeight="bold">My Flashcards</Typography>
                <Button variant="contained" sx={{ mr: 2 }} onClick={() => navigate("/generate")}>Generate New Set</Button>
                <UserButton />
            </div>

            <TextField
                fullWidth
                label="Search sets by name"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                sx={{ mb: 3 }}
            />

            {filteredSets.length === 0 ? (
                <Typography>No flashcards found.</Typography>
            ) : (
                filteredSets.map(set => (
                    <Accordion key={set.id} sx={{ mb: 2 }}>
                        <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                            <Typography fontWeight="bold">{set.name}</Typography>
                        </AccordionSummary>
                        <AccordionDetails>
                            <Grid container spacing={2}>
                                {set.flashcards?.length > 0 ? set.flashcards.map(fc => (
                                    <Grid item key={fc.flashcardId} xs={12} sm={6}>
                                        <Card sx={{ position: "relative" }}>
                                            <CardContent>
                                                <Typography fontWeight="bold">Front</Typography>
                                                {fc.question}
                                                <Typography mt={1} fontWeight="bold">Back</Typography>
                                                {fc.answer}
                                            </CardContent>
                                            <div style={{ display: "flex", justifyContent: "space-between", padding: 8 }}>
                                                <Button size="small" onClick={() => handleEditFlashcard(fc)}>Edit</Button>
                                                <Button size="small" color="error" onClick={() => handleDeleteFlashcard(fc.flashcardId)}>Delete</Button>
                                            </div>
                                        </Card>
                                    </Grid>
                                )) : <Typography>No flashcards in this set.</Typography>}
                            </Grid>

                            <div style={{ marginTop: 16 }}>
                                <Button variant="outlined" color="error" onClick={() => handleDeleteSet(set.id)}>Delete Set</Button>
                                <Button variant="outlined" sx={{ ml: 2 }} onClick={() => window.open(`${backendUrl}/api/reports/flashcards/${set.id}`, "_blank")}>Download CSV</Button>
                                <Button variant="outlined" sx={{ ml: 2 }} onClick={() => handleGenerateSummary(set.id)}>Generate Summary</Button>
                            </div>

                            {summaries[set.id]?.length > 0 && (
                                <div style={{ marginTop: 16 }}>
                                    <Typography fontWeight="bold">Summaries:</Typography>
                                    <ul style={{ paddingLeft: 16 }}>
                                        {summaries[set.id].map((s, idx) => <li key={idx} style={{ marginBottom: 8 }}>{s}</li>)}
                                    </ul>
                                </div>
                            )}
                        </AccordionDetails>
                    </Accordion>
                ))
            )}

            <Dialog open={editDialogOpen} onClose={() => setEditDialogOpen(false)}>
                <DialogContent>
                    <TextField
                        fullWidth
                        label="Front"
                        value={editFlashcard?.question || ""}
                        onChange={(e) => setEditFlashcard({ ...editFlashcard, question: e.target.value })}
                        sx={{ mb: 2 }}
                    />
                    <TextField
                        fullWidth
                        label="Back"
                        value={editFlashcard?.answer || ""}
                        onChange={(e) => setEditFlashcard({ ...editFlashcard, answer: e.target.value })}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setEditDialogOpen(false)}>Cancel</Button>
                    <Button onClick={handleSaveEdit}>Save</Button>
                </DialogActions>
            </Dialog>
        </Container>
    );
}
