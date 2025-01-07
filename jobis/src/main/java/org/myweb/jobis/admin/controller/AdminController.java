package org.myweb.jobis.admin.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myweb.jobis.admin.model.service.AdminService;
import org.myweb.jobis.user.jpa.entity.UserEntity;
import org.myweb.jobis.user.model.dto.User;
import org.myweb.jobis.user.model.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/adminMemberManagementList")
    public Page<UserEntity> getAllUsers(Pageable pageable) {
        log.info("Pageable 정보: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

        Page<UserEntity> users = adminService.getPagedUsers(pageable);

        log.info("총 사용자 수: {}", users.getTotalElements());
        users.getContent().forEach(user ->
                log.info("User: uuid={}, userName={}, userId={}, email={}",
                        user.getUuid(), user.getUserName(), user.getUserId(), user.getUserDefaultEmail())
        );

        return users;
    }

    @GetMapping("/memberDetail") // PathVariable 제거
    public User getUserDetail(@RequestParam String uuid) { // Query Parameter로 전달
        log.info("회원 상세 정보 요청: uuid={}", uuid);
        UserEntity user = adminService.findUserByUuid(uuid);

        if (user == null) {
            log.warn("해당 uuid를 가진 사용자가 없습니다: {}", uuid);
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        return user.toDto();
    }


    @PostMapping("/memberRestrict")
    public ResponseEntity<?> restrictMember(@RequestBody User user) {
        log.info("회원 제제 메서드 시작");
        try {
            log.info("제제 사유 및 사람");
            log.info(user.getUuid());
            log.info(user.getUserRestrictionReason());
            
            adminService.restrictMember(user.getUuid(), user.getUserRestrictionReason());
            return ResponseEntity.ok("회원이 성공적으로 제재되었습니다.");
        } catch (Exception e) {
            e.printStackTrace(); // 예외를 로그로 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("제재 요청에 실패했습니다.");
        }
    }

    @PostMapping("/memberLiftSanction")
    public ResponseEntity<?> liftSanction(@RequestBody Map<String, String> request) {
        String uuid = request.get("uuid");
        if (uuid == null) {
            return ResponseEntity.badRequest().body("유효하지 않은 요청입니다.");
        }

        try {
            adminService.liftMemberSanction(uuid);
            return ResponseEntity.ok("제재 해제가 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("제재 해제 중 오류 발생.");
        }
    }

    @PostMapping("/memberUnsubscribeLift")
    public ResponseEntity<String> liftUnsubscribeStatus(@RequestBody Map<String, String> request) {
        String uuid = request.get("uuid");
        if (uuid == null || uuid.isEmpty()) {
            return ResponseEntity.badRequest().body("UUID가 제공되지 않았습니다.");
        }

        try {
            adminService.liftUnsubscribeStatus(uuid);
            return ResponseEntity.ok("회원의 탈퇴 상태가 해제되었습니다.");
        } catch (Exception e) {
            log.error("탈퇴 상태 해제 중 오류 발생:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원의 탈퇴 상태를 해제할 수 없습니다.");
        }
    }

    @DeleteMapping("/deleteMember")
    public ResponseEntity<String> deleteMember(@RequestBody Map<String, String> request) {
        log.info("회원 삭제 시작");

        String uuid = request.get("uuid");
        log.info("삭제할 UUID: {}", uuid);

        if (uuid == null || uuid.isEmpty()) {
            log.error("UUID가 제공되지 않음");
            return ResponseEntity.badRequest().body("UUID가 제공되지 않았습니다.");
        }

        try {
            adminService.deleteMember(uuid);
            log.info("회원 삭제 완료");
            return ResponseEntity.ok("회원 정보가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("회원 삭제 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원 삭제에 실패했습니다.");
        }
    }


    @PostMapping("/promoteToAdmin")
    public ResponseEntity<?> promoteToAdmin(@RequestBody Map<String, String> payload) {
        String uuid = payload.get("uuid");
        try {
            adminService.promoteToAdmin(uuid); // 유저 서비스에서 관리자로 승격시키는 메소드 호출
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자로 승격 실패: " + e.getMessage());
        }
    }

    @PostMapping("/demoteToUser")
    public ResponseEntity<?> demoteToUser(@RequestBody Map<String, String> payload) {
        String uuid = payload.get("uuid");
        try {
            adminService.demoteToUser(uuid); // 유저 서비스에서 일반 회원으로 강등시키는 메소드 호출
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("일반 회원으로 강등 실패: " + e.getMessage());
        }
    }


    @GetMapping("/salesHistory")
    public Page<Map<String, Object>> getSalesHistory(
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String cancelYN,
            Pageable pageable
    ) {
        log.info("필터: {}, 검색어: {}, 환불 여부: {}", filter, search, cancelYN);

        // AdminService를 통해 필터링된 데이터 반환
        return adminService.getFilteredSalesHistory(filter, search, cancelYN, pageable);
    }








}
    