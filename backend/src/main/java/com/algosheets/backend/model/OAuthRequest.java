package com.algosheets.backend.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OAuthRequest {
    private String code;
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    private String grant_type;
}
