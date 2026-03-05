package com.example.Nhom8.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Chatbot service — uses Ollama for AI responses, with hybrid search context.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotService {

    private final OllamaService ollamaService;
    private final HybridSearchService hybridSearchService;

    private static final String SYSTEM_PROMPT = """
            Bạn là trợ lý AI của website đọc truyện tranh online. \
            Bạn giúp người dùng tìm truyện, đề xuất truyện hay, và trả lời câu hỏi liên quan đến truyện tranh. \
            Hãy trả lời bằng tiếng Việt, thân thiện và ngắn gọn. \
            Nếu có kết quả tìm kiếm được cung cấp, hãy dựa vào đó để trả lời.""";

    public String getResponse(String message) {
        try {
            // Search for relevant stories to provide context
            List<HybridSearchService.SearchResult> results = hybridSearchService.hybridSearch(message, 5);

            String context = "";
            if (!results.isEmpty()) {
                context = "\n\nKết quả tìm kiếm liên quan:\n" + results.stream()
                        .map(r -> String.format("- %s (by %s) [%s] — %s",
                                r.getTitle(),
                                r.getAuthor(),
                                r.getGenres() != null ? String.join(", ", r.getGenres()) : "",
                                r.getStatus()))
                        .collect(Collectors.joining("\n"));
            }

            return ollamaService.chat(SYSTEM_PROMPT, message + context);
        } catch (Exception e) {
            log.warn("Ollama chat failed, using fallback: {}", e.getMessage());
            return getFallbackResponse(message);
        }
    }

    private String getFallbackResponse(String message) {
        if (message.toLowerCase().contains("premium")) {
            return "Gói Premium của chúng tôi cho phép bạn đọc tất cả truyện khóa và không quảng cáo!";
        }
        if (message.toLowerCase().contains("thanh toán")) {
            return "Chúng tôi hỗ trợ thanh toán trực tuyến.";
        }
        return "Xin lỗi, hệ thống AI đang không khả dụng. Vui lòng thử lại sau!";
    }
}
