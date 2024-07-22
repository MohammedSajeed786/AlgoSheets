package com.algosheets.backend.service;

import com.algosheets.backend.dto.CodeDTO;

public interface AuthService {

    public String handleOAuthCallback(CodeDTO code);

    String refreshToken(String email);


    public boolean isEmailExists(String email);
}
