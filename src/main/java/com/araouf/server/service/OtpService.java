package com.araouf.server.service;


public interface OtpService {
    public String generateOtp(String email);
    public void sendOtpEmail(String email,String name);
    public void validateOtp(String email, String otp);
}
