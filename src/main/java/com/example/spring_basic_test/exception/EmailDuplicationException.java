package com.example.spring_basic_test.exception;

import com.example.spring_basic_test.domain.model.Email;
import lombok.Getter;

@Getter
public class EmailDuplicationException extends RuntimeException{
    private Email email;
    private String field;

    public EmailDuplicationException(Email email) {
        this.email = email;
        this.field = "email";
    }
}
