import React from 'react';
import {
    AppBar,
    Container,
    Typography,
    Toolbar,
    Grid,
    Button,
    Box,
    Paper,
} from "@mui/material";

export default function Home() {
    // Temporary user for testing
    const user = { id: "123" };
    const navigate = (path) => window.location.href = path; // simple replacement for routing

    const handleGetStarted = async () => {
        if (user) {
            // Here you can call your backend
            console.log("Create user document for", user.id);
            navigate("/generate");
        } else {
            alert("You must be logged in");
        }
    };

    const handleFlashcards = async () => {
        if (user) {
            console.log("Create user document for", user.id);
            navigate("/flashcards");
        } else {
            alert("You must be logged in");
        }
    };

    return (
        <Container
            maxWidth="100vw"
            sx={{ backgroundColor: "#9395D3", minHeight: "100vh", py: 4 }}
        >
            <AppBar position="static" sx={{ backgroundColor: "#B3B7EE", borderRadius:"8px" }}>
                <Toolbar>
                    <Box sx={{ flexGrow: 1 }}>
                        <Typography variant="h6" sx={{ fontWeight: "bold", color: "#FFF" }}>
                            StudyWise
                        </Typography>
                    </Box>
                    <Box>
                        <Button color="inherit" onClick={() => alert("Login clicked")}>Login</Button>
                    </Box>
                </Toolbar>
            </AppBar>

            <Box sx={{ textAlign: 'center', my: 4, py: 10, fontWeight: "bold", color: "#000807" }}>
                <Typography variant="h2" gutterBottom>Welcome to StudyWise</Typography>
                <Typography variant="h5" gutterBottom>The easiest way to create flashcards from your text.</Typography>
                <Box sx={{ my: 4 }}>
                    <Button variant="contained" onClick={handleGetStarted}>Generate</Button>
                    <Button variant="contained" onClick={handleFlashcards}>Flashcards</Button>
                </Box>
            </Box>

            <Box sx={{ my: 6 }}>
                <Typography variant="h4" gutterBottom sx={{ display: "flex", justifyContent: "center" }}>Features</Typography>
                <Grid container spacing={4}>
                    <Grid item xs={12} md={4}>
                        <Paper elevation={3} sx={{ p: 3, backgroundColor: "#FBF9FF", borderRadius: "12px" }}>
                            <Typography variant="h6" gutterBottom>Generative AI Flashcards</Typography>
                            <Typography>Automatically generate flashcards based on any topic you provide.</Typography>
                        </Paper>
                    </Grid>
                    <Grid item xs={12} md={4}>
                        <Paper elevation={3} sx={{ p: 3, backgroundColor: "#FBF9FF", borderRadius: "12px" }}>
                            <Typography variant="h6" gutterBottom>Create 10 Flashcards per Topic</Typography>
                            <Typography>The AI will generate 10 high-quality flashcards per topic.</Typography>
                        </Paper>
                    </Grid>
                    <Grid item xs={12} md={4}>
                        <Paper elevation={3} sx={{ p: 3, backgroundColor: "#FBF9FF", borderRadius: "12px" }}>
                            <Typography variant="h6" gutterBottom>Powered by Llama 3.1</Typography>
                            <Typography>Ensuring accurate and detailed flashcards for in-depth learning.</Typography>
                        </Paper>
                    </Grid>
                </Grid>
            </Box>
        </Container>
    );
}