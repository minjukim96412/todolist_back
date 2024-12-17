package com.todolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todolist.model.KakaoLoginDTO;
import com.todolist.model.MemberEntity;
import com.todolist.repository.MemberRepository;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    // 닉네임 중복 검사
    public boolean checkNicknameExists(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    // 이메일 중복 검사
    public MemberEntity checkEmailExists(String email) {
        return memberRepository.findByEmail(email);
    }


}
