package com.example.spring_basic_test.exception;

import com.example.spring_basic_test.error.ErrorCode;
import lombok.Getter;

@Getter
public class PasswordFailedException extends RuntimeException {

    private ErrorCode errorCode;

    public PasswordFailedException() {
        this.errorCode = ErrorCode.PASSWORD_FAILED_EXCEEDED;
    }
}