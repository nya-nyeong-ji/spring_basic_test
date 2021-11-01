package com.example.spring_basic_test.controller;

import com.example.spring_basic_test.domain.entity.MemberEntity;
import com.example.spring_basic_test.domain.model.Email;
import com.example.spring_basic_test.domain.model.PageRequest;
import com.example.spring_basic_test.dto.MemberDto;
import com.example.spring_basic_test.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public MemberDto.Res signUp(@RequestBody @Valid final MemberDto.SignUpReq dto) {
        return new MemberDto.Res(memberService.create(dto));
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public MemberDto.Res getMember(@PathVariable final String id) {
        return new MemberDto.Res(memberService.findById(id));
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public MemberDto.Res getMemberByEmail(@Valid Email email) {
        return new MemberDto.Res(memberService.findByEmail(email));
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public MemberDto.Res updateMember(@PathVariable final String id, @RequestBody final MemberDto.MemberReq dto) {
        return new MemberDto.Res(memberService.updateMember(id, dto));
    }

    @GetMapping(value = "/page")
    public Page<MemberDto.Res> getMembers(final PageRequest pageable) {
        return memberService.findALL(pageable.of()).map(MemberDto.Res::new);
    }
}
