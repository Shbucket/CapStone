import { useState, useEffect } from "react";
import {
    Container,
    Typography,
    Grid,
    Card,
    CardContent,
    Accordion,
    AccordionSummary,
    AccordionDetails,
    CircularProgress,
    Button,
    Dialog,
    DialogContent,
    DialogActions,
    TextField,
} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import { useUser, UserButton } from "@clerk/clerk-react";
import { useNavigate } from "react-router-dom";

export default function FlashcardsPage() {
    const { user } = useUser();
    const [sets, setSets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchQuery, setSearchQuery] = useState(""); // NEW: search state

    const [editFlashcard, setEditFlashcard] = useState(null);
    const [editDialogOpen, setEditDialogOpen] = useState(false);
    const navigate = useNavigate();
    const backendUrl = "http://localhost:8080";

    /** Fetch all flashcard sets for the current user */
    const fetchSets = async () => {
        if (!user) return;
        setLoading(true);
        try {
            const res = await fetch(`${backendUrl}/api/flashcards/sets?userId=${user.id}`);
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
        fetchSets();
    }, [user]);

    /** Delete a flashcard set */
    const handleDeleteSet = async (setId) => {
        if (!window.confirm("Delete this set?")) return;
        try {
            const res = await fetch(`${backendUrl}/api/flashcards/sets/${setId}?userId=${user.id}`, {
                method: "DELETE",
            });
            if (!res.ok) throw new Error("Failed to delete set");
            fetchSets();
        } catch (err) {
            console.error(err);
            alert("Error deleting set.");
        }
    };

    /** Delete an individual flashcard */
    const handleDeleteFlashcard = async (flashcardId) => {
        if (!window.confirm("Delete this flashcard?")) return;
        try {
            const res = await fetch(`${backendUrl}/api/flashcards/${flashcardId}?userId=${user.id}`, {
                method: "DELETE",
            });
            if (!res.ok) throw new Error("Failed to delete flashcard");
            fetchSets();
        } catch (err) {
            console.error(err);
            alert("Error deleting flashcard.");
        }
    };

    /** Open edit dialog for a flashcard */
    const handleEditFlashcard = (fc) => {
        setEditFlashcard(fc);
        setEditDialogOpen(true);
    };

    /** Save edited flashcard */
    const handleSaveEdit = async () => {
        if (!editFlashcard) return;
        try {
            const res = await fetch(
                `${backendUrl}/api/flashcards/${editFlashcard.flashcardId}?userId=${user.id}`,
                {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(editFlashcard),
                }
            );
            if (!res.ok) throw new Error("Failed to update flashcard");
            setEditDialogOpen(false);
            fetchSets();
        } catch (err) {
            console.error(err);
            alert("Error updating flashcard.");
        }
    };

    // Filter sets based on search query
    const filteredSets = sets.filter((set) =>
        set.name.toLowerCase().includes(searchQuery.toLowerCase())
    );

    if (loading) {
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
                <Button
                    variant="contained"
                    sx={{ mr: 2 }}
                    onClick={() => navigate("/generate")}
                >
                    Generate New Set
                </Button>
                <UserButton />
            </div>

            {/* Search bar */}
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
                filteredSets.map((set) => (
                    <Accordion key={set.id} sx={{ mb: 2 }}>
                        <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                            <Typography fontWeight="bold">{set.name}</Typography>
                        </AccordionSummary>
                        <AccordionDetails>
                            <Grid container spacing={2}>
                                {set.flashcards?.length > 0 ? (
                                    set.flashcards.map((fc) => (
                                        <Grid item xs={12} sm={6} key={fc.flashcardId}>
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
                                    ))
                                ) : (
                                    <Typography>No flashcards in this set.</Typography>
                                )}
                            </Grid>
                            <Button variant="outlined" color="error" sx={{ mt: 2 }} onClick={() => handleDeleteSet(set.id)}>
                                Delete Set
                            </Button>
                        </AccordionDetails>
                    </Accordion>
                ))
            )}

            {/* Edit Flashcard Dialog */}
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
