package com.ttn.e_commerce_project.exceptionhandling;

public class AccountNotActiveException extends RuntimeException {
    public AccountNotActiveException(String accountNotActive) {
        super(accountNotActive);
    }
}
