package com.app.spotick.repository.promotion;

import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.promotion.PromotionBoard;
import com.app.spotick.domain.entity.promotion.QPromotionBoard;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.user.UserStatus;
import com.app.spotick.repository.user.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional @Commit
public class PromotionRepositoryTest {
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    PromotionRepository promotionRepository;
    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    EntityManager em;

    PromotionBoard promotion1;
    User user1;
    User user2;

    @BeforeEach
    void setUP(){
        user1 = User.builder()
                .id(1L)
                .email("test@naver.com")
                .password("1234")
                .nickName("홍길동")
                .tel("01012341234")
                .userStatus(UserStatus.ACTIVATE)
                .build();

        userRepository.save(user1);

        user2 = User.builder()
                .id(2L)
                .email("abc")
                .password("1234")
                .nickName("이순신")
                .tel("01043214321")
                .userStatus(UserStatus.ACTIVATE)
                .build();

        userRepository.save(user2);

        promotion1 = PromotionBoard.builder()
                .id(1L)
                .title("테스트행사")
                .subTitle("테스트입니다")
                .content("테스트입니다")
                .promotionAddress(new PostAddress("강남구","강남역"))
                .startDate(LocalDate.of(2024,01,01))
                .endDate(LocalDate.of(2024,02,01))
                .user(user1)
                .build();

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("상세보기 테스트")
    void detail(){
        // given
        PromotionBoard savedPromotion = promotionRepository.save(promotion1);
        // when
        Optional<PromotionBoard> promotionOpt = promotionRepository.findById(savedPromotion.getId());
        // then
        assertThat(promotionOpt.orElse(null))
                .extracting(PromotionBoard::getId)
                .isEqualTo(savedPromotion.getId());

        System.out.println("promotionOpt = " + promotionOpt.orElse(null));

    }

}
