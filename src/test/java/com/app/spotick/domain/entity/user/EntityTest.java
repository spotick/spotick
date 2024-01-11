package com.app.spotick.domain.entity.user;

import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.promotion.PromotionBoard;
import com.app.spotick.domain.type.user.UserStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional @Commit
class EntityTest {
    @PersistenceContext
    EntityManager em;


    private UserProfileFile userProfileFile;
    private PromotionBoard promotionBoard;
    private User user;

    @BeforeEach
    void setUp(){
        user = User.builder()
                .email("aaa@naver.com")
                .password("1234")
                .nickName("홍길동")
                .tel("01012341234")
                .userStatus(UserStatus.ACTIVATE)
                .build();
        em.persist(user);
    }

    @Test
    void userProfileFileTest(){
        userProfileFile = UserProfileFile.builder()
                .uuid(UUID.randomUUID().toString())
                .fileName("testFileName")
                .uploadPath("testPah")
                .user(User.builder()
                        .email("aaa@naver.com")
                        .password("1234")
                        .nickName("홍길동")
                        .build())
                .build();
        System.out.println(userProfileFile);

    }

    @Test
    void promotionBoardTest(){
        promotionBoard = PromotionBoard.builder()
                .user(user)
                .title("제목")
                .content("test내용")
                .lat(0.123)
                .lng(0.123)
                .promotionAddress(new PostAddress("서울시","강남구"))
                .startDate(LocalDate.of(2023,12,3))
                .endDate(LocalDate.of(2024,12,3))
                .build();

        em.persist(promotionBoard);
        em.flush();
        em.clear();

        promotionBoard = em.find(PromotionBoard.class,promotionBoard.getId());

        assertThat(promotionBoard.getId()).isNotNull();
        assertThat(promotionBoard.getViewCount()).isEqualTo(0);
    }





}