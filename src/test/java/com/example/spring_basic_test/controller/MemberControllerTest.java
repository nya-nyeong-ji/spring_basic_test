package com.example.spring_basic_test.controller;

import com.example.spring_basic_test.domain.entity.MemberEntity;
import com.example.spring_basic_test.domain.repository.MemberRepository;
import com.example.spring_basic_test.dto.MemberDto;
import com.example.spring_basic_test.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
class MemberControllerTest {

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;
    private MemberEntity memberEntity;

    @Before
    public void setup() {
        final MemberDto.SignUpReq memberDto = MemberDto.SignUpReq.builder()
                .id("jjj7061")
                .nickname("park")
                .email("~~~@naver.com")
                .password("password")
                .build();

        memberEntity = memberDto.toEntity();

        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                .setControllerAdvice()
                .build();
    }

    @Test
    void signUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                .build();

        final MemberDto.SignUpReq memberDto = MemberDto.SignUpReq.builder()
                .id("jjj7061")
                .nickname("park")
                .email("~~~@naver.com")
                .password("password")
                .build();

        given(memberService.create(any())).willReturn(memberDto.toEntity());

        final ResultActions resultActions = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberDto)))
                .andDo(print());

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nickname", is(memberDto.getNickname())))
                .andExpect(jsonPath("$.email", is(memberDto.getEmail())))
                .andExpect(jsonPath("$.password", is(memberDto.getPassword())));
    }
}