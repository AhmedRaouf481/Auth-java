package com.araouf.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.araouf.server.domain.db.OtpInfo;

public interface OtpInfoRepository extends JpaRepository<OtpInfo,Integer> {

    OtpInfo findByUserEmailAndOtp(String email, String otp);
    OtpInfo findByUserEmail(String email);
}
