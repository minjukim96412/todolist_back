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

import com.todolist.model.GoogleLoginDTO;
import com.todolist.model.MemberEntity;
import com.todolist.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class GoogleLoginServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private GoogleLoginService googleLoginService;

    private GoogleLoginDTO googleLoginDto;
    private MemberEntity existingMember;
    private MemberEntity newMember;

    @BeforeEach
    void setUp() {
        googleLoginDto = new GoogleLoginDTO();
        googleLoginDto.setEmail("testuser@example.com");
        googleLoginDto.setNickname("TestUser");
        googleLoginDto.setTokenId("testTokenId");

        existingMember = new MemberEntity();
        existingMember.setEmail("testuser@example.com");
        existingMember.setNickname("ExistingUser");
        existingMember.setTokenId("existingTokenId");

        newMember = new MemberEntity();
        newMember.setEmail("testuser@example.com");
        newMember.setNickname("TestUser");
        newMember.setTokenId("testTokenId");
    }

    @Test
    void testHandleGoogleLogin_ExistingUser() {
        // 기존 회원이 존재하는 경우
        when(memberRepository.findByEmail(googleLoginDto.getEmail())).thenReturn(existingMember);

        MemberEntity result = googleLoginService.handleGoogleLogin(googleLoginDto);

        assertNotNull(result);
        assertEquals(existingMember.getEmail(), result.getEmail());
        verify(memberRepository, times(1)).findByEmail(googleLoginDto.getEmail());
        verify(memberRepository, never()).save(any(MemberEntity.class));
    }

    @Test
    void testHandleGoogleLogin_NewUser() {
        // 신규 회원 등록
        when(memberRepository.findByEmail(googleLoginDto.getEmail())).thenReturn(null);
        when(memberRepository.save(any(MemberEntity.class))).thenReturn(newMember);

        MemberEntity result = googleLoginService.handleGoogleLogin(googleLoginDto);

        assertNotNull(result);
        assertEquals(googleLoginDto.getEmail(), result.getEmail());
        assertEquals(googleLoginDto.getNickname(), result.getNickname());
        verify(memberRepository, times(1)).findByEmail(googleLoginDto.getEmail());
        verify(memberRepository, times(1)).save(any(MemberEntity.class));
    }

    @Test
    void testHandleGoogleLogin_Exception() {
        // 예외 처리
        when(memberRepository.findByEmail(googleLoginDto.getEmail())).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> 
            googleLoginService.handleGoogleLogin(googleLoginDto)
        );

        assertEquals("Unexpected error during Google login", exception.getMessage());
        verify(memberRepository, times(1)).findByEmail(googleLoginDto.getEmail()); 
    }
}
