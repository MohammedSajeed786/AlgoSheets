package com.algosheets.backend.requestprocessor;

import com.algosheets.backend.dto.CodeDTO;

public interface AuthRequestProcessor {

    String handleOAuthCallback(CodeDTO request, String XRequestedWithHeader);

    String refreshToken(String email);
    boolean isEmailValid(String email);
}
