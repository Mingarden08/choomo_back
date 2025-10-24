package com.mithon.choomo.api.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;


import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret-key}")
    private String secretKey;

    private final String SECRET_KEY = secretKey; // 🔑 비밀 키

    // JWT 생성 (토큰 발급)
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date()) // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24시간 후 만료
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // JWT 검증 & 사용자 이름 가져오기
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // JWT 만료 여부 확인
    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // 토큰에서 Claims(정보) 추출
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
