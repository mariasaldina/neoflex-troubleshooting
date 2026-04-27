package com.troubleshooting.user.service;

import com.troubleshooting.user.dto.CreateUserDto;
import com.troubleshooting.user.dto.GetUserDto;
import com.troubleshooting.user.entity.User;
import com.troubleshooting.user.exception.UserNotFoundException;
import com.troubleshooting.user.mapper.UserMapper;
import com.troubleshooting.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public GetUserDto get(UUID userId) {
        return mapper.toDto(this.repository.findById(userId).orElseThrow(UserNotFoundException::new));
    }

    public byte[] hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] data = password.getBytes(StandardCharsets.UTF_8);

            for (int i = 0; i < 2000000; i++) {
                data = digest.digest(data);
            }

            return data;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public GetUserDto create(CreateUserDto dto) {
        return mapper.toDto(this.repository.save(new User(dto.username(), hash(dto.password()))));
    }

    @Transactional
    public GetUserDto update(UUID userId, String password) {
        User user = this.repository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setPassword(hash(password));
        return mapper.toDto(user);
    }

    @Transactional
    public void delete(UUID userId) {
        this.repository.deleteById(userId);
    }
}
