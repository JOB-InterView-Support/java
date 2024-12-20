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





}
    