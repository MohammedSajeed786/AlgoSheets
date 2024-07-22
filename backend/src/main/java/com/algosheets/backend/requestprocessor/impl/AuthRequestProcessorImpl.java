package com.algosheets.backend.requestprocessor.impl;

import com.algosheets.backend.constants.ErrorCodeEnum;
import com.algosheets.backend.dto.CodeDTO;
import com.algosheets.backend.exception.BadRequestException;
import com.algosheets.backend.requestprocessor.AuthRequestProcessor;
import com.algosheets.backend.service.AuthService;
import com.algosheets.backend.utility.AppUtil;
import com.algosheets.backend.utility.OAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.algosheets.backend.constants.AppConstants.XML_HTTP_REQUEST;
import static com.algosheets.backend.constants.RegexConstants.EMAIL_REGEX;

@Component
public class AuthRequestProcessorImpl implements AuthRequestProcessor {

    @Autowired
    AuthService authService;

    @Override
    public String handleOAuthCallback(CodeDTO request, String XRequestedWithHeader) {
        List<String> invalidFields = new ArrayList<>();
        if (!XRequestedWithHeader.equals(XML_HTTP_REQUEST))
            invalidFields.add(ErrorCodeEnum.ErrorXRequestedWithHeader.getDescription());
        if (!AppUtil.isValidString(request.getCode()))
            invalidFields = Collections.singletonList("code is invalid");

        if (invalidFields.size() > 0)
            throw new BadRequestException(ErrorCodeEnum.ErrorBadRequest.getDescription(), invalidFields);

        return authService.handleOAuthCallback(request);

    }
    @Override
    public String refreshToken(String email) {
        List<String> invalidFields = new ArrayList<>();
        if (!AppUtil.isValidString(email) || !AppUtil.isValidPattern(EMAIL_REGEX, email))
            invalidFields = Collections.singletonList("email is invalid");
        if (invalidFields.size() > 0)
            throw new BadRequestException(ErrorCodeEnum.ErrorBadRequest.getDescription(), invalidFields);

        return authService.refreshToken(email);

    }


    @Override
    public boolean isEmailValid(String email) {
        List<String> invalidFields;

        if (!AppUtil.isValidString(email) || !AppUtil.isValidPattern(EMAIL_REGEX, email))
            invalidFields = Collections.singletonList("email is invalid");
        else return authService.isEmailExists(email);

        throw new BadRequestException(ErrorCodeEnum.ErrorBadRequest.getDescription(), invalidFields);


    }
}


