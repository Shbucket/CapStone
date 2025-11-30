package com.shomari.flashcardbe.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name="flashcards",
        indexes = {@Index(columnList = "userId")})
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flashcardId;

    @NotBlank(message = "Topic is required")
    private String topic;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Question is required")
    private String question;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Answer is required")
    private String answer;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String userId;

    @ManyToOne
    @JoinColumn(name = "flashcard_set_id", nullable = false)
    @JsonBackReference
    private FlashcardSet flashcardSet;

    public Flashcard() {}

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // getters and setters
    public Long getFlashcardId() { return flashcardId; }
    public void setFlashcardId(Long flashcardId) { this.flashcardId = flashcardId; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public FlashcardSet getFlashcardSet() { return flashcardSet; }
    public void setFlashcardSet(FlashcardSet flashcardSet) { this.flashcardSet = flashcardSet; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
