package org.myweb.jobis.user.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.myweb.jobis.user.jpa.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean isUsernameDuplicate(String username) {
        return userRepository.existsByUserName(username);
    }
}
