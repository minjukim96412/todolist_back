package com.todolist.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import com.todolist.model.KakaoLoginDTO;
import com.todolist.model.MemberEntity;
import com.todolist.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class KakaoLoginServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private KakaoLoginService kakaoLoginService;

    private KakaoLoginDTO kakaoLoginDto;
    private MemberEntity existingMember;
    private MemberEntity newMember;

    @BeforeEach
    void setUp() {
        kakaoLoginDto = new KakaoLoginDTO();
        kakaoLoginDto.setEmail("test@example.com");
        kakaoLoginDto.setNickname("TestUser");
        kakaoLoginDto.setTokenId("dummyTokenId");

        existingMember = new MemberEntity();
        existingMember.setEmail("test@example.com");
        existingMember.setNickname("ExistingUser");
        existingMember.setTokenId("existingTokenId");

        newMember = new MemberEntity();
        newMember.setEmail("test@example.com");
        newMember.setNickname("TestUser");
        newMember.setTokenId("dummyTokenId");
    }

    @Test
    void testHandleKakaoLogin_ExistingUser() {
        when(memberRepository.findByEmail(kakaoLoginDto.getEmail())).thenReturn(existingMember);

        MemberEntity result = kakaoLoginService.handleKakaoLogin(kakaoLoginDto);

        assertNotNull(result);
        assertEquals(existingMember.getEmail(), result.getEmail());
        verify(memberRepository, times(1)).findByEmail(kakaoLoginDto.getEmail());
        verify(memberRepository, never()).save(any(MemberEntity.class));
    }

    @Test
    void testHandleKakaoLogin_NewUser() {
        when(memberRepository.findByEmail(kakaoLoginDto.getEmail())).thenReturn(null);
        when(memberRepository.save(any(MemberEntity.class))).thenReturn(newMember);

        MemberEntity result = kakaoLoginService.handleKakaoLogin(kakaoLoginDto);

        assertNotNull(result);
        assertEquals(kakaoLoginDto.getEmail(), result.getEmail());
        assertEquals(kakaoLoginDto.getNickname(), result.getNickname());
        verify(memberRepository, times(1)).findByEmail(kakaoLoginDto.getEmail());
        verify(memberRepository, times(1)).save(any(MemberEntity.class));
    }

    @Test
    void testHandleKakaoLogin_Exception() {
        when(memberRepository.findByEmail(kakaoLoginDto.getEmail())).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> 
            kakaoLoginService.handleKakaoLogin(kakaoLoginDto)
        );

        assertEquals("Unexpected error during Kakao login", exception.getMessage());
        verify(memberRepository, times(1)).findByEmail(kakaoLoginDto.getEmail());
    }
}
