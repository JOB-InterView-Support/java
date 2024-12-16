package org.myweb.jobis.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.myweb.jobis.security.jwt.JWTUtil;
import org.myweb.jobis.user.jpa.entity.UserEntity;
import org.myweb.jobis.user.jpa.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserRepository userRepository) {
        this.setAuthenticationManager(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository; // UserRepository 주입
        setFilterProcessesUrl("/login"); // 로그인 엔드포인트 설정
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            // GET 요청에 대해 예외 대신 응답을 설정하고 요청을 종료
            try {
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED); // 405 상태 반환
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"error\":\"로그인 요청은 POST 방식만 지원합니다.\"}");
            } catch (IOException e) {
                throw new RuntimeException("응답 처리 중 오류 발생", e);
            }
            return null;
        }

        String userId = null;
        String userPw = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), Map.class);
            userId = requestBody.get("userId");
            userPw = requestBody.get("userPw");
        } catch (IOException e) {
            throw new RuntimeException("요청 데이터를 읽을 수 없습니다.", e);
        }

        if (userId == null || userPw == null) {
            throw new RuntimeException("아이디 또는 비밀번호가 전달되지 않았습니다.");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userId, userPw);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = authResult.getName(); // 사용자 이름
        UserEntity user = userRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + username));

        // 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(username); // Access Token 생성
        String refreshToken = jwtUtil.generateRefreshToken(username); // Refresh Token 생성

        System.out.println("발급된 AccessToken: " + accessToken);
        System.out.println("발급된 RefreshToken: " + refreshToken);

        // 리프레시 토큰 DB에 저장
        user.setUserRefreshToken(refreshToken);
        userRepository.save(user);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(String.format("{\"accessToken\":\"%s\",\"refreshToken\":\"%s\"}", accessToken, refreshToken));
    }




    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\":\"로그인 실패: 아이디 또는 비밀번호를 확인하세요.\"}");
    }
}
