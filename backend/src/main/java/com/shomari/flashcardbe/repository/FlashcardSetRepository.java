package com.shomari.flashcardbe.repository;

import com.shomari.flashcardbe.entity.FlashcardSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlashcardSetRepository extends JpaRepository<FlashcardSet, Long> {
    List<FlashcardSet> findByUserId(String userId);
}
