package com.todolist.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;  // 이 부분이 변경됨
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ConfigController {

    @Value("${oauth.kakao.client-id}")
    private String kakaoClientId;

    @Value("${oauth.google.client-id}")
    private String googleClientId;

    @GetMapping("/config")
    public ResponseEntity<Map<String, String>> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("KAKAO_KEY", kakaoClientId);
        config.put("GOOGLE_CLIENT_ID", googleClientId);
        return ResponseEntity.ok(config);
    }
}