package com.ttn.e_commerce_project.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Map<String,String>> handleDuplicateEntryException(SQLIntegrityConstraintViolationException ex)
    {
        Map<String ,String > response = new HashMap<>();
        response.put("error",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String,String>> handleNullPointerException(SQLIntegrityConstraintViolationException ex)
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
    public ResponseEntity<Map<String,String>> handleMethodArgumentTypeMismatchExceptionException(MethodArgumentTypeMismatchException ex)
    {
        Map<String ,String > response = new HashMap<>();
        response.put("error",ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}
