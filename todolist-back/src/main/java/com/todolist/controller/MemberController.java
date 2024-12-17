package com.todolist.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todolist.model.GoogleLoginDTO;
import com.todolist.model.KakaoLoginDTO;
import com.todolist.model.MemberEntity;
import com.todolist.service.GoogleLoginService;
import com.todolist.service.KakaoLoginService;
import com.todolist.service.MemberService;

@RestController
@RequestMapping("/api/login")
public class MemberController {

    @Autowired
    private MemberService memberService;
    
    @Autowired
    private GoogleLoginService googleLoginService;


    @Autowired
    private KakaoLoginService kakaoLoginService;
    
    // 구글 로그인 API
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/google")
    public ResponseEntity<MemberEntity> googleLogin(@RequestBody GoogleLoginDTO googleLoginDto) {
        MemberEntity member = googleLoginService.handleGoogleLogin(googleLoginDto);
        return ResponseEntity.ok(member);
    }


    // 카카오 로그인 API
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/kakao")
    public ResponseEntity<MemberEntity> kakaoLogin(@RequestBody KakaoLoginDTO kakaoLoginDto) {
    	MemberEntity member = kakaoLoginService.handleKakaoLogin(kakaoLoginDto);
    	return ResponseEntity.ok(member);
    }
    
    // 닉네임 중복 검사 API
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/check-nickname")
    public ResponseEntity<Map<String, Object>> checkNickname(@RequestBody Map<String, String> request) {
        String nickname = request.get("nickname");
        if (nickname == null || nickname.trim().isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "닉네임이 비어 있습니다.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        boolean exists = memberService.checkNicknameExists(nickname);
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @CrossOrigin(origins = "http://localhost:3000")  // Allow CORS for frontend
    @PostMapping("/check-user")
    public ResponseEntity<Map<String, Object>> checkUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (email == null || email.trim().isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "회원 정보가 없습니다.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        MemberEntity member = memberService.checkEmailExists(email);
        Map<String, Object> response = new HashMap<>();
        if (member != null) {
            response.put("exists", true);
            response.put("user", Map.of(
                "memId", member.getMemId(),
                "email", member.getEmail(),
                "nickname", member.getNickname(),
                "tokenId", member.getTokenId()
            ));
        } else {
            response.put("exists", false);
        }

        return ResponseEntity.ok(response);
    }



 
}

