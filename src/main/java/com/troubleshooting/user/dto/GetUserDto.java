package com.troubleshooting.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetUserDto(
        UUID userId,
        String username,
        String interests,
        LocalDateTime createdAt
) {}