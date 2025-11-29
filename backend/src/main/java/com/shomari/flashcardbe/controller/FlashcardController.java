package com.shomari.flashcardbe.controller;


import com.shomari.flashcardbe.entity.Flashcard;
import com.shomari.flashcardbe.entity.User;
import com.shomari.flashcardbe.service.FlashcardService;
import com.shomari.flashcardbe.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


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
    @PostMapping
    public ResponseEntity<Flashcard> createFlashcard(Authentication authentication,
                                                     @Valid @RequestBody Flashcard flashcard) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        flashcard.setUser(user);
        return ResponseEntity.ok(flashcardService.createFlashcard(flashcard));
    }

    // Get all flashcards for the authenticated user
    @GetMapping
    public ResponseEntity<List<Flashcard>> getFlashcards(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(flashcardService.getFlashcardsByUser(user));
    }

    // Get a specific flashcard
    @GetMapping("/{flashcardId}")
    public ResponseEntity<Flashcard> getFlashcard(Authentication authentication,
                                                  @PathVariable Long flashcardId) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        Optional<Flashcard> flashcard = flashcardService.getFlashcardByIdAndUser(flashcardId, user);
        return flashcard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a flashcard
    @PutMapping("/{flashcardId}")
    public ResponseEntity<Flashcard> updateFlashcard(Authentication authentication,
                                                     @PathVariable Long flashcardId,
                                                     @Valid @RequestBody Flashcard updatedFlashcard) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        Optional<Flashcard> flashcardOpt = flashcardService.getFlashcardByIdAndUser(flashcardId, user);

        if (flashcardOpt.isEmpty()) return ResponseEntity.notFound().build();

        Flashcard flashcard = flashcardOpt.get();
        flashcard.setTopic(updatedFlashcard.getTopic());
        flashcard.setQuestion(updatedFlashcard.getQuestion());
        flashcard.setAnswer(updatedFlashcard.getAnswer());
        return ResponseEntity.ok(flashcardService.updateFlashcard(flashcard));
    }

    // Delete a flashcard
    @DeleteMapping("/{flashcardId}")
    public ResponseEntity<Void> deleteFlashcard(Authentication authentication,
                                                @PathVariable Long flashcardId) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        Optional<Flashcard> flashcardOpt = flashcardService.getFlashcardByIdAndUser(flashcardId, user);

        if (flashcardOpt.isEmpty()) return ResponseEntity.notFound().build();

        flashcardService.deleteFlashcard(flashcardOpt.get());
        return ResponseEntity.noContent().build();
    }

}
