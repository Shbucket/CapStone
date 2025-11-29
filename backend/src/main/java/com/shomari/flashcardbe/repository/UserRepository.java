package com.shomari.flashcardbe.repository;

import com.shomari.flashcardbe.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(@NotBlank(message = "Email is required") @Email(message = "Email should be valid") String email);
}
