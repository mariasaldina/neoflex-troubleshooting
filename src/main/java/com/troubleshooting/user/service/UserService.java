package com.troubleshooting.user.service;

import com.troubleshooting.user.dto.CreateUserDto;
import com.troubleshooting.user.dto.GetUserDto;
import com.troubleshooting.user.entity.User;
import com.troubleshooting.user.exception.UserAlreadyExistsException;
import com.troubleshooting.user.exception.UserNotFoundException;
import com.troubleshooting.user.mapper.UserMapper;
import com.troubleshooting.user.masking.MaskingComponent;
import com.troubleshooting.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final MaskingComponent maskingComponent;

    public GetUserDto get(UUID userId) {
        log.trace("<< get");
        log.debug("Входные параметры: userId: {}", userId);
        GetUserDto result = mapper.toDto(this.repository
                .findById(userId)
                .orElseThrow(() -> {
                    UserNotFoundException e = new UserNotFoundException(userId);
                    log.error(e.getMessage());
                    return e;
                })
        );
        log.trace(">> get");
        return result;
    }

    public byte[] hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] data = password.getBytes(StandardCharsets.UTF_8);

            for (int i = 0; i < 2000000; i++) {
                data = digest.digest(data);
            }
            log.debug("Сгенерирован хэш пароля");

            return data;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public GetUserDto create(CreateUserDto dto) {
        log.trace(">> create");
        log.debug("Входные параметры: CreateUserDto: {}", maskingComponent.maskInfo(dto.toString()));

        User user = this.repository.findByUsername(dto.username());
        if (user != null) {
            UserAlreadyExistsException e = new UserAlreadyExistsException(dto.username());
            log.error(e.getMessage());
            throw e;
        }

        GetUserDto savedUser = mapper.toDto(this.repository.save(new User(
                dto.username(),
                hash(dto.password()))
        ));
        log.info("Создан пользователь: {}", savedUser.toString());

        log.trace("<< create");
        return savedUser;
    }

    @Transactional
    public GetUserDto update(UUID userId, String interests) {
        log.trace(">> update");
        log.debug("Входные параметры: userId: {}, interests: {}", userId, interests);

        User user = this.repository.findById(userId).orElseThrow(() -> {
            UserNotFoundException e = new UserNotFoundException(userId);
            log.error(e.getMessage());
            return e;
        });

        if (interests == null) {
            log.warn("Параметр interests не указан");
        }

        user.setInterests(interests);
        log.trace("<< update");
        return mapper.toDto(user);
    }

    @Transactional
    public void delete(UUID userId) {
        log.trace(">> delete");
        log.debug("Входные параметры: userId: {}", userId);

        this.repository.deleteById(userId);
        log.info("Удален пользователь: {}", userId);

        log.trace("<< delete");
    }
}
