package com.shomari.flashcardbe.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FlashcardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser // Simulate a logged-in user
    void testGetAllFlashcards() throws Exception {
        mockMvc.perform(get("/flashcards"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser // Simulate a logged-in user
    void testCreateFlashcard() throws Exception {
        String flashcardJson = """
            {
                "question": "What is Java?",
                "answer": "A programming language",
                "topic": "Programming",
                "userId": 1
            }
        """;

        mockMvc.perform(post("/flashcards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(flashcardJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.question").value("What is Java?"));
    }
}