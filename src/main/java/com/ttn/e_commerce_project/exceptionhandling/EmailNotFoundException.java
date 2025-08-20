package com.ttn.e_commerce_project.exceptionhandling;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String message) {
        super(message);
    }
}

