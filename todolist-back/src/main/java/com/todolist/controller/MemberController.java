package com.todolist.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/login")
@Tag(name = "Member API", description = "사용자 로그인 및 관련 기능 API")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private GoogleLoginService googleLoginService;

    @Autowired
    private KakaoLoginService kakaoLoginService;

    @Operation(summary = "구글 로그인", description = "구글 로그인 API를 사용하여 사용자 정보를 처리합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = MemberEntity.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/google")
    public ResponseEntity<MemberEntity> googleLogin(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "구글 로그인 데이터", required = true, content = @Content(schema = @Schema(implementation = GoogleLoginDTO.class)))
            @RequestBody GoogleLoginDTO googleLoginDto) {
        MemberEntity member = googleLoginService.handleGoogleLogin(googleLoginDto);
        return ResponseEntity.ok(member);
    }

    @Operation(summary = "카카오 로그인", description = "카카오 로그인 API를 사용하여 사용자 정보를 처리합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = MemberEntity.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/kakao")
    public ResponseEntity<MemberEntity> kakaoLogin(
    		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "카카오 로그인 데이터", required = true, content = @Content(schema = @Schema(implementation = KakaoLoginDTO.class)))
            @RequestBody KakaoLoginDTO kakaoLoginDto) {
        MemberEntity member = kakaoLoginService.handleKakaoLogin(kakaoLoginDto);
        return ResponseEntity.ok(member);
    }

    @Operation(summary = "닉네임 중복 검사", description = "닉네임 중복 여부를 확인합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "검사 성공", content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "400", description = "닉네임이 비어 있음")
    })
    @PostMapping("/check-nickname")
    public ResponseEntity<Map<String, Object>> checkNickname(
    		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "닉네임 요청 데이터", required = true, content = @Content(schema = @Schema(type = "object", example = "{\"nickname\": \"example\"}")))
            @RequestBody Map<String, String> request) {
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

    @Operation(summary = "이메일 중복 검사", description = "회원 이메일 정보를 확인합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "검사 성공", content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "400", description = "회원 정보가 없음")
    })
    @PostMapping("/check-user")
    public ResponseEntity<Map<String, Object>> checkUser(
    		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "이메일 요청 데이터", required = true, content = @Content(schema = @Schema(type = "object", example = "{\"email\": \"example@example.com\"}")))
            @RequestBody Map<String, String> request) {
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
