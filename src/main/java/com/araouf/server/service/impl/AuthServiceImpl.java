package com.araouf.server.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.araouf.server.domain.db.Role;
import com.araouf.server.domain.db.User;
import com.araouf.server.domain.request.AuthRequest;
import com.araouf.server.domain.request.RegisterRequest;
import com.araouf.server.domain.response.AuthResponse;
import com.araouf.server.exceptions.ResourceNotFoundException;
import com.araouf.server.repository.RoleRepository;
import com.araouf.server.repository.UserRepository;
import com.araouf.server.service.AuthService;
import com.araouf.server.service.JwtService;
import com.araouf.server.service.OtpService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + (request.getRoleId())));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fname(request.getFname())
                .lname(request.getLname())
                .birthDate(request.getBirthDate())
                .gender(request.getGender())
                .phone(request.getPhone())
                .role(role)
                .build();

        userRepository.save(user);

        // otpService.sendOtpEmail(user.getEmail(), user.getFname() + user.getLname());

        String token = jwtService.generateToken(user);
        return AuthResponse.builder().token(token).build();
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));
        String token = jwtService.generateToken(user);
        return AuthResponse.builder().token(token).build();
    }

    @Override
    public void verifyOtp(String email, String otp) {

        // if (!verifyOtpRequest.getOtp().equals("123456")) {
        // throw new ResourceNotFoundException("Invalid OTP");
        // }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found: " + email));
        otpService.validateOtp(email, otp);

        user.setIsEmailVerified(true);
        user.setEnabled(true);
        userRepository.save(user);

    }

    @Override
    public void sendOtpEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found: " + email));
        otpService.sendOtpEmail(user.getEmail(), user.getFname() + user.getLname());

    }

}
