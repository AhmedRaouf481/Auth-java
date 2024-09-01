package com.araouf.server.service.impl;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.araouf.server.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {
    private final String SECRET = "q+b/bFXSxytInnKu5Mp5/KlYyFWSetbqY6DhtLXCfiFVfFhw5bzLaLyAL/7iTALUohu49jtmfR2+ixyUDaFUIP1B8v/SaNi+T4DnsniaK+YY5MXk8FkogLFN60CxLwnG3Gav1nwiLugBUartJ5Q9eL2W0qHxiUwOI6oYw2Rtu+rZpBaSDYcUePHIhtOdCFxz9S00LnBANY+mjSCy+lTInWznfLaztTHa1sHuAgAWgVfVaeUuy+nKgiRh9PF8Lc7SvsokCsQo24wgQ5wjZHv2bWrWDiOV4a/3SC4Jgnwz8g/IfMy98gO3Dt9mVR2ZmOYrURTMj9yIfw8EItN9IJj2+SlMIxTY7JmMfJH8folad+A=";
    private final Integer EXPIRATION = 1000 * 60 * 24 * 30;

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }


    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .signWith(getSigningKey(SECRET))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .compact();
    }

    public boolean isTokenVaild(String token, UserDetails userDetails) {
        final String userEmail = extractEmail(token);
        return (
                userEmail.equals(userDetails.getUsername()) &&
                !isTokenExpired(token)
        );
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey(SECRET))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public SecretKey getSigningKey(String secret) {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

}
