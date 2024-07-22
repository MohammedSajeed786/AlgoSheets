package com.algosheets.backend.service;

import java.util.Map;
import java.util.UUID;

public interface JwtService {

    String generateToken(Map<String, Object> extraClaims, UUID userId);

    String generateToken(UUID userId);

    String extractUserId(String jwtToken);

    Boolean isTokenValid(String jwtToken, UUID userId);
}
