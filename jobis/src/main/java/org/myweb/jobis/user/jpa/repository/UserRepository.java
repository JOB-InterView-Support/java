package org.myweb.jobis.user.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.myweb.jobis.user.jpa.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByUserName(String userName); // 엔티티의 필드명과 일치

    // 전화번호 중복 여부 확인 메서드
    boolean existsByUserPhone(String userPhone);

    // 이메일 중복 여부 확인 메서드
    boolean existsByUserDefaultEmail(String userDefaultEmail);

    // 로그인
    Optional<UserEntity> findByUserId(String userId); // userId로 사용자 찾기
}

