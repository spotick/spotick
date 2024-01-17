package com.app.spotick.repository.place;

import com.app.spotick.domain.dto.place.PlaceFileDto;
import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.place.*;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.domain.type.user.UserStatus;
import com.app.spotick.repository.place.bookmark.PlaceBookmarkRepository;
import com.app.spotick.repository.place.file.PlaceFileRepository;
import com.app.spotick.repository.user.UserAuthorityRepository;
import com.app.spotick.repository.user.UserRepository;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
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
import java.util.Optional;
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
                    .placeStatus(PostStatus.APPROVED)
                    .placeAddress(new PostAddress("서울특별시 강남구 테헤란로 " + i, "" + i))
                    .user(user2)
                    .placeStatus(PostStatus.APPROVED)
                    .build();
            placeRepository.save(placeOf2);
            List<PlaceFile> placeFileList = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                placeFileList.add(PlaceFile.builder()
                        .uuid(UUID.randomUUID().toString())
                        .fileName(placeOf2.getId()+"사진이름" + j)
                        .uploadPath(placeOf2.getId()+"사진경로" + j)
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
    }

    @Test
    void findPlaceListPaging() {

        JPQLQuery<Double> reviewAvg = JPAExpressions.select(placeReview.score.avg())
                .from(placeReview)
                .where(placeReview.place.eq(place));


        JPQLQuery<Long> reviewCount = JPAExpressions.select(placeReview.count())
                .from(placeReview)
                .where(placeReview.place.eq(place));

        JPQLQuery<Long> bookmarkCount = JPAExpressions.select(placeBookmark.count())
                .from(placeBookmark)
                .where(placeBookmark.place.eq(place));


        List<PlaceListDto> placeListDtos = queryFactory.select(
                        Projections.constructor(PlaceListDto.class,
                                place.id,
                                place.title,
                                place.price,
                                place.placeAddress,
                                reviewAvg,
                                reviewCount,
                                bookmarkCount
                        )
                )
                .from(place)
                .where(place.placeStatus.eq(PostStatus.APPROVED))
                .orderBy(place.id.desc())
                .offset(0)
                .limit(12)
                .fetch();

        placeListDtos.forEach(placeListDto -> {
            placeListDto.updateEvalAvg(Optional.ofNullable(placeListDto.getEvalAvg()).orElse(0.0));

            placeListDto.getPlaceAddress().cutAddress();

            placeListDto.updatePlaceFiles(queryFactory.select(
                            Projections.constructor(PlaceFileDto.class,
                                    placeFile.id,
                                    placeFile.fileName,
                                    placeFile.uuid,
                                    placeFile.uploadPath
                            )
                    )
                    .from(placeFile)
                    .where(placeFile.place.id.eq(placeListDto.getId()))
                    .fetch());
        });


        placeListDtos.forEach(System.out::println);



    }
}













