package com.bananaapps.MyOnlineShoppingService.domain.exception.custom;

public class AccountsByUserException extends RuntimeException{
    public AccountsByUserException(String message){
        super(message);
    }
}
