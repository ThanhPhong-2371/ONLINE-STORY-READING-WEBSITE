package com.example.Nhom8.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String username;
    private String avatar;

    public JwtAuthenticationResponse(String accessToken, String username, String avatar) {
        this.accessToken = accessToken;
        this.username = username;
        this.avatar = avatar;
    }
}
