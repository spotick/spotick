package com.app.spotick.repository.promotion;

import com.app.spotick.domain.dto.promotion.PromotionListDto;
import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.promotion.PromotionBoard;
import com.app.spotick.domain.entity.promotion.PromotionFile;
import com.app.spotick.domain.entity.promotion.QPromotionBoard;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.promotion.PromotionCategory;
import com.app.spotick.domain.type.user.UserStatus;
import com.app.spotick.repository.promotion.file.PromotionFileRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional @Commit
public class PromotionRepositoryTest {
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    PromotionRepository promotionRepository;
    @Autowired
    PromotionFileRepository promotionFileRepository;
    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    EntityManager em;

    PromotionBoard promotion1;
    User user1;
    User user2;

    PromotionFile file;

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
                .promotionCategory(PromotionCategory.CONCERT)
                .user(user1)
                .build();

        promotionRepository.save(promotion1);

        file = PromotionFile.builder()
                .fileName("test")
                .uuid(UUID.randomUUID().toString())
                .uploadPath("test")
                .promotionBoard(promotion1)
                .user(promotion1.getUser())
                .build();

        promotionFileRepository.save(file);

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("상세보기 테스트")
    void detail(){
        // given
        PromotionBoard savedPromotion = promotionRepository.save(promotion1);
        PromotionFile savedFile = promotionFileRepository.save(file);
        // when
        Optional<PromotionBoard> promotionOpt = promotionRepository.findById(savedPromotion.getId());
        Optional<PromotionFile> fileOpt = promotionFileRepository.findById(savedFile.getId());
        // then
        assertThat(promotionOpt.orElse(null))
                .extracting(PromotionBoard::getId)
                .isEqualTo(savedPromotion.getId());

        System.out.println("promotionOpt = " + promotionOpt.orElse(null));
        System.out.println("fileOpt = " + fileOpt.orElse(null));

    }

    @Test
    public void findListWithPage(){
        List<PromotionBoard> boards = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            boards.add(
                    PromotionBoard.builder()
                            .title("test" + i)
                            .subTitle("test" + i)
                            .content("test" + i)
                            .promotionCategory(PromotionCategory.CONCERT)
                            .user(user1)
                            .build()
            );
        }
        promotionRepository.saveAll(boards);
        promotionRepository.flush();
        em.clear();

        Sort sortById = Sort.by(Sort.Direction.DESC,"id");
        PageRequest pageRequest = PageRequest.of(5, 10, sortById);

        Page<PromotionListDto> boardPage = promotionRepository.findListWithPage(pageRequest);

        assertThat(boardPage.getTotalElements()).isEqualTo(51);
        boardPage.getContent().forEach(System.out::println);
    }

}
