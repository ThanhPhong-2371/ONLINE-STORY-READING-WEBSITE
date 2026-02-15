package com.example.Nhom8.controllers;

import com.example.Nhom8.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {
    private final ChatbotService chatbotService;

    @PostMapping("/ask")
    public ResponseEntity<?> ask(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String response = chatbotService.getResponse(message);
        return ResponseEntity.ok(Map.of("response", response));
    }
}
