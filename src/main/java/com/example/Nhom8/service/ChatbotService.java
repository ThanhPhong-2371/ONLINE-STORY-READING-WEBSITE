package com.example.Nhom8.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    public String getResponse(String message) {
        // Here you would call an AI API like OpenAI or Gemini
        // For now, let's use a simple switch for demo
        if (message.toLowerCase().contains("premium")) {
            return "Gói Premium của chúng tôi cho phép bạn đọc tất cả truyện khóa và không quảng cáo!";
        }
        if (message.toLowerCase().contains("thanh toán")) {
            return "Chúng tôi hỗ trợ thanh toán trực tuyến.";
        }
        return "Chào bạn! Tôi có thể giúp gì cho bạn về việc mua sách hoặc sử dụng dịch vụ không?";
    }
}
