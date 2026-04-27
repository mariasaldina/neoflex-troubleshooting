package com.troubleshooting.user.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("No user with this id exists :(");
    }
}
