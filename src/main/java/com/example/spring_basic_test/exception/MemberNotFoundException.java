package com.example.spring_basic_test.exception;

import lombok.Getter;

@Getter
public class MemberNotFoundException extends RuntimeException{

    public MemberNotFoundException(String id) {
        super(id + "is not existed!");
    }
}