package com.maksgir.sneakers.service.dto;


import com.maksgir.sneakers.domain.User;

public record TokenRefreshResponse(String accessToken, String refreshToken, User user) {
}
