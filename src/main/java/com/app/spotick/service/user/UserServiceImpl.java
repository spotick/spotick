package com.app.spotick.service.user;

import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.domain.dto.user.UserJoinDto;
import com.app.spotick.domain.dto.user.UserProfileDto;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.entity.user.UserAuthority;
import com.app.spotick.domain.type.user.AuthorityType;
import com.app.spotick.repository.user.UserAuthorityRepository;
import com.app.spotick.repository.user.UserRepository;
import com.app.spotick.service.redis.RedisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserAuthorityRepository authorityRepository;
    private final UserProfileFileService profileFileService;
    private final RedisService redisService;

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = userRepository.findUserByEmail(username);

        if (foundUser == null) {
            throw new UsernameNotFoundException("해당 이메일로 등록된 회원 없음");
        }
        return new UserDetailsDto(foundUser, authorityRepository.findUserAuthorityByUser(foundUser));
    }

    @Override
    public UserProfileDto getUserProfile(Long userId) {
        return userRepository.findUserProfileById(userId)
                .orElseThrow(() -> new NoSuchElementException("유저 프로필을 찾을 수 없음: " + userId));
    }

    @Override
    public void updateNickName(Long userId, String newNickName) {
        User foundUser = userRepository.findById(userId).orElseThrow(
                NoSuchElementException::new
        );

        foundUser.updateNickName(newNickName);
    }

    // 전화번호 SMS 인증 <- SMS API는 coolsms로 선택함.
    @Override
    public void sendAuthCodeToTel(String tel) {
        String code = createKey();

        System.out.println("tel = " + tel);
        System.out.println("code = " + code);

        DefaultMessageService messageService = NurigoApp.INSTANCE.initialize("NCS25SRQDFKRGDMH", "MKL9HRNYWZ2FPCYDRRPHBAOU9MMYBRHG", "https://api.coolsms.co.kr");

        Message message = new Message();
        message.setFrom("01036316448");
        message.setTo(tel);
        message.setText("인증번호는 " + code + "입니다. 1분 이내로 인증해주세요.");

//        해당부분은 실행되면 돈나가므로 주석처리함. 인증코드 확인은 서버콘솔창 확인하거나 redis콘솔창 확인하여 사용할 것.
//        try {
//            messageService.send(message);
//        } catch (NurigoMessageNotReceivedException exception) {
//            // 발송에 실패한 메시지 목록을 확인할 수 있습니다!
//            System.out.println(exception.getFailedMessageList());
//            System.out.println(exception.getMessage());
//        } catch (Exception exception) {
//            System.out.println(exception.getMessage());
//        }

//        redis 키: 전화번호 / 밸류: 인증코드 / 지속시간 1분 설정
        redisService.setValues(tel, code, Duration.ofMinutes(1));
    }

    @Override
    public void updateTel(Long userId, String newTel) {
        User foundUser = userRepository.findById(userId).orElseThrow(
                NoSuchElementException::new
        );

        foundUser.updateTel(newTel);
    }


    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    //    인증코드 제조기(6자리 숫자)
    public static String createKey() {
        StringBuilder key = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }
}









