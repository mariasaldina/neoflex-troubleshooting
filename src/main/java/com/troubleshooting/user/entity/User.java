package com.troubleshooting.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    public User(String username, byte[] password) {
        this.userId = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    @Id
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, columnDefinition = "TEXT")
    private byte[] password;

    private String interests;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
