package com.algosheets.backend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OAuthResponse {
    private String access_token;
    private int expires_in;
    private String token_type;
    private String scope;
    private String refresh_token;
}
