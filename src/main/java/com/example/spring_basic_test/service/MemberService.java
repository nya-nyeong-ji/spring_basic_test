package com.example.spring_basic_test.service;

import com.example.spring_basic_test.domain.entity.MemberEntity;
import com.example.spring_basic_test.domain.model.Email;
import com.example.spring_basic_test.domain.repository.MemberRepository;
import com.example.spring_basic_test.dto.MemberDto;
import com.example.spring_basic_test.exception.EmailDuplicationException;
import com.example.spring_basic_test.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberEntity create (MemberDto.SignUpReq dto) {
        if (isExistedEmail(dto.getEmail())) {
            throw new EmailDuplicationException(dto.getEmail());
        }
        return memberRepository.save(dto.toEntity());
    }

    @Transactional(readOnly = true)
    public MemberEntity findById(String id) {
        final Optional<MemberEntity> memberEntity = memberRepository.findById(id);
        memberEntity.orElseThrow(() -> new MemberNotFoundException(id));
        return memberEntity.get();
    }

    @Transactional(readOnly = true)
    public MemberEntity findByEmail(Email email) {
        final MemberEntity memberEntity = memberRepository.findByEmail(email);
        if (memberEntity == null) throw new MemberNotFoundException(email);
        return memberEntity;
    }

    public MemberEntity updateMember (String id, MemberDto.MemberReq dto) {
        final MemberEntity memberEntity = findById(id);
        memberEntity.updateMember(dto);
        return memberEntity;
    }

    @Transactional(readOnly = true)
    public Page<MemberEntity> findALL(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    private boolean isExistedEmail(Email email) {
        return memberRepository.findByEmail(email) != null;
    }
}
