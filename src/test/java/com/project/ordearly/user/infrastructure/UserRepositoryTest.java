package com.project.ordearly.user.infrastructure;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.project.ordearly.user.domain.Role;
import com.project.ordearly.user.domain.User;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldFindUser_whenEmailMatches() {
        var savedUser = createValidUser();

        userRepository.save(savedUser);

        var result = userRepository.findByEmailOrUsername("john@mail.com", "wrongUser");

        assertTrue(result.isPresent());
        assertEquals("john@mail.com", result.get().getEmail());
    }

    @Test
    public void shouldFindUser_whenUsernameMatches() {
        var savedUser = createValidUser();

        userRepository.save(savedUser);

        var result = userRepository.findByEmailOrUsername("wrong@mail.com", "john123");

        assertTrue(result.isPresent());
        assertEquals("john123", result.get().getUsername());
    }

    @Test
    public void shouldReturnEmpty_whenEmailAndUsernameDoNotMatch() {
        var result = userRepository.findByEmailOrUsername("wrong@mail.com", "wrongUser");

        assertTrue(result.isEmpty());
    }

    private User createValidUser() {
        return User.builder()
                .firstName("John")
                .lastName("Doe")
                .address("Street 123")
                .birthdate(LocalDate.of(2000, 1, 1))
                .role(Role.ADMIN)
                .username("john123")
                .email("john@mail.com")
                .password("123456")
                .createdAt(ZonedDateTime.now())
                .build();
    }
}