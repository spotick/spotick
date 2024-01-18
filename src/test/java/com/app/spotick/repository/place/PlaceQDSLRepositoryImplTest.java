package com.app.spotick.repository.place;

import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceFile;
import com.app.spotick.domain.entity.place.QPlaceFile;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.domain.type.user.UserStatus;
import com.app.spotick.repository.place.bookmark.PlaceBookmarkRepository;
import com.app.spotick.repository.place.file.PlaceFileRepository;
import com.app.spotick.repository.user.UserAuthorityRepository;
import com.app.spotick.repository.user.UserRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
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
@Transactional
@Commit
class PlaceQDSLRepositoryImplTest {
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
    Place placeOf2;

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

        for (int i = 0; i < 50; i++) {
            placeOf2 = Place.builder()
                    .title("테스트 제목" + i)
                    .subTitle("테스트 부제목" + i)
                    .price(10000)
                    .placeAddress(new PostAddress("서울특별시 강남구 테헤란로 " + i, "" + i))
                    .user(user2)
                    .placeStatus(PostStatus.APPROVED)
                    .build();
            placeRepository.save(placeOf2);
            List<PlaceFile> placeFileList = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                placeFileList.add(PlaceFile.builder()
                        .uuid(UUID.randomUUID().toString())
                        .fileName(placeOf2.getId() + "사진이름" + j)
                        .uploadPath(placeOf2.getId() + "사진경로" + j)
                        .place(placeOf2).build());
            }
            placeFileRepository.saveAll(placeFileList);
        }

//        PlaceBookmark user1Bookmark = PlaceBookmark.builder()
//                .user(user2)
//                .place(placeOf2)
//                .build();
//        placeBookmarkRepository.save(user1Bookmark);

        em.flush();
        em.clear();
        System.out.println("===================================================");
    }

    @Test
    void findPlaceListPaging() {
        QPlaceFile subPlaceFile = new QPlaceFile("subPlaceFile");
        JPQLQuery<Double> reviewAvg = JPAExpressions.select(placeReview.score.avg())
                .from(placeReview)
                .where(placeReview.place.eq(place));


        JPQLQuery<Long> reviewCount = JPAExpressions.select(placeReview.count())
                .from(placeReview)
                .where(placeReview.place.eq(place));

        JPQLQuery<Long> bookmarkCount = JPAExpressions.select(placeBookmark.count())
                .from(placeBookmark)
                .where(placeBookmark.place.eq(place));

        JPAExpressions.select(placeFile.id)
                .from(placeFile)
                .where(placeFile.place.eq(place))
                .orderBy(placeFile.id.asc())
                .limit(5);


        List<Tuple> tupleList = queryFactory.select(
                        place.id, place.title, place.price, place.placeAddress, placeFile
                )
                .from(place)
                .leftJoin(placeFile).on(placeFile.place.eq(place).and(placeFile.id.in(
                        JPAExpressions.select(placeFile.id)
                                .from(placeFile)
                                .where(placeFile.place.eq(place))
                                .orderBy(placeFile.id.asc())
                                .limit(5)
                )))
                .where(place.placeStatus.eq(PostStatus.APPROVED))
//                .groupBy(place.id, place.title, place.price, place.placeAddress,placeFile)
                .orderBy(place.id.desc())
                .offset(0)
                .limit(5)
                .fetch();

        tupleList.forEach(tuple -> {
            System.out.println("place.id : "+tuple.get(place.id));
            System.out.println("place.title : "+tuple.get(place.title));
            System.out.println("place.price : "+tuple.get(place.price));
            System.out.println("place.placeAddress : "+tuple.get(place.placeAddress));
            System.out.println("placeFile : "+tuple.get(placeFile));
        });


    }
}













