package com.shomari.flashcardbe.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void testCreateUser() {
        System.out.println("Running testCreateUser...");
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@test.com");
        user.setPassword("password");

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User created = userService.saveUser(user);

        Assertions.assertEquals("test", created.getUsername());
        Assertions.assertEquals("test@test.com", created.getEmail());
        System.out.println("testCreateUser passed!");
    }

    @Test
    void testFindUserByEmail() {
        User user = new User();
        user.setEmail("test@test.com");

        Mockito.when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByEmail("test@test.com");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("test@test.com", result.get().getEmail());

    }
}