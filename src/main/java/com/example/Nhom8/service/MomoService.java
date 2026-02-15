package com.example.Nhom8.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MomoService {
    @Value("${momo.partnerCode}")
    private String partnerCode;
    @Value("${momo.accessKey}")
    private String accessKey;
    @Value("${momo.secretKey}")
    private String secretKey;
    @Value("${momo.endpoint}")
    private String endpoint;
    @Value("${momo.returnUrl}")
    private String returnUrl;
    @Value("${momo.notifyUrl}")
    private String notifyUrl;

    public Map<String, Object> createPayment(Long amount, String orderInfo) {
        String orderId = UUID.randomUUID().toString();
        String requestId = UUID.randomUUID().toString();

        // In a real app, you'd calculate the signature here using HMAC SHA256
        // For this demo, I'll return a mock response structure

        Map<String, Object> response = new HashMap<>();
        response.put("partnerCode", partnerCode);
        response.put("orderId", orderId);
        response.put("amount", amount);
        response.put("payUrl", "https://test-payment.momo.vn/v2/gateway/api/create?orderId=" + orderId);

        return response;
    }
}
