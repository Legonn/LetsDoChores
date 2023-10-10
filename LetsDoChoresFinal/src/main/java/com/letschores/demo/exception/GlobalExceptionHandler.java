package com.letschores.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String,String> handleValidationException(MethodArgumentNotValidException ex){
        BindingResult bindingResult=ex.getBindingResult();
        Map<String,String> errors=new HashMap<>();

        bindingResult.getFieldErrors().forEach(fieldError -> {
            String fieldName=fieldError.getField();
            String errorMessage=fieldError.getDefaultMessage();
            errors.put(fieldName,errorMessage);
        });
        return errors;
    }
}
