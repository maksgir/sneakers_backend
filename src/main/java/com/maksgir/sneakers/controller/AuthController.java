package com.maksgir.sneakers.controller;


import com.maksgir.sneakers.exception.UserAlreadySignedUpException;
import com.maksgir.sneakers.repository.UserRepository;
import com.maksgir.sneakers.service.AuthService;
import com.maksgir.sneakers.service.dto.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    UserRepository userRepository;


    @Autowired
    AuthService authService;

    @PostMapping("/login")
     public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("POST /api/auth/login, request: " + loginRequest);
        try {
            return ResponseEntity.ok(authService.auth(loginRequest));
        } catch (Exception e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(new ErrorMessage(500, new Date(), "U nas pizdec", e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        log.info("POST /api/auth/register, request: " + signUpRequest);
        try {
            authService.registerUser(signUpRequest);
            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (UserAlreadySignedUpException e){
            log.error("Couldn't register new user", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("An error occurred: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }

    }
}
