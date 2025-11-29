package com.shomari.flashcardbe.controller;


import com.shomari.flashcardbe.entity.Flashcard;
import com.shomari.flashcardbe.entity.User;
import com.shomari.flashcardbe.service.FlashcardService;
import com.shomari.flashcardbe.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController {
    private final FlashcardService flashcardService;
    private final UserService userService;

    public FlashcardController(FlashcardService flashcardService, UserService userService) {
        this.flashcardService = flashcardService;
        this.userService = userService;
    }

    // Create a new flashcard
    @PostMapping("/{userId}")
    public ResponseEntity<Flashcard> createFlashcard(@PathVariable Long userId,
                                                     @Valid @RequestBody Flashcard flashcard) {
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        flashcard.setUser(user.get());
        Flashcard savedFlashcard = flashcardService.createFlashcard(flashcard);
        return ResponseEntity.ok(savedFlashcard);
    }

    // Get all flashcards for a user
    @GetMapping("/{userId}")
    public ResponseEntity<List<Flashcard>> getFlashcardsByUser(@PathVariable Long userId) {
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Flashcard> flashcards = flashcardService.getFlashcardsByUser(user.get());
        return ResponseEntity.ok(flashcards);
    }

    // Get a specific flashcard by ID for a user
    @GetMapping("/{userId}/{flashcardId}")
    public ResponseEntity<Flashcard> getFlashcardById(@PathVariable Long userId,
                                                      @PathVariable Long flashcardId) {
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Optional<Flashcard> flashcard = flashcardService.getFlashcardByIdAndUser(flashcardId, user.get());
        return flashcard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a flashcard
    @PutMapping("/{userId}/{flashcardId}")
    public ResponseEntity<Flashcard> updateFlashcard(@PathVariable Long userId,
                                                     @PathVariable Long flashcardId,
                                                     @Valid @RequestBody Flashcard updatedFlashcard) {
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Optional<Flashcard> flashcardOpt = flashcardService.getFlashcardByIdAndUser(flashcardId, user.get());
        if (flashcardOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Flashcard flashcard = flashcardOpt.get();
        flashcard.setTopic(updatedFlashcard.getTopic());
        flashcard.setQuestion(updatedFlashcard.getQuestion());
        flashcard.setAnswer(updatedFlashcard.getAnswer());
        Flashcard savedFlashcard = flashcardService.updateFlashcard(flashcard);

        return ResponseEntity.ok(savedFlashcard);
    }

    // Delete a flashcard
    @DeleteMapping("/{userId}/{flashcardId}")
    public ResponseEntity<Void> deleteFlashcard(@PathVariable Long userId,
                                                @PathVariable Long flashcardId) {
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Optional<Flashcard> flashcardOpt = flashcardService.getFlashcardByIdAndUser(flashcardId, user.get());
        if (flashcardOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        flashcardService.deleteFlashcard(flashcardOpt.get());
        return ResponseEntity.noContent().build();
    }
}
