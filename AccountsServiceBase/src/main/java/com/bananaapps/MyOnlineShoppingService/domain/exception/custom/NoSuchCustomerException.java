package com.bananaapps.MyOnlineShoppingService.domain.exception.custom;

public class NoSuchCustomerException extends RuntimeException{
    public NoSuchCustomerException(String message){
        super(message);
    }
}
