package com.example.Nhom8.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChatbotServiceTest {

    @Test
    void getResponse_stillCallsAiWhenHybridSearchFails() {
        OllamaService ollamaService = mock(OllamaService.class);
        HybridSearchService hybridSearchService = mock(HybridSearchService.class);
        when(hybridSearchService.hybridSearch("hello", 5)).thenThrow(new RuntimeException("search down"));
        when(ollamaService.chat(anyString(), eq("hello"))).thenReturn("Xin chào!");

        ChatbotService service = new ChatbotService(ollamaService, hybridSearchService);

        assertEquals("Xin chào!", service.getResponse("hello"));
        verify(ollamaService).chat(anyString(), eq("hello"));
    }

    @Test
    void getResponse_returnsFallbackWhenAiChatFails() {
        OllamaService ollamaService = mock(OllamaService.class);
        HybridSearchService hybridSearchService = mock(HybridSearchService.class);
        when(hybridSearchService.hybridSearch("premium", 5)).thenThrow(new RuntimeException("search down"));
        when(ollamaService.chat(anyString(), eq("premium"))).thenThrow(new RuntimeException("chat down"));

        ChatbotService service = new ChatbotService(ollamaService, hybridSearchService);

        assertEquals("Gói Premium của chúng tôi cho phép bạn đọc tất cả truyện khóa và không quảng cáo!",
                service.getResponse("premium"));
    }
}