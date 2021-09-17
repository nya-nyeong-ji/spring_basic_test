package com.example.spring_basic_test.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
    MEMBER_NOT_FOUND("AC_001", "해당 멤버를 찾을 수 없습니다.", 404),
    ID_DUPLICATION("AC_002", "아이디가 중복도었습니다.", 405),
    EMAIL_DUPLICATION("AC_003", "이메일이 중복되었습니다.", 406),
    INPUT_INVALID("???", "올바르지 못한 입력입니다.", 407);

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
