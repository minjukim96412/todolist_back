package com.todolist.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.util.Value;

@RestController
@RequestMapping("/api")
public class ConfigController {

    @Value("${oauth.kakao.client-id}")  // application.properties에서 가져올 값
    private String kakaoClientId;

    @Value("${oauth.google.client-id}")  // application.properties에서 가져올 값
    private String googleClientId;

    @GetMapping("/config")
    public ResponseEntity<Map<String, String>> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("KAKAO_KEY", kakaoClientId);
        config.put("GOOGLE_CLIENT_ID", googleClientId);
        return ResponseEntity.ok(config);
    }
}
