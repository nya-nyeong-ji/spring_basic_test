package com.example.spring_basic_test.dto;

import com.example.spring_basic_test.domain.entity.MemberEntity;
import com.example.spring_basic_test.domain.model.Email;
import com.example.spring_basic_test.domain.model.Password;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

public class MemberDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpReq {
        @NotEmpty
        private String id;
        @NotEmpty
        private String nickname;
        @Valid
        private Email email;
        @NotEmpty
        private String password;

        @Builder
        public SignUpReq(String id, String nickname, Email email, String password) {
            this.id = id;
            this.nickname = nickname;
            this.email = email;
            this.password = password;
        }

        public MemberEntity toEntity() {
            return MemberEntity.builder()
                    .id(id)
                    .nickname(nickname)
                    .email(email)
                    .password(Password.builder().value(password).build())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MemberReq {
        private String nickname;
        private Email email;
        private Password password;

        @Builder
        public MemberReq(String nickname, Email email, Password password) {
            this.nickname = nickname;
            this.email = email;
            this.password = password;
        }
    }

    @Getter
    public static class Res {
        private String id;
        private String nickname;
        private Email email;
        private Password password;

        public Res(MemberEntity me) {
            this.id = me.getId();
            this.nickname = me.getNickname();
            this.email = me.getEmail();
            this.password = me.getPassword();
        }
    }


}
