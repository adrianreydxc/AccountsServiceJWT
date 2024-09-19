package com.bananaapps.MyOnlineShoppingService.domain.exception.custom;

public class InsuficientBalanceException extends RuntimeException{
    public InsuficientBalanceException(String message){ super(message); }
}
