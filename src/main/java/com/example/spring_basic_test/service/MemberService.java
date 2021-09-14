package com.example.spring_basic_test.service;

import com.example.spring_basic_test.domain.entity.MemberEntity;
import com.example.spring_basic_test.domain.repository.MemberRepository;
import com.example.spring_basic_test.dto.MemberDto;
import com.example.spring_basic_test.error.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberEntity create (MemberDto.SignUpReq dto) {
        return memberRepository.save(dto.toEntity());
    }

    @Transactional(readOnly = true)
    public MemberEntity findById(String id) {
        final Optional<MemberEntity> memberEntity = memberRepository.findById(id);
        if (memberEntity == null) {
            throw new MemberNotFoundException(id);
        }
        return memberEntity.get();
    }

    public MemberEntity updateMember (String id, MemberDto.MemberReq dto) {
        final MemberEntity memberEntity = findById(id);
        memberEntity.updateMember(dto);
        return memberEntity;
    }
}
