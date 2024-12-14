package org.myweb.jobis.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey; // application.properties에서 가져온 비밀 키

    @Value("${jwt.access_expiration}")
    private long accessExpiration; // 액세스 토큰 만료 시간

    @Value("${jwt.refresh_expiration}")
    private long refreshExpiration; // 리프레시 토큰 만료 시간

    /**
     * JWT 액세스 토큰 생성
     *
     * @param uuid 사용자 UUID
     * @param userId 사용자 아이디
     * @param adminYn 관리자 여부
     * @param userName 사용자 이름
     * @return JWT 액세스 토큰
     */
    public String createAccessToken(String uuid, String userId, String adminYn, String userName) {
        return Jwts.builder()
                .setSubject(uuid)
                .claim("userId", userId)
                .claim("adminYn", adminYn)
                .claim("userName", userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * JWT 리프레시 토큰 생성
     *
     * @param uuid 사용자 UUID
     * @return JWT 리프레시 토큰
     */
    public String createRefreshToken(String uuid) {
        return Jwts.builder()
                .setSubject(uuid)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
