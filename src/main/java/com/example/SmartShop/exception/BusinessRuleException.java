package com.example.SmartShop.exception;

public class BusinessRuleException extends RuntimeException{
    public BusinessRuleException(String message){
        super(message);
    }
}
