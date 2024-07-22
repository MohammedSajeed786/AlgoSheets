package com.algosheets.backend.utility;

import com.algosheets.backend.model.Auth;
import com.algosheets.backend.model.OAuthResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OAuthUtil {

    public Auth convertToAuth(OAuthResponse oAuthResponse){
        return Auth.builder()
                .accessToken(oAuthResponse.getAccess_token())
                .refreshToken(oAuthResponse.getRefresh_token())
                .tokenExpiry(LocalDateTime.now().plusSeconds(oAuthResponse.getExpires_in()))
                .build();
    }
}
