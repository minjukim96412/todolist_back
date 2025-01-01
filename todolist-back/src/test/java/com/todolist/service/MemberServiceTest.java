package com.todolist.service;

import com.todolist.model.MemberEntity;
import com.todolist.repository.MemberRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;  // MemberRepository를 mock 처리

    @InjectMocks
    private MemberService memberService;  // MemberService는 mock된 repository를 주입받아 사용

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Mockito 초기화
    }
    
    @Test
    void testCheckNicknameExists_ReturnsTrue() {
        // 테스트 데이터 준비
        String nickname = "testNickname";

        // Mockito의 when()을 사용하여 mock 객체가 반환할 값을 설정
        when(memberRepository.existsByNickname(nickname)).thenReturn(true);

        // 실제 서비스 메서드 호출
        boolean result = memberService.checkNicknameExists(nickname);

        // 결과 검증
        assertTrue(result);  // 닉네임이 존재하는 경우 true를 반환해야 한다.
        verify(memberRepository, times(1)).existsByNickname(nickname);  // 메서드가 한 번 호출되었는지 검증
    }

    @Test
    void testCheckNicknameExists_ReturnsFalse() {
        // 테스트 데이터 준비
        String nickname = "uniqueNickname";

        // Mockito의 when()을 사용하여 mock 객체가 반환할 값을 설정
        when(memberRepository.existsByNickname(nickname)).thenReturn(false);

        // 실제 서비스 메서드 호출
        boolean result = memberService.checkNicknameExists(nickname);

        // 결과 검증
        assertFalse(result);  // 닉네임이 존재하지 않으면 false를 반환해야 한다.
        verify(memberRepository, times(1)).existsByNickname(nickname);  // 메서드가 한 번 호출되었는지 검증
    }

    @Test
    void testCheckEmailExists_Found() {
        // 테스트 데이터 준비
        String email = "test@example.com";
        MemberEntity member = new MemberEntity();
        member.setEmail(email);

        // Mockito의 when()을 사용하여 mock 객체가 반환할 값을 설정
        when(memberRepository.findByEmail(email)).thenReturn(member);

        // 실제 서비스 메서드 호출
        MemberEntity result = memberService.checkEmailExists(email);

        // 결과 검증
        assertNotNull(result);  // 이메일이 존재하면 member 객체가 반환되어야 한다.
        assertEquals(email, result.getEmail());  // 이메일이 일치해야 한다.
        verify(memberRepository, times(1)).findByEmail(email);  // 메서드가 한 번 호출되었는지 검증
    }

    @Test
    void testCheckEmailExists_NotFound() {
        // 테스트 데이터 준비
        String email = "nonexistent@example.com";

        // Mockito의 when()을 사용하여 mock 객체가 반환할 값을 설정
        when(memberRepository.findByEmail(email)).thenReturn(null);

        // 실제 서비스 메서드 호출
        MemberEntity result = memberService.checkEmailExists(email);

        // 결과 검증
        assertNull(result);  // 이메일이 존재하지 않으면 null이 반환되어야 한다.
        verify(memberRepository, times(1)).findByEmail(email);  // 메서드가 한 번 호출되었는지 검증
    }
}

