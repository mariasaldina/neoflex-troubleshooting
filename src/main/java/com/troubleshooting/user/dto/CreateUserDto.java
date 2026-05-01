package com.troubleshooting.user.dto;

public record CreateUserDto(
        String username,
        String password
) {
    @Override
    public String toString() {
        return "CreateUserDto[" +
                "username='" + username +
                "', password='" + password +
                "']";
    }
}
