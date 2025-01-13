package com.sevensegment.jobis.sns.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.sevensegment.jobis.user.jpa.entity.UserEntity;
import com.sevensegment.jobis.user.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/naver")
@RequiredArgsConstructor
public class NaverLoginController {

    private final UserRepository userRepository;

    @Value("${naver.oauth.client-id}")
    private String naverClientId;

    @Value("${naver.oauth.client-secret}")
    private String naverClientSecret;

    @Value("${naver.oauth.redirect-uri}")
    private String naverRedirectUri;

    @Value("${naver.oauth.link-redirect-uri}")
    private String naverLinkRedirectUri;

    @PostMapping("/apicode")
    public ResponseEntity<Map<String, String>> handleNaverAuth(@RequestBody Map<String, String> body,
                                                               HttpServletRequest request,
                                                               HttpServletResponse response) {
        String naverCode = body.get("code");
        String state = "random_state_value"; // 클라이언트와 일치하는 상태 값
        log.info("Received Naver authorization code: {}", naverCode);

        try {
            // 1. 네이버 토큰 요청
            String naverTokenUrl = "https://nid.naver.com/oauth2.0/token";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> naverParams = new LinkedMultiValueMap<>();
            naverParams.add("grant_type", "authorization_code");
            naverParams.add("client_id", naverClientId);
            naverParams.add("client_secret", naverClientSecret);
            naverParams.add("redirect_uri", naverRedirectUri);
            naverParams.add("code", naverCode);
            naverParams.add("state", state);

            HttpEntity<MultiValueMap<String, String>> naverRequest = new HttpEntity<>(naverParams, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(naverTokenUrl, naverRequest, Map.class);

            Map<String, Object> tokenBody = tokenResponse.getBody();
            if (tokenBody == null || !tokenBody.containsKey("access_token")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Naver Access Token을 추출할 수 없습니다."));
            }

            String accessToken = (String) tokenBody.get("access_token");
            log.info("Naver Access Token: {}", accessToken);

            // 2. 네이버 사용자 정보 요청
            String userInfoUrl = "https://openapi.naver.com/v1/nid/me";
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth(accessToken); // Access Token 설정
            HttpEntity<String> userInfoRequest = new HttpEntity<>(userInfoHeaders);

            ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequest, Map.class);
            Map<String, Object> userInfo = userInfoResponse.getBody();

            if (userInfo == null || !userInfo.containsKey("response")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Naver 사용자 정보를 가져올 수 없습니다."));
            }

            Map<String, Object> responseMap = (Map<String, Object>) userInfo.get("response");
            String naverEmail = (String) responseMap.get("email");
            log.info("Naver Email: {}", naverEmail);

            // 3. DB에서 이메일 확인
            Optional<UserEntity> naverUserOptional = userRepository.findByUserNaverEmail(naverEmail);
            if (naverUserOptional.isPresent()) {
                UserEntity naverUser = naverUserOptional.get();
                log.info("Naver User exists. ID: {}, PW: {}", naverUser.getUserId(), naverUser.getUserPw());

                // LoginFilter로 흐름 넘기기
                request.setAttribute("userId", naverUser.getUserId());
                request.setAttribute("userPw", naverUser.getUserPw());
                request.getRequestDispatcher("/login").forward(request, response);

                return null; // Response는 LoginFilter에서 처리됨
            } else {
                log.info("등록되지 않은 Naver 이메일: {}", naverEmail);
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of(
                                "error", "등록되지 않은 Naver 이메일입니다.",
                                "email", naverEmail
                        ));
            }

        } catch (Exception e) {
            log.error("Naver login process error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Naver 로그인 처리 중 오류 발생"));
        }
    }

    @PostMapping("/link")
    public ResponseEntity<?> linkNaverAccount(@RequestBody Map<String, String> body) {
        String naverCode = body.get("code");
        String uuid = body.get("uuid");
        String state = "random_state_value"; // 클라이언트와 일치하는 상태 값
        log.info("Received Naver authorization code: {}", naverCode);
        log.info("UUID: {}", uuid);

        if (uuid == null || naverCode == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "uuid 또는 code가 누락되었습니다."));
        }

        try {
            // 1. 네이버 토큰 요청
            String naverTokenUrl = "https://nid.naver.com/oauth2.0/token";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> naverParams = new LinkedMultiValueMap<>();
            naverParams.add("grant_type", "authorization_code");
            naverParams.add("client_id", naverClientId);
            naverParams.add("client_secret", naverClientSecret);
            naverParams.add("redirect_uri", naverLinkRedirectUri);
            naverParams.add("code", naverCode);
            naverParams.add("state", state);

            HttpEntity<MultiValueMap<String, String>> naverRequest = new HttpEntity<>(naverParams, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(naverTokenUrl, naverRequest, Map.class);

            Map<String, Object> tokenBody = tokenResponse.getBody();
            if (tokenBody == null || !tokenBody.containsKey("access_token")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Naver Access Token을 추출할 수 없습니다."));
            }

            String accessToken = (String) tokenBody.get("access_token");
            log.info("Naver Access Token: {}", accessToken);

            // 2. 네이버 사용자 정보 요청
            String userInfoUrl = "https://openapi.naver.com/v1/nid/me";
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth(accessToken); // Access Token 설정
            HttpEntity<String> userInfoRequest = new HttpEntity<>(userInfoHeaders);

            ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequest, Map.class);
            Map<String, Object> userInfo = userInfoResponse.getBody();

            if (userInfo == null || !userInfo.containsKey("response")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Naver 사용자 정보를 가져올 수 없습니다."));
            }

            Map<String, Object> responseMap = (Map<String, Object>) userInfo.get("response");
            String naverEmail = (String) responseMap.get("email");
            log.info("Naver Email: {}", naverEmail);

            // 3. DB 업데이트
            Optional<UserEntity> userOptional = userRepository.findById(uuid);
            if (userOptional.isPresent()) {
                UserEntity userEntity = userOptional.get();
                userEntity.setUserNaverEmail(naverEmail); // 네이버 이메일 업데이트
                userRepository.save(userEntity); // 저장
                log.info("User Naver Email linked successfully. UUID: {}", uuid);

                Map<String, String> response = new HashMap<>();
                response.put("success", "true");
                response.put("message", "네이버 이메일이 성공적으로 연동되었습니다.");

                return ResponseEntity.ok(response);

            } else {
                log.error("UUID에 해당하는 사용자가 없습니다. UUID: {}", uuid);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "UUID에 해당하는 사용자가 없습니다."));
            }
        } catch (Exception e) {
            log.error("Naver link process error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Naver 연동 처리 중 오류 발생"));
        }
    }

    @PostMapping("/unlink")
    public ResponseEntity<Map<String, String>> unlinkNaverEmail(@RequestBody Map<String, String> body) {
        String uuid = body.get("uuid");
        log.info("Received UUID for unlinking Naver email: {}", uuid);

        try {
            // uuid로 사용자 찾기
            Optional<UserEntity> userOptional = userRepository.findById(uuid);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "사용자를 찾을 수 없습니다."));
            }

            // 사용자 정보 업데이트
            UserEntity user = userOptional.get();
            user.setUserNaverEmail(null); // Naver 이메일 컬럼을 null로 설정
            userRepository.save(user);

            log.info("Naver Email unlinked successfully for UUID: {}", uuid);

            return ResponseEntity.ok(Map.of("message", "네이버 이메일이 성공적으로 해제되었습니다."));
        } catch (Exception e) {
            log.error("Error unlinking Naver email: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "네이버 이메일 해제 중 오류 발생"));
        }
    }


}
