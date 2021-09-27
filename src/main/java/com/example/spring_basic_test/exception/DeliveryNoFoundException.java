package com.example.spring_basic_test.exception;

import lombok.Getter;

@Getter
public class DeliveryNoFoundException extends RuntimeException{

    private long id;

    public DeliveryNoFoundException(long id) {
        super(id + " is not found");
        this.id = id;
    }
}
