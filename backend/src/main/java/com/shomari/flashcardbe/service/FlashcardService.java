package com.shomari.flashcardbe.service;

import com.shomari.flashcardbe.entity.Flashcard;
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

    public Flashcard createFlashcard(Flashcard flashcard) {
        return flashcardRepository.save(flashcard);
    }

    public List<Flashcard> getFlashcardsByUserId(String userId) {
        return flashcardRepository.findByUserId(userId);
    }

    public Optional<Flashcard> getFlashcardByIdAndUserId(Long flashcardId, String userId) {
        return flashcardRepository.findByIdAndUserId(flashcardId, userId);
    }

    public Flashcard updateFlashcard(Flashcard flashcard) {
        return flashcardRepository.save(flashcard);
    }

    public void deleteFlashcard(Flashcard flashcard) {
        flashcardRepository.delete(flashcard);
    }

    public List<Flashcard> getFlashcardsBySetId(Long setId) {
        return flashcardRepository.findByFlashcardSetId(setId);
    }
}