package com.bananaapps.MyOnlineShoppingService.domain.exception.custom;

public class NoSuchAccountsException extends RuntimeException{
    public NoSuchAccountsException(String message){ super(message); }
}
