package com.app.spotick.service.user;

import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.domain.dto.user.UserJoinDto;
import com.app.spotick.domain.dto.user.UserProfileDto;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.entity.user.UserAuthority;
import com.app.spotick.domain.entity.user.UserProfileFile;
import com.app.spotick.domain.type.user.AuthorityType;
import com.app.spotick.repository.user.UserAuthorityRepository;
import com.app.spotick.repository.user.UserProfileFileRepository;
import com.app.spotick.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserAuthorityRepository authorityRepository;
    private final UserProfileFileService profileFileService;

    @Override
    public void join(UserJoinDto userJoinDto) {
        userJoinDto.setPassword(encodePassword(userJoinDto.getPassword()));

        User savedUser = userRepository.save(userJoinDto.toEntity());
//      권한 추가
        authorityRepository.save(UserAuthority.builder()
                .authorityType(AuthorityType.ROLE_USER)
                .user(savedUser)
                .build());
//        랜덤 사진 추가
        profileFileService.saveDefaultRandomImgByUser(savedUser);
    }

    @Override
    public Optional<UserProfileDto> getUserProfile(Long userId) {
        return userRepository.findUserProfileById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = userRepository.findUserByEmail(username);

        if (foundUser == null) {
            throw new UsernameNotFoundException("해당 이메일로 등록된 회원 없음");
        }
        return new UserDetailsDto(foundUser,authorityRepository.findUserAuthorityByUser(foundUser)) ;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


}









