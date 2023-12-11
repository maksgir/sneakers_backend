package com.maksgir.sneakers.service.dto;



public record SignupRequest(
        String username,
        String email,
        String password,
        String firstName,
        String lastName,
        String phone,
        boolean isSeller
) {}

