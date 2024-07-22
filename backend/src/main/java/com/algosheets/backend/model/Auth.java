package com.algosheets.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(schema = "auth")
@Entity
public class Auth {

    @Id
    @GeneratedValue
    private UUID userId;
    @Column(unique = true)
    private String email;
    private String accessToken;
    private LocalDateTime tokenExpiry;
    private String refreshToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
