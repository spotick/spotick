package com.app.spotick.repository.user;

import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.user.UserStatus;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional @Commit
class UserRepositoryTest {
    @Autowired UserRepository userRepository;

    User user;
    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("aaa")
                .password("1234")
                .nickName("홍길동")
                .tel("0101111111")
                .userStatus(UserStatus.ACTIVATE)
                .build();
    }

    @Test
    @DisplayName("회원가입")
    void joinTest(){
        userRepository.save(user);

        assertThat(user.getId()).isNotNull().isInstanceOf(Long.class);

        System.out.println(user);

    }
}