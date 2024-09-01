package com.araouf.server.service;

import com.araouf.server.domain.request.AuthRequest;
import com.araouf.server.domain.request.RegisterRequest;
import com.araouf.server.domain.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(AuthRequest authRequest);

    void verifyOtp(String email,String otp);

    void sendOtpEmail(String email);

}
