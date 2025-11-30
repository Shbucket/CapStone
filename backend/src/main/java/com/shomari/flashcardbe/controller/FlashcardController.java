package com.shomari.flashcardbe.controller;

import com.shomari.flashcardbe.entity.Flashcard;
import com.shomari.flashcardbe.entity.FlashcardSet;
import com.shomari.flashcardbe.service.FlashcardService;
import com.shomari.flashcardbe.service.FlashcardSetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.List;

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController {

    private final FlashcardService flashcardService;
    private final FlashcardSetService flashcardSetService;

    public FlashcardController(FlashcardService flashcardService,
                               FlashcardSetService flashcardSetService) {
        this.flashcardService = flashcardService;
        this.flashcardSetService = flashcardSetService;
    }

    // Flashcard Sets

    @GetMapping("/sets")
    public ResponseEntity<List<FlashcardSet>> getFlashcardSets(@RequestParam String userId) {
        // userId is now passed from frontend via query param
        return ResponseEntity.ok(flashcardSetService.getSetsByUser(userId));
    }

    @PostMapping("/sets")
    public ResponseEntity<FlashcardSet> createFlashcardSet(@RequestParam String userId,
                                                           @RequestBody CreateSetRequest body) {
        return ResponseEntity.ok(flashcardSetService.createSet(userId, body.getName()));
    }

    @DeleteMapping("/sets/{setId}")
    public ResponseEntity<Void> deleteSet(@RequestParam String userId, @PathVariable Long setId) {
        flashcardSetService.deleteSet(setId, userId);
        return ResponseEntity.noContent().build();
    }

    // Flashcards

    @PostMapping
    public ResponseEntity<Flashcard> createFlashcard(@RequestParam String userId,
                                                     @RequestBody Flashcard flashcard,
                                                     @RequestParam Long setId) {
        FlashcardSet set = flashcardSetService.getSetByIdAndUser(setId, userId);
        flashcard.setFlashcardSet(set);
        flashcard.setUserId(userId);
        return ResponseEntity.ok(flashcardService.createFlashcard(flashcard));
    }

    @GetMapping
    public ResponseEntity<List<Flashcard>> getFlashcards(@RequestParam String userId,
                                                         @RequestParam Long setId) {
        flashcardSetService.getSetByIdAndUser(setId, userId); // validate ownership
        return ResponseEntity.ok(flashcardService.getFlashcardsBySetId(setId));
    }

    @PutMapping("/{flashcardId}")
    public ResponseEntity<Flashcard> updateFlashcard(@RequestParam String userId,
                                                     @PathVariable Long flashcardId,
                                                     @RequestBody Flashcard updated) {
        Flashcard flashcard = flashcardService.getFlashcardByIdAndUserId(flashcardId, userId)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));
        flashcard.setTopic(updated.getTopic());
        flashcard.setQuestion(updated.getQuestion());
        flashcard.setAnswer(updated.getAnswer());
        return ResponseEntity.ok(flashcardService.updateFlashcard(flashcard));
    }

    @DeleteMapping("/{flashcardId}")
    public ResponseEntity<Void> deleteFlashcard(@RequestParam String userId,
                                                @PathVariable Long flashcardId) {
        Flashcard flashcard = flashcardService.getFlashcardByIdAndUserId(flashcardId, userId)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));
        flashcardService.deleteFlashcard(flashcard);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/summaries")
    public List<String> getFlashcardSummaries(@RequestParam String userId,
                                              @RequestParam Long setId) {
        //Validate
        flashcardSetService.getSetByIdAndUser(setId, userId);

        List<Flashcard> flashcards = flashcardService.getFlashcardsBySetId(setId);
        return flashcards.stream()
                .map(Flashcard::getSummary)
                .toList();
    }
    // DTO
    public static class CreateSetRequest {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
