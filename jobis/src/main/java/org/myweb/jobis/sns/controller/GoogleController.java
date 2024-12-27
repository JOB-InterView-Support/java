package org.myweb.jobis.sns.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.user.jpa.entity.UserEntity;
import org.myweb.jobis.user.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/google")
@RequiredArgsConstructor
public class GoogleController {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${google.oauth.client-id}")
    private String googleClientId;

    @Value("${google.oauth.client-secret}")
    private String googleClientSecret;

    @Value("${google.oauth.redirect-uri}")
    private String googleRedirectUri;

    @Value("${google.oauth.link-redirect-uri}")
    private String googleLinkRedirectUri;

    @PostMapping("/apicode")
    public ResponseEntity<Map<String, String>> handleGoogleAuth(@RequestBody Map<String, String> body,
                                                                HttpServletRequest request,
                                                                HttpServletResponse response) {
        String googleCode = body.get("code");
        log.info("Received Google authorization code: {}", googleCode);

        try {
            // 1. Google 토큰 요청
            String googleTokenUrl = "https://oauth2.googleapis.com/token";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> googleParams = new LinkedMultiValueMap<>();
            googleParams.add("grant_type", "authorization_code");
            googleParams.add("client_id", googleClientId);
            googleParams.add("client_secret", googleClientSecret);
            googleParams.add("redirect_uri", googleRedirectUri);
            googleParams.add("code", googleCode);

            HttpEntity<MultiValueMap<String, String>> googleRequest = new HttpEntity<>(googleParams, headers);
            ResponseEntity<Map> googleResponse = restTemplate.postForEntity(googleTokenUrl, googleRequest, Map.class);

            // 2. Google Access Token 추출
            Map<String, Object> tokenResponse = googleResponse.getBody();
            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Google Access Token을 추출할 수 없습니다."));
            }

            String accessToken = (String) tokenResponse.get("access_token");
            log.info("Google Access Token: {}", accessToken);

            // 3. Google 사용자 정보 요청
            String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth(accessToken); // Access Token 설정
            HttpEntity<String> userInfoRequest = new HttpEntity<>(userInfoHeaders);

            ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequest, Map.class);
            Map<String, Object> userInfo = userInfoResponse.getBody();

            if (userInfo == null || !userInfo.containsKey("email")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Google 사용자 정보를 가져올 수 없습니다."));
            }

            String googleEmail = (String) userInfo.get("email");
            log.info("Google Email: {}", googleEmail);

            // 4. DB에서 이메일 확인
            Optional<UserEntity> googleUserOptional = userRepository.findByUserGoogleEmail(googleEmail);
            if (googleUserOptional.isPresent()) {
                UserEntity googleUser = googleUserOptional.get();
                log.info("Google User exists. ID: {}, PW: {}", googleUser.getUserId(), googleUser.getUserPw());

                // LoginFilter로 흐름 넘기기
                request.setAttribute("userId", googleUser.getUserId());
                request.setAttribute("userPw", googleUser.getUserPw());
                request.getRequestDispatcher("/login").forward(request, response);

                return null; // Response는 LoginFilter에서 처리됨
            } else {
                log.info("등록되지 않은 Google 이메일: {}", googleEmail);
                return ResponseEntity.status(HttpStatus.CONFLICT) // 409 Conflict 반환
                        .body(Map.of(
                                "error", "등록되지 않은 Google 이메일입니다.",
                                "email", googleEmail // 클라이언트에서 새 회원가입 시 사용
                        ));
            }

        } catch (Exception e) {
            log.error("Google login process error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Google 로그인 처리 중 오류 발생"));
        }
    }

    @PostMapping("/link")
    public ResponseEntity<Map<String, String>> linkGoogleAccount(@RequestBody Map<String, String> body) {
        String googleCode = body.get("code");
        String uuid = body.get("uuid");
        log.info("Received Google authorization code: {}, UUID: {}", googleCode, uuid);

        try {
            // 1. Google 토큰 요청
            String googleTokenUrl = "https://oauth2.googleapis.com/token";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> googleParams = new LinkedMultiValueMap<>();
            googleParams.add("grant_type", "authorization_code");
            googleParams.add("client_id", googleClientId);
            googleParams.add("client_secret", googleClientSecret);
            googleParams.add("redirect_uri", googleLinkRedirectUri);
            googleParams.add("code", googleCode);

            HttpEntity<MultiValueMap<String, String>> googleRequest = new HttpEntity<>(googleParams, headers);
            ResponseEntity<Map> googleResponse = restTemplate.postForEntity(googleTokenUrl, googleRequest, Map.class);

            // 2. Google Access Token 추출
            Map<String, Object> tokenResponse = googleResponse.getBody();
            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Google Access Token을 추출할 수 없습니다."));
            }

            String accessToken = (String) tokenResponse.get("access_token");
            log.info("Google Access Token: {}", accessToken);

            // 3. Google 사용자 정보 요청
            String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth(accessToken); // Access Token 설정
            HttpEntity<String> userInfoRequest = new HttpEntity<>(userInfoHeaders);

            ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequest, Map.class);
            Map<String, Object> userInfo = userInfoResponse.getBody();

            if (userInfo == null || !userInfo.containsKey("email")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Google 사용자 정보를 가져올 수 없습니다."));
            }

            String googleEmail = (String) userInfo.get("email");
            log.info("Google Email: {}", googleEmail);

            // 4. UUID로 사용자 검색
            Optional<UserEntity> userOptional = userRepository.findById(uuid);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "사용자를 찾을 수 없습니다."));
            }

            // 5. 사용자 정보 업데이트
            UserEntity user = userOptional.get();
            user.setUserGoogleEmail(googleEmail);
            userRepository.save(user);

            log.info("Google Email linked successfully for UUID: {}", uuid);

            return ResponseEntity.ok(Map.of("message", "Google 이메일이 성공적으로 연동되었습니다."));

        } catch (Exception e) {
            log.error("Google linking process error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Google 연동 처리 중 오류 발생"));
        }
    }

    @PostMapping("/unlink")
    public ResponseEntity<Map<String, String>> unlinkGoogleEmail(@RequestBody Map<String, String> body) {
        String uuid = body.get("uuid");
        log.info("Received request to unlink Google email for UUID: {}", uuid);

        try {
            // UUID로 사용자 검색
            Optional<UserEntity> userOptional = userRepository.findById(uuid);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "사용자를 찾을 수 없습니다."));
            }

            // 사용자 정보 업데이트
            UserEntity user = userOptional.get();
            user.setUserGoogleEmail(null); // Google 이메일 해제
            userRepository.save(user);

            log.info("Successfully unlinked Google email for UUID: {}", uuid);

            return ResponseEntity.ok(Map.of("message", "구글 이메일 연동이 성공적으로 해제되었습니다."));
        } catch (Exception e) {
            log.error("Error unlinking Google email: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "구글 이메일 해제 중 오류 발생"));
        }
    }


}
