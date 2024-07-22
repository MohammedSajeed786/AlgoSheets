package com.algosheets.backend.controller;

import com.algosheets.backend.dto.CodeDTO;
import com.algosheets.backend.dto.EmailDTO;
import com.algosheets.backend.requestprocessor.AuthRequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/v1")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    AuthRequestProcessor authRequestProcessor;


    @PostMapping("/validate-email")
    ResponseEntity<Map<String, Boolean>> validateEmail(@RequestBody EmailDTO emailDTO) {

        Map<String, Boolean> response = new HashMap<>();
        Boolean exists = authRequestProcessor.isEmailValid(emailDTO.getEmail());
        response.put("exists", exists);
        if (exists)
            return new ResponseEntity<>(response, HttpStatus.OK);
        else return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/oauth/callback")
    ResponseEntity<Map<String, String>> handleOAuthCallback(CodeDTO request, @RequestHeader("X-Requested-With") String xRequestedWithHeader) {
        String token= authRequestProcessor.handleOAuthCallback(request, xRequestedWithHeader);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    ResponseEntity<Map<String, String>> refreshToken(@RequestBody EmailDTO request) {
        String token= authRequestProcessor.refreshToken(request.getEmail());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}
