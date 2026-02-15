package com.example.Nhom8.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPayService {
    @Value("${vnpay.vnp_TmnCode}")
    private String tmnCode;
    @Value("${vnpay.vnp_HashSecret}")
    private String hashSecret;
    @Value("${vnpay.vnp_Url}")
    private String vnpUrl;
    @Value("${vnpay.vnp_ReturnUrl}")
    private String returnUrl;

    public String createPayment(long amount, String orderInfo, String ipAddress) {
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", tmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", UUID.randomUUID().toString());
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", returnUrl);
        vnp_Params.put("vnp_IpAddr", ipAddress);
        vnp_Params.put("vnp_CreateDate", "20240101120000"); // Example

        // In a real app, you'd sort params and hash them
        return vnpUrl + "?vnp_TmnCode=" + tmnCode + "&vnp_Amount=" + (amount * 100);
    }
}
