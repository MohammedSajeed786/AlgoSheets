package com.algosheets.backend.service.impl;

import com.algosheets.backend.dto.CodeDTO;
import com.algosheets.backend.exception.DaoException;
import com.algosheets.backend.model.Auth;
import com.algosheets.backend.model.InitRequest;
import com.algosheets.backend.model.OAuthResponse;
import com.algosheets.backend.model.UserProfile;
import com.algosheets.backend.repository.AuthRepository;
import com.algosheets.backend.service.AuthService;
import com.algosheets.backend.service.JwtService;
import com.algosheets.backend.utility.HttpUtil;
import com.algosheets.backend.utility.OAuthUtil;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.algosheets.backend.constants.AppConstants.*;
import static com.algosheets.backend.constants.HttpConstants.TOKEN_EXCHANGE_URL;
import static com.algosheets.backend.constants.HttpConstants.USER_PROFILE;

@Service
public class AuthServiceImpl implements AuthService {


    @Autowired
    HttpUtil httpUtil;
    @Autowired
    OAuthUtil oAuthUtil;
    @Autowired
    AuthRepository authRepository;

    @Autowired
    JwtService jwtService;

    @Value("${oauth.client.id}")
    private String clientId;
    @Value("${oauth.client.secret}")
    private String clientSecret;
    @Value("${oauth.redirect.uri}")
    private String redirectUri;

    @Override
    public String handleOAuthCallback(CodeDTO code) {

        //get access token
        OAuthResponse response = exchangeToken(AUTHORIZATION_CODE,code.getCode());


        //get user details from oauth
        UserProfile userProfile=getUserDetails(response.getAccess_token());


        //save to db
        Auth auth = oAuthUtil.convertToAuth(response);
        auth.setEmail(userProfile.getEmailAddresses().get(0).getValue());
        auth=authRepository.save(auth);

        //generate a jwt and return
        return generateJwtToken(userProfile,auth.getUserId());
    }

    private String generateJwtToken(UserProfile userProfile, UUID userId){
        Map<String,Object> claims=new HashMap<>();
        claims.put("email",userProfile.getEmailAddresses().get(0).getValue());
        claims.put("name",userProfile.getNames().get(0).getDisplayName());
        claims.put("profilePicture",userProfile.getPhotos().get(0).getUrl());
        return jwtService.generateToken(claims,userId);
    }
    @Override
    public String refreshToken(String email) {
      Optional<Auth> authOptional=  authRepository.findByEmail(email);
      if(!authOptional.isPresent()) throw new DaoException("email not found in db");

      Auth auth=authOptional.get();
      OAuthResponse response=null;

       //check if access token is expired
        String token=auth.getAccessToken();
        if(auth.getAccessToken()!=null && auth.getTokenExpiry().isBefore(LocalDateTime.now())) {
            //if access token expired
            response = exchangeToken(REFRESH_TOKEN, auth.getRefreshToken());
            auth.setAccessToken(response.getAccess_token());
            auth.setTokenExpiry(LocalDateTime.now().plusSeconds(response.getExpires_in()));

            //update in db
            authRepository.save(auth);
        }

            //get user details from oauth
            UserProfile userProfile=getUserDetails(auth.getAccessToken());

            //generate a jwt and return
            return generateJwtToken(userProfile,auth.getUserId());
    }


    private UserProfile getUserDetails(String accessToken){
        InitRequest<UserProfile> initRequest=new InitRequest<>();
        initRequest.setUrl(USER_PROFILE);
        initRequest.setResponseType(UserProfile.class);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization","Bearer "+accessToken);
        initRequest.setHeaders(headers);
        return (UserProfile) httpUtil.doHttpGet(initRequest).getBody();

    }

    private OAuthResponse exchangeToken(String grantType,String token) {
        InitRequest<OAuthResponse> initRequest = new InitRequest<>();
        initRequest.setUrl(TOKEN_EXCHANGE_URL);
        initRequest.setResponseType(OAuthResponse.class);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        if(grantType.equals(AUTHORIZATION_CODE))
        {
            body.add("code", token);
            body.add("redirect_uri", POST_MESSAGE);
        }
        else body.add("refresh_token",token);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("grant_type", grantType);

        initRequest.setBody(body);
        initRequest.setUrl(TOKEN_EXCHANGE_URL);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        initRequest.setHeaders(headers);
        return (OAuthResponse) httpUtil.doHttpPost(initRequest).getBody();
    }

    @Override
    public boolean isEmailExists(String email) {
        Optional<Auth> authOptional = authRepository.findByEmail(email);
        return authOptional.isPresent();
    }


}
