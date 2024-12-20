package org.myweb.jobis.user.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.myweb.jobis.user.jpa.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByUserName(String userName); // 사용자 이름 중복 확인
    boolean existsByUserPhone(String userPhone); // 전화번호 중복 확인
    boolean existsByUserDefaultEmail(String userDefaultEmail); // 이메일 중복 확인

    Optional<UserEntity> findByUserId(String userId); // userId로 사용자 조회

    // RefreshToken 업데이트
    @Modifying
    @Query("UPDATE UserEntity u SET u.userRefreshToken = :refreshToken WHERE u.userId = :userId")
    void updateRefreshToken(@Param("userId") String userId, @Param("refreshToken") String refreshToken);

    // RefreshToken 삭제 (null로 설정)
    @Modifying
    @Query("UPDATE UserEntity u SET u.userRefreshToken = NULL WHERE u.userId = :userId")
    void clearRefreshTokenQuery(@Param("userId") String userId);


    UserEntity findByUuid(String uuid);


}
