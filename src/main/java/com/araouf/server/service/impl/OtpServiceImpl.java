package com.araouf.server.service.impl;

import java.time.LocalDateTime;
import java.util.Random;
import org.springframework.stereotype.Service;

import com.araouf.server.domain.db.OtpInfo;
import com.araouf.server.domain.db.User;
import com.araouf.server.exceptions.ResourceNotFoundException;
import com.araouf.server.repository.OtpInfoRepository;
import com.araouf.server.repository.UserRepository;
import com.araouf.server.service.EmailService;
import com.araouf.server.service.OtpService;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    public static final int OTP_ATTEMPTS_LIMIT = 3;
    public static final int OTP_EXPIRY_MINUTES = 5;
    public static final int OTP_RESET_WAITING_TIME_MINUTES = 10;
    public static final int OTP_RETRY_LIMIT_WINDOW_MINUTES = 15;

    private final OtpInfoRepository otpInfoRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    public String generateOtp(String email) {
        Random random = new Random();
        Integer otpValue = 100_000 + random.nextInt(900_000);
        String otp = String.valueOf(otpValue);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));
        OtpInfo newOtp = OtpInfo.builder()
                .user(user)
                .otp(otp)
                .generatedAt(LocalDateTime.now())
                .build();

        otpInfoRepository.save(newOtp);

        return otp;
    }

    @Override
    public void sendOtpEmail(String email, String name) {
        OtpInfo existOtp = otpInfoRepository.findByUserEmail(email);
        if (existOtp != null) {
            otpInfoRepository.delete(existOtp);
        }
        String otp = generateOtp(email);
        emailService.send(email, emailService.getVerficationEmailTemplate(name, otp));
    }

    @Override
    public void validateOtp(String email, String otp) {
        val otpInfo = otpInfoRepository.findByUserEmailAndOtp(email, otp);
        if (otpInfo == null) {
            throw new ResourceNotFoundException("Invalid OTP");
        }
        if (isOtpExpired(otpInfo)) {
            throw new ResourceNotFoundException("OTP expired");
        }
        otpInfoRepository.delete(otpInfo);

    }

    private boolean isOtpExpired(OtpInfo otpInfo) {
        val now = LocalDateTime.now();
        val generatedAt = otpInfo.getGeneratedAt();
        val expired = generatedAt.isBefore(now.minusMinutes(OTP_EXPIRY_MINUTES));
        if (expired) {
            otpInfoRepository.delete(otpInfo);
        }

        return expired;
    }

}
