package com.algosheets.backend.utility;

import com.algosheets.backend.constants.ErrorCodeEnum;
import com.algosheets.backend.exception.HttpException;
import com.algosheets.backend.model.InitRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Component
public class HttpUtil {

    @Autowired
    RestTemplate restTemplate;

    public <T> ResponseEntity<T> doHttpPost(InitRequest<T> initRequest) {

        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            for (Map.Entry<String, String> header : initRequest.getHeaders().entrySet()) {
                httpHeaders.set(header.getKey(), header.getValue());
            }
            HttpEntity httpEntity = new HttpEntity(initRequest.getBody(), httpHeaders);
            return restTemplate.exchange(initRequest.getUrl(), HttpMethod.POST, httpEntity, initRequest.getResponseType());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle client and server errors
            throw new HttpException(ErrorCodeEnum.ErrorExternalApiCall.getDescription()+" "+e.getStatusCode() + " " + e.getResponseBodyAsString());
        } catch (Exception e) {
            // Handle other errors
            throw new HttpException(ErrorCodeEnum.ErrorInternalError.getDescription()+" "+e.getMessage());
        }
    }

    public <T> ResponseEntity<T> doHttpGet(InitRequest<T> initRequest) {

        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            for (Map.Entry<String, String> header : initRequest.getHeaders().entrySet()) {
                httpHeaders.set(header.getKey(), header.getValue());
            }
            HttpEntity httpEntity = new HttpEntity(httpHeaders);
            return restTemplate.exchange(initRequest.getUrl(), HttpMethod.GET, httpEntity, initRequest.getResponseType());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle client and server errors
            throw new HttpException(ErrorCodeEnum.ErrorExternalApiCall.getDescription()+" "+e.getStatusCode() + " " + e.getResponseBodyAsString());
        } catch (Exception e) {
            // Handle other errors
            throw new HttpException(ErrorCodeEnum.ErrorInternalError.getDescription()+" "+e.getMessage());
        }
    }



}
