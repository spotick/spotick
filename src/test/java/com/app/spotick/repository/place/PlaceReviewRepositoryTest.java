package com.app.spotick.repository.place;

import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceReview;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.domain.type.user.UserStatus;
import com.app.spotick.repository.place.Review.PlaceReviewRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional @Commit
class PlaceReviewRepositoryTest {

    @Autowired private PlaceReviewRepository placeReviewRepository;
    @Autowired private PlaceRepository placeRepository;
    @PersistenceContext
    private EntityManager em;

    Place place;
    @BeforeEach
    void setUp() {
        User user = User.builder()
                .email("aaa")
                .password("1234")
                .nickName("홍길동")
                .tel("0101111111")
                .userStatus(UserStatus.ACTIVATE)
                .build();
        em.persist(user);

        place = Place.builder()
                .title("테스트 제목")
                .subTitle("테스트 부제목")
                .price(10000)
                .placeAddress(new PostAddress("서울특별시 강남구 테헤란로 ", "4층"))
                .user(user)
                .placeStatus(PostStatus.APPROVED)
                .lat(0.123)
                .lng(0.453)
                .build();
        place = placeRepository.save(place);
        List<PlaceReview> reviewList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
//            reviewList.add(PlaceReview.builder().build())


        }



    }

}

















