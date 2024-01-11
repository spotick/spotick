package com.app.spotick.service.user;

import com.app.spotick.domain.dto.user.UserJoinDto;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.entity.user.UserAuthority;
import com.app.spotick.domain.type.user.AuthorityType;
import com.app.spotick.repository.user.UserAuthorityRepository;
import com.app.spotick.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserAuthorityRepository authorityRepository;

    @Override
    public void join(UserJoinDto userJoinDto) {
        User savedUser = userRepository.save(User.builder()
                .email(userJoinDto.getEmail())
                .password(encodePassword(userJoinDto.getPassword()))
                .nickName(userJoinDto.getNickName())
                .tel(userJoinDto.getTel())
                .build()
        );

        authorityRepository.save(UserAuthority.builder()
                .authorityType(AuthorityType.ROLE_USER)
                .user(savedUser)
                .build());
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


}









