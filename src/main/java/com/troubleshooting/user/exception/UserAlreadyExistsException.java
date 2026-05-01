package com.troubleshooting.user.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        super("Username %s is taken".formatted(username));
    }
}
