package com.ttn.e_commerce_project.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<Map<String,String>> handleAccountLockedException(AccountLockedException ex)
    {
        Map<String ,String > response = new HashMap<>();
        response.put("error",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.LOCKED);
    }

    @ExceptionHandler(AccountNotActiveException.class)
    public ResponseEntity<Map<String,String>> handleAccountNotActiveException(AccountNotActiveException ex)
    {
        Map<String ,String > response = new HashMap<>();
        response.put("error",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.LOCKED);
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<Map<String,String>> handleAccountNotActiveException(InvalidArgumentException ex)
    {
        Map<String ,String > response = new HashMap<>();
        response.put("error",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.LOCKED);
    }
}
