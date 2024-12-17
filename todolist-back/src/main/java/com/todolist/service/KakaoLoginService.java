package com.todolist.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todolist.model.KakaoLoginDTO;
import com.todolist.model.MemberEntity;
import com.todolist.repository.MemberRepository;

@Service
public class KakaoLoginService {

	private static final String KAKAO_USER_INFO_API = "https://kapi.kakao.com/v2/user/me";
	
	private static final Logger logger = LoggerFactory.getLogger(KakaoLoginService.class);
	
	@Autowired
    private MemberRepository memberRepository;
	
	
	public MemberEntity handleKakaoLogin(KakaoLoginDTO kakaoLoginDto) {
		try {
			logger.info("Processing Kakao login for tokenId: {}", kakaoLoginDto.getTokenId());
	
	        
	        String tokenId = kakaoLoginDto.getTokenId();
	        // Verify Google token
	        String email = kakaoLoginDto.getEmail();
	        System.out.println(email);
	        // Check if user already exists
	        MemberEntity member = memberRepository.findByEmail(email);
	        if (member != null) {
	            logger.info("Existing user found: {}", email);
	            return member;
	        }else {
	        	
	            // Register new user
	            member = new MemberEntity();
	            member.setEmail(email);
	            member.setNickname(kakaoLoginDto.getNickname());
	            member.setTokenId(tokenId);
	            memberRepository.save(member);
	
	            logger.info("New user registered: {}", email);
	            return member;
	        }
	
	    }catch (Exception e) {
	        logger.error("Unexpected error during Kakao login", e);
	        throw new RuntimeException("Unexpected error during Kakao login", e);
	    }
	}
	
}
