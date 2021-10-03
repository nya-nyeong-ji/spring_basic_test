package com.example.spring_basic_test.exception;

import com.example.spring_basic_test.domain.model.Email;
import lombok.Getter;

@Getter
public class MemberNotFoundException extends RuntimeException{
    private String id;
    private Email email;

    public MemberNotFoundException(String id) {
        this.id = id;
    }
    public MemberNotFoundException(Email email) {
        this.email = email;
    }
}