package com.shomari.flashcardbe.repository;

import com.shomari.flashcardbe.entity.Flashcard;
import com.shomari.flashcardbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
    List<Flashcard> findByUser(User user);
    Optional<Flashcard> findByIdAndUser(Long id, User user);
}
