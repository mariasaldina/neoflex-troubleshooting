package com.troubleshooting.user.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID userId) {
        super("No user with id %s exists :(".formatted(userId));
    }
}
