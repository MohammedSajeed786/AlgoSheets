package com.algosheets.backend.constants;

import lombok.Data;
import lombok.Getter;

@Getter
public enum ErrorCodeEnum {

    ErrorXRequestedWithHeader("header X-Requested-With is either missing or invalid"),
    ErrorBadRequest("bad request"),
    ErrorAccessTokenRequest("error fetching access token"),
    ErrorExternalApiCall("external API call failed"),
    ErrorInternalError("Internal Server error");

    private String description;
    ErrorCodeEnum(String description){this.description=description;}
}
