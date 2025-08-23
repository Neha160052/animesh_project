package com.ttn.e_commerce_project.exceptionhandling;


public class InvalidArgumentException extends RuntimeException {

    public InvalidArgumentException(String message)
    {
       super(message);
    }
}
