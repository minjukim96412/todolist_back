package com.todolist.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.todolist.model.GoogleLoginDTO;
import com.todolist.model.MemberEntity;
import com.todolist.repository.MemberRepository;

@Service
public class GoogleLoginService {

    private static final Logger logger = LoggerFactory.getLogger(GoogleLoginService.class);

    @Autowired
    private MemberRepository memberRepository;

    public MemberEntity handleGoogleLogin(GoogleLoginDTO googleLoginDto) {
        try {
            logger.info("Processing Google login for tokenId: {}", googleLoginDto.getTokenId());

            
            String tokenId = googleLoginDto.getTokenId();
            // Verify Google token
            String email = googleLoginDto.getEmail();
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
	            member.setNickname(googleLoginDto.getNickname());
	            member.setTokenId(tokenId);
	            memberRepository.save(member);
	
	            logger.info("New user registered: {}", email);
	            return member;
            }

        }catch (Exception e) {
            logger.error("Unexpected error during Google login", e);
            throw new RuntimeException("Unexpected error during Google login", e);
        }
    }

}
