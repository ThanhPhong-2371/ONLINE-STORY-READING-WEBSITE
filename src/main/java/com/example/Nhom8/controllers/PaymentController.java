package com.example.Nhom8.controllers;

import com.example.Nhom8.service.MomoService;
import com.example.Nhom8.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final MomoService momoService;
    private final VNPayService vnPayService;

    @PostMapping("/momo")
    public ResponseEntity<?> createMomoPayment(@RequestParam Long amount) {
        return ResponseEntity.ok(momoService.createPayment(amount, "Thanh toan goi Premium"));
    }

    @PostMapping("/vnpay")
    public ResponseEntity<?> createVNPayPayment(@RequestParam Long amount, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        return ResponseEntity
                .ok(Map.of("payUrl", vnPayService.createPayment(amount, "Thanh toan goi Premium", ipAddress)));
    }
}
