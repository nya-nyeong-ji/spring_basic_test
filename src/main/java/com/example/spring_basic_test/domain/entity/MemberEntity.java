package com.example.spring_basic_test.domain.entity;

import com.example.spring_basic_test.domain.model.Email;
import com.example.spring_basic_test.domain.model.Password;
import com.example.spring_basic_test.dto.MemberDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity extends TimeEntity{

    @Id
    private String id;

    @Column(nullable = false)
    private String nickname;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Builder
    public MemberEntity(String id, String nickname, Email email, Password password) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public void updateMember(MemberDto.MemberReq dto) {
        this.nickname = dto.getNickname();
        this.email = dto.getEmail();
        this.password = dto.getPassword();
    }
}
