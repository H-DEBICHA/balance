package com.yassir.bank.balance.exception;

public class EntityAlreadyExist extends RuntimeException{
    public EntityAlreadyExist(String msg) {
        super(msg);
    }
}
