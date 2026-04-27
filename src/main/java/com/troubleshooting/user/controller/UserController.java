package com.troubleshooting.user.controller;

import com.troubleshooting.user.dto.CreateUserDto;
import com.troubleshooting.user.dto.GetUserDto;
import com.troubleshooting.user.dto.UpdateUserDto;
import com.troubleshooting.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/{userId}")
    public ResponseEntity<GetUserDto> get(@PathVariable UUID userId) {
        return ResponseEntity.ok(this.service.get(userId));
    }

    @PostMapping
    public ResponseEntity<GetUserDto> create(@RequestBody CreateUserDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.create(dto));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<GetUserDto> update(
            @PathVariable UUID userId,
            @RequestBody UpdateUserDto dto
    ) {
        return ResponseEntity.ok(this.service.update(userId, dto.password()));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        this.service.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
