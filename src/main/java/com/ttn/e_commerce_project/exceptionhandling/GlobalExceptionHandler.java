package com.ttn.e_commerce_project.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.FileNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleResourceNotFound(ResourceNotFoundException ex)
    {
        Map<String ,String > response = new HashMap<>();
        response.put("error",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<Map<String,String>> handleAccountLockedException(AccountLockedException ex)
    {
        Map<String ,String > response = new HashMap<>();
        response.put("error",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotActiveException.class)
    public ResponseEntity<Map<String,String>> handleAccountNotActiveException(AccountNotActiveException ex)
    {
        Map<String ,String > response = new HashMap<>();
        response.put("error",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<Map<String,String>> handleAccountNotActiveException(InvalidArgumentException ex)
    {
        Map<String ,String > response = new HashMap<>();
        response.put("error",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Map<String,String>> handleDuplicateEntryException(SQLIntegrityConstraintViolationException ex)
    {
        Map<String ,String > response = new HashMap<>();
        response.put("error",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String,String>> handleNullPointerException(NullPointerException ex)
    {
        Map<String ,String > response = new HashMap<>();
        response.put("error",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<Map<String,String>> handlePasswordMismatchException(PasswordMismatchException ex)
    {
        Map<String ,String > response = new HashMap<>();
        response.put("error",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex)
    {
        Map<String ,String > response = new HashMap<>();
        response.put("error",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String,String>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex)
    {
        Map<String ,String > response = new HashMap<>();
        response.put("error",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String,String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex)
    {
        Map<String ,String > response = new HashMap<>();
        response.put("error",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleFileNotFoundException(FileNotFoundException ex)
    {
        Map<String ,String > response = new HashMap<>();
        response.put("error",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String,String>> handleBadCrendentialsException(BadCredentialsException ex)
    {
        Map<String ,String > response = new HashMap<>();
        response.put("error",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Map<String,String>> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex)
    {
        Map<String ,String > response = new HashMap<>();
        response.put("error",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}
