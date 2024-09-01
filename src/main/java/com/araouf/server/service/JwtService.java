package com.araouf.server.service;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;

@Service
public interface JwtService {

    public String extractEmail(String token);

    public Date extractExpiration(String token);

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver);

    public String generateToken(UserDetails userDetails);

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    public boolean isTokenVaild(String token, UserDetails userDetails);

    public boolean isTokenExpired(String token);

    public Claims extractAllClaims(String token);

    public SecretKey getSigningKey(String secret);

}
