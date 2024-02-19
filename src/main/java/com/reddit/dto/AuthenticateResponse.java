package com.reddit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticateResponse {
    private String authenticateToken;
    private String username;
    //for the logout. refresh token
    private String refreshToken;
    private Instant expiresAt;
}
