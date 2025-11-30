package com.shomari.flashcardbe.repository;

import com.shomari.flashcardbe.entity.Flashcard;
import com.shomari.flashcardbe.entity.FlashcardSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
    List<Flashcard> findByUserId(String userId);
    Optional<Flashcard> findByIdAndUserId(Long id, String userId);
    List<Flashcard> findByFlashcardSetId(Long setId);
    List<Flashcard> findAllByFlashcardSetId(Long flashcardSetId);
}
