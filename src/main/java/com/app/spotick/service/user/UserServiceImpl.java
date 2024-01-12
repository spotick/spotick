package com.app.spotick.service.user;

import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.domain.dto.user.UserJoinDto;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.entity.user.UserAuthority;
import com.app.spotick.domain.type.user.AuthorityType;
import com.app.spotick.repository.user.UserAuthorityRepository;
import com.app.spotick.repository.user.UserRepository;
import com.app.spotick.security.principal.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = userRepository.findUserByEmail(username);

        if (foundUser == null) {
            throw new UsernameNotFoundException("해당 이메일로 등록된 회원 없음");
        }
        return new PrincipalDetails(new UserDetailsDto(foundUser), authorityRepository.findUserAuthorityByUser(foundUser));
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


}









