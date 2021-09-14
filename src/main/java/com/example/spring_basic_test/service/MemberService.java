package com.example.spring_basic_test.service;

import com.example.spring_basic_test.domain.entity.MemberEntity;
import com.example.spring_basic_test.domain.repository.MemberRepository;
import com.example.spring_basic_test.dto.MemberDto;
import com.example.spring_basic_test.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberEntity findById(String id) {
        Optional<MemberEntity> memberEntity = memberRepository.findById(id);
        memberEntity.orElseThrow(() -> new MemberNotFoundException(id));
        return memberEntity.get();
    }

    public MemberEntity create (MemberDto.SignUpReq dto) {
        return memberRepository.save(dto.toEntity());
    }

    public MemberEntity updateMember (String id, MemberDto.MemberReq dto) {
        final MemberEntity memberEntity = findById(id);
        memberEntity.updateMember(dto);
        return memberEntity;
    }
}
