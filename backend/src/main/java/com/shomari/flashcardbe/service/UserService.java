package com.shomari.flashcardbe.service;


import com.shomari.flashcardbe.entity.User;
import com.shomari.flashcardbe.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create or save a user
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Find user by ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Find user by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    //Find user by email
    public Optional<User> findByEmail(String email) {return userRepository.findByEmail(email);
    }


}
