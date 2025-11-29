package com.shomari.flashcardbe.service;


import com.shomari.flashcardbe.entity.Flashcard;
import com.shomari.flashcardbe.entity.User;
import com.shomari.flashcardbe.repository.FlashcardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlashcardService {

    private final FlashcardRepository flashcardRepository;

    public FlashcardService(FlashcardRepository flashcardRepository) {
        this.flashcardRepository = flashcardRepository;
    }

    // Create a flashcard
    public Flashcard createFlashcard(Flashcard flashcard) {
        return flashcardRepository.save(flashcard);
    }

    // Get all flashcards for a user
    public List<Flashcard> getFlashcardsByUser(User user) {
        return flashcardRepository.findByUser(user);
    }

    // Find a specific flashcard for a user
    public Optional<Flashcard> getFlashcardByIdAndUser(Long id, User user) {
        return flashcardRepository.findByIdAndUser(id, user);
    }

    // Update a flashcard
    public Flashcard updateFlashcard(Flashcard flashcard) {
        return flashcardRepository.save(flashcard);
    }

    // Delete a flashcard
    public void deleteFlashcard(Flashcard flashcard) {
        flashcardRepository.delete(flashcard);
    }
}
