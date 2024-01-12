package com.app.spotick.service.user;

import com.app.spotick.domain.dto.user.UserJoinDto;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.user.UserStatus;
import com.app.spotick.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(UserJoinDto userJoinDto) {
        userRepository.save(User.builder()
                .email(userJoinDto.getEmail())
                .password(encodePassword(userJoinDto.getPassword()))
                .nickName(userJoinDto.getNickName())
                .tel(userJoinDto.getTel())
                .userStatus(UserStatus.ACTIVATE)
                .build()
        );
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


}









