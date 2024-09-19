package com.bananaapps.MyOnlineShoppingService.domain.exception.custom;

public class NoSuchAccountException extends RuntimeException{
    public NoSuchAccountException(String message){
        super(message);
    }
}
