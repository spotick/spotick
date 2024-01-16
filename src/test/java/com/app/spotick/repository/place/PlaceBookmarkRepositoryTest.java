package com.app.spotick.repository.place;

import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceBookmark;
import com.app.spotick.domain.entity.place.PlaceFile;
import com.app.spotick.domain.entity.place.QPlace;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.domain.type.user.UserStatus;
import com.app.spotick.repository.place.bookmark.PlaceBookmarkRepository;
import com.app.spotick.repository.user.UserAuthorityRepository;
import com.app.spotick.repository.user.UserRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.app.spotick.domain.entity.place.QPlace.place;
import static com.app.spotick.domain.entity.place.QPlaceBookmark.placeBookmark;
import static com.app.spotick.domain.entity.place.QPlaceFile.placeFile;
import static com.app.spotick.domain.entity.place.QPlaceReview.placeReview;

@SpringBootTest
@Transactional @Commit
class PlaceBookmarkRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAuthorityRepository authorityRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private PlaceBookmarkRepository placeBookmarkRepository;
    @Autowired
    private PlaceFileRepository placeFileRepository;

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private JPAQueryFactory queryFactory;

    User user1;
    User user2;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .id(1L)
                .email("aaa")
                .password("1234")
                .nickName("홍길동")
                .tel("0101111111")
                .userStatus(UserStatus.ACTIVATE)
                .build();
        userRepository.save(user1);

        user2 = User.builder()
                .id(2L)
                .email("bbb")
                .password("1234")
                .nickName("컨텐츠프로바이더")
                .tel("01022222222")
                .userStatus(UserStatus.ACTIVATE)
                .build();
        userRepository.save(user2);

        Place placeOf2 = Place.builder()
                .title("테스트 제목")
                .subTitle("테스트 부제목")
                .price(10000)
                .placeStatus(PostStatus.APPROVED)
                .placeAddress( new PostAddress("서울특별시 강남구 테헤란로 202", "1111"))
                .user(user2)
                .build();
        placeRepository.save(placeOf2);
        List<PlaceFile> placeFileList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            placeFileList.add(PlaceFile.builder()
                    .uuid(UUID.randomUUID().toString())
                    .fileName("사진이름"+i)
                    .uploadPath("사진경로"+i)
                    .place(placeOf2).build());
        }
        placeFileRepository.saveAll(placeFileList);

        PlaceBookmark user1Bookmark = PlaceBookmark.builder()
                .id(1L)
                .user(user1)
                .place(placeOf2)
                .build();
        placeBookmarkRepository.save(user1Bookmark);

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("북마크 리스트 테스트")
    void bookmarkListTest(){
        List<PlaceListDto> bookmarkedPlacesByUserId = placeBookmarkRepository.findBookmarkedPlacesByUserId(user1.getId());

        System.out.println("bookmarkedPlacesByUserId = " + bookmarkedPlacesByUserId);
    }

    @Test
    @DisplayName("북마크 리스트 테스트2")

    void bookmarkListTest2(){
    QPlace subPlace = new QPlace("subPlace");

        JPQLQuery<Double> reviewAvg = JPAExpressions.select(placeReview.score.avg())
                .from(placeReview)
                .where(placeReview.place.eq(subPlace));


        JPQLQuery<Long> reviewCount = JPAExpressions.select(placeReview.count())
                .from(placeReview)
                .where(placeReview.place.eq(subPlace));

        JPQLQuery<Long> bookmarkCount = JPAExpressions.select(placeBookmark.count())
                .from(placeBookmark)
                .where(placeBookmark.place.eq(subPlace));



//        queryFactory.select(
//                        Projections.bean(PlaceListDto.class,
//                                place.id,
//                                place.title,
//                                place.price,
//                                place.placeAddress,
////                                게시글의 사진 리스트를 도대체 어떤 방식으로 가져와야 하는가?
//                                reviewAvg,
//                                reviewCount,
//                                bookmarkCount
//                        )
//                )
//                .from(placeBookmark)
//                .join(placeBookmark.place, place)
//                .leftJoin(placeFile.place, place)
//                .where(placeBookmark.user.id.eq(user2.getId()), place.placeStatus.eq(PostStatus.APPROVED))
//                .fetch();


    }






}










