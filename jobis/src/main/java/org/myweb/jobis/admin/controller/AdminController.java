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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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



}
