package com.algosheets.backend.exception;

import lombok.Data;

import java.util.List;

@Data
public class BadRequestException extends RuntimeException{
    private  List<String> invalidFields;
    public BadRequestException(String message, List<String> invalidFields){
        super(message);
        this.invalidFields=invalidFields;
    }
}
