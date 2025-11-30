package com.shomari.flashcardbe.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="flashcard_sets")
public class FlashcardSet extends BaseEntity {
    @NotBlank(message = "FlashcardSet name is required")
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String userId; // Clerk user ID

    @OneToMany(mappedBy = "flashcardSet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Flashcard> flashcards = new ArrayList<>();

    public FlashcardSet() {}

    @Override
    public String getSummary() {
        return "Set: " + name + " (" + flashcards.size() + " flashcards)";
    }

    // getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<Flashcard> getFlashcards() { return flashcards; }
    public void setFlashcards(List<Flashcard> flashcards) { this.flashcards = flashcards; }
}
