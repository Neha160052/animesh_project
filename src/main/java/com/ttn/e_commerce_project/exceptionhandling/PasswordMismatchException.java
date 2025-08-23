package com.ttn.e_commerce_project.exceptionhandling;

public class PasswordMismatchException extends RuntimeException{

    public PasswordMismatchException(String message)
    {
        super(message);
    }
}
