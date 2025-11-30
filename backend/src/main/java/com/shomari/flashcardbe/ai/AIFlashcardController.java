package com.shomari.flashcardbe.ai;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AIFlashcardController {

    private final AIFlashcardService aiService;

    public AIFlashcardController(AIFlashcardService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/flashcards")
    public List<FlashcardDTO> generateFlashcards(@RequestBody FlashcardRequest request) {
        return aiService.generateFlashcards(request.getText(), request.getNumFlashcards());
    }

    public static class FlashcardRequest {
        private String text;
        private int numFlashcards;

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public int getNumFlashcards() { return numFlashcards; }
        public void setNumFlashcards(int numFlashcards) { this.numFlashcards = numFlashcards; }
    }
}
