package com.todolist.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.model.GoogleLoginDTO;
import com.todolist.model.KakaoLoginDTO;
import com.todolist.model.MemberEntity;
import com.todolist.service.GoogleLoginService;
import com.todolist.service.KakaoLoginService;
import com.todolist.service.MemberService;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @Mock
    private GoogleLoginService googleLoginService;

    @Mock
    private KakaoLoginService kakaoLoginService;

    @InjectMocks
    private MemberController memberController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGoogleLogin() throws Exception {
        GoogleLoginDTO googleLoginDTO = new GoogleLoginDTO();
        googleLoginDTO.setTokenId("testToken");

        MemberEntity member = new MemberEntity();
        member.setMemId(1);
        member.setNickname("testUser");

        when(googleLoginService.handleGoogleLogin(any(GoogleLoginDTO.class))).thenReturn(member);

        mockMvc.perform(post("/api/login/google")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(googleLoginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memId").value(1))
                .andExpect(jsonPath("$.nickname").value("testUser"));

        verify(googleLoginService, times(1)).handleGoogleLogin(any(GoogleLoginDTO.class));
    }

    @Test
    void testKakaoLogin() throws Exception {
        KakaoLoginDTO kakaoLoginDTO = new KakaoLoginDTO();
        kakaoLoginDTO.setTokenId("testToken");

        MemberEntity member = new MemberEntity();
        member.setMemId(2);
        member.setNickname("kakaoUser");

        when(kakaoLoginService.handleKakaoLogin(any(KakaoLoginDTO.class))).thenReturn(member);

        mockMvc.perform(post("/api/login/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(kakaoLoginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memId").value(2))
                .andExpect(jsonPath("$.nickname").value("kakaoUser"));

        verify(kakaoLoginService, times(1)).handleKakaoLogin(any(KakaoLoginDTO.class));
    }

    @Test
    void testCheckNickname() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("nickname", "testNickname");

        when(memberService.checkNicknameExists("testNickname")).thenReturn(true);

        mockMvc.perform(post("/api/login/check-nickname")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true));

        verify(memberService, times(1)).checkNicknameExists("testNickname");
    }

    @Test
    void testCheckUser() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("email", "test@example.com");

        MemberEntity member = new MemberEntity();
        member.setMemId(1);
        member.setEmail("test@example.com");
        member.setNickname("testUser");
        member.setTokenId("testToken");

        when(memberService.checkEmailExists("test@example.com")).thenReturn(member);

        mockMvc.perform(post("/api/login/check-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true))
                .andExpect(jsonPath("$.user.memId").value(1))
                .andExpect(jsonPath("$.user.email").value("test@example.com"))
                .andExpect(jsonPath("$.user.nickname").value("testUser"))
                .andExpect(jsonPath("$.user.tokenId").value("testToken"));

        verify(memberService, times(1)).checkEmailExists("test@example.com");
    }
}
