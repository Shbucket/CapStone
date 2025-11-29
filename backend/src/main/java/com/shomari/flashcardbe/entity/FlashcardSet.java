package com.shomari.flashcardbe.entity;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="flashcard_sets")
public class FlashcardSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String userId; // Clerk user ID

    @OneToMany(mappedBy = "flashcardSet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Flashcard> flashcards = new ArrayList<>();

    public FlashcardSet() {}

    // getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public List<Flashcard> getFlashcards() {
        return flashcards;
    }
    public void setFlashcards(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }
}
