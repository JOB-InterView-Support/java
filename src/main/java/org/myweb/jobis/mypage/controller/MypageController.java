package org.myweb.jobis.mypage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.faceid.model.service.FaceIdService;
import org.myweb.jobis.mypage.jpa.entity.SelfIntroduceEntity;
import org.myweb.jobis.mypage.model.dto.SelfIntroduce;
import org.myweb.jobis.mypage.model.service.MypageService;
import org.myweb.jobis.ticket.jpa.entity.TicketEntity;
import org.myweb.jobis.user.jpa.entity.UserEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
@CrossOrigin
public class MypageController {
    private final MypageService mypageService;
    private final PasswordEncoder passwordEncoder;
    private final FaceIdService faceIdService;

    // 회원 정보 불러오기
    @GetMapping("/{userId}")
    public ResponseEntity<UserEntity> getUserInfo(@PathVariable String userId) {
        UserEntity userInfo = mypageService.getUserInfo(userId);
        return ResponseEntity.ok(userInfo);
    }
    // 회원 정보 수정 (JSON 형식)
    @PutMapping("/{userId}")
    public ResponseEntity<UserEntity> updateUser(
            @PathVariable String userId,
            @RequestBody UserEntity updatedUser) {
        log.info("Received update request for userId: {}", userId);
        log.info("Received data: {}", updatedUser);

        if (updatedUser == null) {
            log.error("Request body is missing or invalid.");
            return ResponseEntity.badRequest().build();
        }

        UserEntity currentUser = mypageService.getUserInfo(userId); // 현재 유저 정보 가져오기
        String userPw = updatedUser.getUserPw();

        UserEntity result;
        log.info("입력받은 userPw: {}", userPw);

        // userPw가 null 또는 빈 문자열인지 확인
        if (userPw != null && !userPw.trim().isEmpty()) {
            if (!passwordEncoder.matches(userPw, currentUser.getUserPw())) {
                // userPw가 암호화된 값인지 확인
                if (userPw.startsWith("$2a$")) { // 암호화된 비밀번호로 간주
                    log.info("암호화된 비밀번호가 전달됨. 기존 비밀번호 유지.");
                    result = mypageService.updateUserWithoutPassword(
                            userId,
                            updatedUser.getUserName(),
                            updatedUser.getUserPhone(),
                            updatedUser.getUserDefaultEmail()
                    );
                } else {
                    // 비밀번호가 새로 입력된 경우
                    log.info("비밀번호 포함 회원정보 수정");
                    updatedUser.setUserPw(passwordEncoder.encode(userPw));
                    result = mypageService.updateUser(
                            userId,
                            updatedUser.getUserName(),
                            updatedUser.getUserPw(),
                            updatedUser.getUserPhone(),
                            updatedUser.getUserDefaultEmail()
                    );
                }
            } else {
                log.info("비밀번호가 기존 비밀번호와 동일합니다. 수정되지 않습니다.");
                result = mypageService.updateUserWithoutPassword(
                        userId,
                        updatedUser.getUserName(),
                        updatedUser.getUserPhone(),
                        updatedUser.getUserDefaultEmail()
                );
            }
        } else {
            // 비밀번호가 입력되지 않은 경우
            log.info("비밀번호 미입력, 비밀번호 제외 회원정보 수정");
            result = mypageService.updateUserWithoutPassword(
                    userId,
                    updatedUser.getUserName(),
                    updatedUser.getUserPhone(),
                    updatedUser.getUserDefaultEmail()
            );
        }

        log.info("Updated entity: {}", result);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/secession/{userId}")
    public ResponseEntity<String> updateUserSecessionStatus(
            @PathVariable String userId,
            @RequestBody Map<String, String> requestBody) {
        log.info("Received secession request for userId: {}", userId);

        // 탈퇴 사유 가져오기 (null 가능)
        String userDeletionReason = requestBody.get("userDeletionReason");

        try {
            mypageService.updateUserSecessionStatus(userId, userDeletionReason);
            log.info("회원 탈퇴 처리가 완료되었습니다. userId: {}", userId);
            return ResponseEntity.ok("회원 탈퇴가 성공적으로 처리되었습니다.");
        } catch (RuntimeException e) {
            log.error("회원 탈퇴 처리 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(500).body("회원 탈퇴 처리 중 문제가 발생했습니다.");
        }
    }

    @GetMapping("/intro/{uuid}")
    public ResponseEntity<List<SelfIntroduce>> getIntroList(
            @PathVariable String uuid,
            @RequestParam(required = false, defaultValue = "N") String introIsEdited) {
        List<SelfIntroduce> introList = mypageService.getIntroListFiltered(uuid, introIsEdited)
                .stream()
                .map(SelfIntroduceEntity::toDto) // Entity를 DTO로 변환
                .toList();
        log.info("자기소개서 리스트 반환: {}", introList);
        return ResponseEntity.ok(introList);
    }

    // 작성가능 수 10개 제한 로직
    @GetMapping("/intro/check-limit/{uuid}")
    public ResponseEntity<String> checkIntroductionLimit(@PathVariable String uuid) {
        int maxCount = 10; // 최대 작성 가능 수
        if (mypageService.canCreateMoreIntroductions(uuid, maxCount)) {
            return ResponseEntity.ok("작성 가능");
        } else {
            return ResponseEntity.badRequest().body("자기소개서는 최대 10개까지만 작성할 수 있습니다.");
        }
    }

    @GetMapping("/intro/detail/{introNo}")
    public ResponseEntity<SelfIntroduce> getIntroDetail(@PathVariable String introNo) {
        try {
            // Service 호출
            SelfIntroduce detail = mypageService.getIntroDetailByIntroNo(introNo);
            return ResponseEntity.ok(detail); // 성공적으로 데이터 반환
        } catch (RuntimeException e) {
            log.error("자기소개서 상세 정보를 가져오는 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(404).body(null); // 데이터가 없으면 404 반환
        }
    }

    @PutMapping("/intro/insert")
    public ResponseEntity<String> createIntroduction(@RequestBody SelfIntroduce newIntro) {
        try {
            mypageService.createIntroduction(newIntro);
            log.info("새 자기소개서가 저장되었습니다: {}", newIntro);
            return ResponseEntity.ok("저장 성공");
        } catch (Exception e) {
            log.error("자기소개서 저장 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(500).body("저장 실패");
        }
    }

    @PutMapping("/intro/update/{introNo}")
    public ResponseEntity<String> updateIntro(@PathVariable String introNo, @RequestBody SelfIntroduce updateIntro) {
        try {
            mypageService.updateIntroduction(introNo, updateIntro);
            log.info("자기소개서 업데이트 : {}", updateIntro);
            return ResponseEntity.ok("수정 성공");
        } catch (RuntimeException e){
            log.error("자기소개서 업데이트 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(404).body("수정 실패");
        }
    }

    @PutMapping("/intro/delete/{introNo}")
    public ResponseEntity<String> deleteIntro(@PathVariable String introNo) {
        try{
            mypageService.deleteSelfIntroduction(introNo);
            return ResponseEntity.ok("삭제성공");
        } catch (RuntimeException e) {
            log.error("자기소개서 삭제 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(404).body("삭제 실패");
        }
    }


    @PutMapping("/faceId/{uuid}")
    public ResponseEntity<String> resetFaceIdStatus(@PathVariable String uuid) {
        // 1. USER_FACEID_STATUS를 "N"으로 업데이트
        mypageService.updateFaceIdStatusToN(uuid);

        // 2. FACEID 데이터의 IMAGE_PATH 파일 삭제 및 데이터 삭제
        faceIdService.deleteImageAndDataByUuid(uuid);

        return ResponseEntity.ok("USER_FACEID_STATUS has been reset to 'N' and image file deleted.");
    }

    @GetMapping("/ticket/{uuid}")
    public ResponseEntity<List<TicketEntity>> getTicketList(@PathVariable String uuid) {
        List<TicketEntity> sortedTickets = mypageService.getTicketsByUuidSortedByPurchaseDate(uuid);

        // 빈 리스트일 때도 JSON 형식으로 반환
        if (sortedTickets.isEmpty()) {
            log.info("티켓 데이터가 없습니다. UUID: {}", uuid);
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(sortedTickets);
    }




}
