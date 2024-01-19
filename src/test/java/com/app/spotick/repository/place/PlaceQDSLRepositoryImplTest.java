package com.app.spotick.repository.place;

import com.app.spotick.domain.dto.place.PlaceFileDto;
import com.app.spotick.domain.dto.place.PlaceListDto;
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
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.app.spotick.domain.entity.place.QPlace.place;
import static com.app.spotick.domain.entity.place.QPlaceBookmark.placeBookmark;
import static com.app.spotick.domain.entity.place.QPlaceFile.placeFile;
import static com.app.spotick.domain.entity.place.QPlaceReview.placeReview;

@Slf4j
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
        Random random = new Random();
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

        for (int i = 0; i < 10000; i++) {
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

            for (int j = 0; j < random.nextInt(15) + 5; j++) {
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
            System.out.println("place.id : " + tuple.get(place.id));
            System.out.println("place.title : " + tuple.get(place.title));
            System.out.println("place.price : " + tuple.get(place.price));
            System.out.println("place.placeAddress : " + tuple.get(place.placeAddress));
            System.out.println("placeFile : " + tuple.get(placeFile));
        });
    }

    @Test
    void findPlaceListPaging2() {
        JPQLQuery<Double> reviewAvg = JPAExpressions.select(placeReview.score.avg())
                .from(placeReview)
                .where(placeReview.place.eq(place));

        JPQLQuery<Long> reviewCount = JPAExpressions.select(placeReview.count())
                .from(placeReview)
                .where(placeReview.place.eq(place));

        JPQLQuery<Long> bookmarkCount = JPAExpressions.select(placeBookmark.count())
                .from(placeBookmark)
                .where(placeBookmark.place.eq(place));

        //        로그인 되어있지 않으면 쿼리 실행 x
        BooleanExpression isBookmarkChecked = JPAExpressions.select(placeBookmark.id.isNotNull())
                .from(placeBookmark)
                .where(placeBookmark.place.eq(place).and(placeBookmark.user.id.eq(user2.getId())))
                .exists();

        List<PlaceListDto> placeListDtos = queryFactory.select(
                        Projections.constructor(PlaceListDto.class,
                                place.id,
                                place.title,
                                place.price,
                                place.placeAddress,
                                reviewAvg,
                                reviewCount,
                                bookmarkCount,
                                isBookmarkChecked
                        )
                )
                .from(place)
                .where(place.placeStatus.eq(PostStatus.APPROVED))
                .orderBy(place.id.desc())
                .offset(0)   //페이지 번호
                .limit(12)  //페이지 사이즈
                .fetch();

//        포스트의 id만 list로 가져온다
        List<Long> placeIdList = placeListDtos.stream().map(PlaceListDto::getId).toList();

//        가져온 id리스트를 in절의 조건으로 사진정보들을 가져온다.
        List<PlaceFileDto> fileDtoList = queryFactory.select(
                        Projections.constructor(PlaceFileDto.class,
                                placeFile.id,
                                placeFile.fileName,
                                placeFile.uuid,
                                placeFile.uploadPath,
                                placeFile.place.id
                        ))
                .from(placeFile)
                .where(placeFile.place.id.in(placeIdList))
                .orderBy(placeFile.id.asc(),placeFile.place.id.desc())
                .fetch();

//        사진정보를 장소 id별로 묶는다
        Map<Long, List<PlaceFileDto>> fileListMap = fileDtoList.stream().collect(Collectors.groupingBy(PlaceFileDto::getPlaceId));

//        장소 id별로 구분된 사진들을 각각 게시글 번호에 맞게 추가한다
        placeListDtos.forEach(place->{
            place.updatePlaceFiles(fileListMap.get(place.getId())
                    .stream().limit(5L).toList());
        });

    }
    
    @Test
    @DisplayName("실행속도 비교 테스트")
    void comparisonTest(){
        //given
        PageRequest pageRequest = PageRequest.of(0,12);

        long startTime2 = System.nanoTime();
        // 테스트 코드 실행
//        placeRepository.findPlaceListPaging2(pageRequest,1L);

        long endTime2 = System.nanoTime();
        long duration2 = (endTime2 - startTime2);  // 실행 시간 (나노초 단위)

        em.flush();
        em.clear();

//        ----------------------------------------------------------------------
        long startTime1 = System.nanoTime();
        // 테스트 코드 실행
        placeRepository.findPlaceListPaging(pageRequest,1L);

        long endTime1 = System.nanoTime();
        long duration1 = (endTime1 - startTime1);  // 실행 시간 (나노초 단위)

        double durationInSeconds1 = (double)duration1 / 1_000_000_000.0; // 초 단위로 변환
        double durationInSeconds2 = (double)duration2 / 1_000_000_000.0; // 초 단위로 변환

        log.info("duration1 : {}", duration1);
        log.info("durationInSeconds1 : {}", durationInSeconds1);
        System.out.println("===============================================");
        log.info("duration2 : {}", duration2);
        log.info("durationInSeconds2 : {}", durationInSeconds2);

    }


}













