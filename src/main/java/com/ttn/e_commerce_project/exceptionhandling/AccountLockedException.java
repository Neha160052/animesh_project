package com.ttn.e_commerce_project.exceptionhandling;

public class AccountLockedException extends RuntimeException{

    public AccountLockedException(String message)
    {
        super(message);
    }
}
