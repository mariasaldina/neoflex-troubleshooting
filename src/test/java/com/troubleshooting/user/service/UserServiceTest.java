package com.troubleshooting.user.service;

import com.troubleshooting.user.dto.CreateUserDto;
import com.troubleshooting.user.dto.GetUserDto;
import com.troubleshooting.user.entity.User;
import com.troubleshooting.user.exception.UserAlreadyExistsException;
import com.troubleshooting.user.exception.UserNotFoundException;
import com.troubleshooting.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Profile("test")
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void create_success() {
        GetUserDto user = this.userService.create(new CreateUserDto("user", "12345"));
        User savedUser = this.userRepository.findById(user.userId()).orElseThrow(
                () -> new UserNotFoundException(user.userId())
        );

        assertEquals("user", savedUser.getUsername());
        assertNotEquals("12345".getBytes(StandardCharsets.UTF_8), savedUser.getPassword());
    }

    @Test
    void create_failure() {
        this.userRepository.save(new User("user", "123".getBytes(StandardCharsets.UTF_8)));

        assertThrows(UserAlreadyExistsException.class, () -> this.userService.create(
                new CreateUserDto("user", "12345")
        ));
    }

    @Test
    void update_success() {
        User user = this.userRepository.save(new User("user", "123".getBytes(StandardCharsets.UTF_8)));
        this.userService.update(user.getUserId(), "aboba");

        assertEquals("aboba", user.getInterests());
    }

    @Test
    void update_failure() {
        assertThrows(UserNotFoundException.class, () -> userService.update(UUID.randomUUID(), "aboba"));
    }
}
