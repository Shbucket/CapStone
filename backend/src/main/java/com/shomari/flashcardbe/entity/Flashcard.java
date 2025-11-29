package com.shomari.flashcardbe.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name="flashcards",
        indexes = {
                @Index(columnList = "user_id")
        })
public class Flashcard {
    @Id
    @Column(name = "flashcard_id")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Long getId() {
        return flashcardId;
    }

    public void setId(Long id) {
        this.flashcardId = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
