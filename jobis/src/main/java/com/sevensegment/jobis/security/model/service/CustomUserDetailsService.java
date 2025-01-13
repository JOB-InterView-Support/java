package com.sevensegment.jobis.security.model.service;

import com.sevensegment.jobis.user.jpa.entity.UserEntity;
import com.sevensegment.jobis.user.jpa.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> {
                    System.out.println("사용자를 찾을 수 없습니다: " + username);
                    return new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
                });

        System.out.println("DB에서 조회된 사용자: " + userEntity);

        return User.builder()
                .username(userEntity.getUserId())
                .password(userEntity.getUserPw())
                .roles(userEntity.getAdminYn().equals("Y") ? "ADMIN" : "USER")
                .build();
    }

}
