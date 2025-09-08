package com.ttn.e_commerce_project.exceptionhandling;

public class ProductOwnershipException extends RuntimeException{
   public ProductOwnershipException(String message)
   {
       super(message);
   }
}
