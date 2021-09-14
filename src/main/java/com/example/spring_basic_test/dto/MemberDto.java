package com.example.spring_basic_test.dto;

import com.example.spring_basic_test.domain.entity.MemberEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUpReq {
        private String id;
        private String nickname;
        private String email;
        private String password;

        @Builder
        public SignUpReq(String id, String nickname, String email, String password) {
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
                    .password(password)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MemberReq {
        private String nickname;
        private String email;
        private String password;

        @Builder
        public MemberReq(String nickname, String email, String password) {
            this.nickname = nickname;
            this.email = email;
            this.password = password;
        }
    }

    @Getter
    public static class Res {
        private String id;
        private String nickname;
        private String email;
        private String password;

        public Res(MemberEntity me) {
            this.id = me.getId();
            this.nickname = me.getNickname();
            this.email = me.getEmail();
            this.password = me.getPassword();
        }
    }
}
