package com.example.authapp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {

    public String generateToken(String username ,String role)  {
        Map<String, Object> claims=new HashMap<>();
        claims.put("role", role);
        return createToken(claims,username);
    }

    private String createToken(Map<String,Object> claims , String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 1000*60*30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode("8f3a7b2c9d4e1f6a5b8c7d2e9f0a3b6c4d8e2f9a0b5c8d7e1f4a9b2c5d8e3f7a");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims verifySignatureandExtractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return verifySignatureandExtractAllClaims(token).getSubject();
    }

    public Date getExpiration(String token) {
        return verifySignatureandExtractAllClaims(token).getExpiration();
    }
    public String extractRole(String token) {
        return verifySignatureandExtractAllClaims(token).get("role", String.class);
    }

    public boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}
