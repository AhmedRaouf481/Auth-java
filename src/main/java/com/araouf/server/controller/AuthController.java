package com.araouf.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.araouf.server.domain.request.AuthRequest;
import com.araouf.server.domain.request.RegisterRequest;
import com.araouf.server.domain.request.VerifyOtpRequest;
import com.araouf.server.domain.response.AuthResponse;
import com.araouf.server.domain.response.GeneralResponse;
import com.araouf.server.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponse> postMethodName(@RequestBody AuthRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("verify-otp")
    public ResponseEntity<GeneralResponse> verifyOtp(@RequestBody VerifyOtpRequest req) {
           String email = SecurityContextHolder.getContext().getAuthentication().getName();
           authService.verifyOtp(email, req.getOtp());

        return ResponseEntity.ok(GeneralResponse.builder().msg("Otp verified successfully").status("success").statusCode(200).build());
    }

    @PostMapping("send-otp")
    public ResponseEntity<GeneralResponse> sendOtp() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        authService.sendOtpEmail(email);
        return ResponseEntity.ok(GeneralResponse.builder()
                .msg("Otp email sent")
                .status("success")
                .statusCode(200)
                .build());
    }

}
