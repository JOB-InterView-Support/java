package com.sevensegment.jobis.sns.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.sevensegment.jobis.security.jwt.JWTUtil;
import com.sevensegment.jobis.user.jpa.entity.UserEntity;
import com.sevensegment.jobis.user.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/kakao")
@RequiredArgsConstructor
public class KakaoController {

    private final RestTemplate restTemplate;

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;


    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${kakao.link-redirect-uri}")
    private String kakaoLinkRedirectUri;

    @PostMapping("/apicode")
    public ResponseEntity<Map<String, String>> kakaoApi(@RequestBody Map<String, String> body, HttpServletRequest request, HttpServletResponse response) {
        String kakaoCode = body.get("code");
        log.info("Received Kakao API code: {}", kakaoCode);

        try {
            // Kakao Token 요청
            String kakaoTokenUrl = "https://kauth.kakao.com/oauth/token";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> kakaoParams = new LinkedMultiValueMap<>();
            kakaoParams.add("grant_type", "authorization_code");
            kakaoParams.add("client_id", kakaoClientId); // Kakao Client ID
            kakaoParams.add("redirect_uri", kakaoRedirectUri); // Kakao Redirect URI
            kakaoParams.add("code", kakaoCode);

            HttpEntity<MultiValueMap<String, String>> kakaoRequest = new HttpEntity<>(kakaoParams, headers);
            ResponseEntity<String> kakaoResponse = restTemplate.postForEntity(kakaoTokenUrl, kakaoRequest, String.class);

            // Kakao ID Token 추출
            String kakaoIdToken = extractIdToken(kakaoResponse.getBody());
            if (kakaoIdToken == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Kakao ID 토큰을 추출할 수 없습니다."));
            }

            // Kakao 이메일 추출
            String kakaoEmail = extractEmailFromIdToken(kakaoIdToken);
            if (kakaoEmail == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Kakao 이메일을 추출할 수 없습니다."));
            }

            log.info("Kakao Email: {}", kakaoEmail);

            // DB에서 이메일 확인
            Optional<UserEntity> kakaoUserOptional = userRepository.findByUserKakaoEmail(kakaoEmail);
            if (kakaoUserOptional.isPresent()) {
                UserEntity kakaoUser = kakaoUserOptional.get();
                log.info("Kakao User exists. ID: {}, PW: {}", kakaoUser.getUserId(), kakaoUser.getUserPw());

                // LoginFilter로 흐름 넘기기
                request.setAttribute("userId", kakaoUser.getUserId());
                request.setAttribute("userPw", kakaoUser.getUserPw());
                request.getRequestDispatcher("/login").forward(request, response);

                return null; // Response는 LoginFilter에서 처리됨
            } else {
                log.info("등록되지 않은 Kakao 이메일: {}", kakaoEmail);
                return ResponseEntity.status(HttpStatus.CONFLICT) // 409 Conflict 반환
                        .body(Map.of(
                                "error", "등록되지 않은 Kakao 이메일입니다.",
                                "email", kakaoEmail // 클라이언트에서 새 회원가입 시 사용
                        ));
            }

        } catch (Exception e) {
            log.error("Kakao login process error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Kakao 로그인 처리 중 오류 발생"));
        }
    }

    private String extractIdToken(String responseBody) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(responseBody);
            return (String) json.get("id_token");
        } catch (ParseException e) {
            log.error("Failed to parse token response: {}", e.getMessage());
            return null;
        }
    }

    private String extractEmailFromIdToken(String idToken) {
        try {
            String[] tokenParts = idToken.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(tokenParts[1]));
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(payload);
            return (String) json.get("email");
        } catch (Exception e) {
            log.error("Failed to decode ID token: {}", e.getMessage());
            return null;
        }
    }

    @PostMapping("/link")
    public ResponseEntity<Map<String, String>> kakaoLink(@RequestBody Map<String, String> body) {
        log.info("카카오 이메일 연동 시작");
        String kakaoCode = body.get("code");
        String uuid = body.get("uuid");
        log.info("Received Kakao code: {}, uuid: {}", kakaoCode, uuid);

        try {
            // Kakao Token 요청
            String kakaoTokenUrl = "https://kauth.kakao.com/oauth/token";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> kakaoParams = new LinkedMultiValueMap<>();
            kakaoParams.add("grant_type", "authorization_code");
            kakaoParams.add("client_id", kakaoClientId);
            kakaoParams.add("redirect_uri", kakaoLinkRedirectUri);
            kakaoParams.add("code", kakaoCode);

            HttpEntity<MultiValueMap<String, String>> kakaoRequest = new HttpEntity<>(kakaoParams, headers);
            ResponseEntity<String> kakaoResponse = restTemplate.postForEntity(kakaoTokenUrl, kakaoRequest, String.class);

            log.info("Kakao Token Response: {}", kakaoResponse.getBody());

            // Kakao 이메일 추출
            JSONParser parser = new JSONParser();
            JSONObject tokenJson = (JSONObject) parser.parse(kakaoResponse.getBody());
            String accessToken = (String) tokenJson.get("access_token");

            String kakaoUserInfoUrl = "https://kapi.kakao.com/v2/user/me";
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth(accessToken);

            HttpEntity<Void> userInfoRequest = new HttpEntity<>(userInfoHeaders);
            ResponseEntity<String> userInfoResponse = restTemplate.exchange(kakaoUserInfoUrl, HttpMethod.GET, userInfoRequest, String.class);

            log.info("Kakao User Info Response: {}", userInfoResponse.getBody());

            JSONObject userInfoJson = (JSONObject) parser.parse(userInfoResponse.getBody());
            JSONObject kakaoAccount = (JSONObject) userInfoJson.get("kakao_account");
            String kakaoEmail = (String) kakaoAccount.get("email");

            log.info("Extracted Kakao Email: {}", kakaoEmail);

            // uuid로 사용자 찾기
            Optional<UserEntity> userOptional = userRepository.findById(uuid);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "사용자를 찾을 수 없습니다."));
            }

            // 사용자 정보 업데이트
            UserEntity user = userOptional.get();
            user.setUserKakaoEmail(kakaoEmail);
            userRepository.save(user);
            log.info("Updated Kakao Email for UUID: {}", uuid);

            Map<String, String> response = new HashMap<>();
            response.put("success", "true");
            response.put("message", "카카오 이메일이 성공적으로 연동되었습니다.");

            return ResponseEntity.ok(response);



        } catch (Exception e) {
            log.error("Error during Kakao linking process: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "카카오 연동 처리 중 오류 발생"));
        }
    }

    @PostMapping("/unlink")
    public ResponseEntity<Map<String, String>> unlinkKakaoEmail(@RequestBody Map<String, String> body) {
        String uuid = body.get("uuid");
        log.info("Received request to unlink Kakao email for UUID: {}", uuid);

        try {
            // uuid로 사용자 찾기
            Optional<UserEntity> userOptional = userRepository.findById(uuid);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "사용자를 찾을 수 없습니다."));
            }

            // Kakao 이메일 해제
            UserEntity user = userOptional.get();
            user.setUserKakaoEmail(null);
            userRepository.save(user);

            log.info("Successfully unlinked Kakao email for UUID: {}", uuid);

            return ResponseEntity.ok(Map.of("message", "카카오 이메일 연동이 해제되었습니다."));
        } catch (Exception e) {
            log.error("Error unlinking Kakao email: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "카카오 이메일 해제 처리 중 오류가 발생했습니다."));
        }
    }



}
