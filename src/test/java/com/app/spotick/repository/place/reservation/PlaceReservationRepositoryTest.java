package com.app.spotick.repository.place.reservation;

import com.app.spotick.domain.dto.place.reservation.PlaceReservationTimeDto;
import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceFile;
import com.app.spotick.domain.entity.place.PlaceReservation;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.place.PlaceReservationStatus;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.domain.type.user.UserStatus;
import com.app.spotick.repository.place.PlaceRepository;
import com.app.spotick.repository.place.file.PlaceFileRepository;
import com.app.spotick.repository.user.UserAuthorityRepository;
import com.app.spotick.repository.user.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Commit
class PlaceReservationRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAuthorityRepository authorityRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private PlaceReservationRepository placeReservationRepository;
    @Autowired
    private PlaceFileRepository placeFileRepository;
    @PersistenceContext
    private EntityManager em;
    @Autowired
    JPAQueryFactory queryFactory;

    User user1;
    User user2;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .email("aaa")
                .password("1234")
                .nickName("홍길동")
                .tel("0101111111")
                .userStatus(UserStatus.ACTIVATE)
                .build();
        userRepository.save(user1);

        user2 = User.builder()
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
                .placeAddress(new PostAddress("서울특별시 강남구 테헤란로 202", "1111"))
                .user(user2)
                .build();
        placeRepository.save(placeOf2);

        List<PlaceFile> placeFileList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            placeFileList.add(PlaceFile.builder()
                    .uuid(UUID.randomUUID().toString())
                    .fileName("사진이름" + i)
                    .uploadPath("사진경로" + i)
                    .place(placeOf2).build());
        }
        placeFileRepository.saveAll(placeFileList);

        PlaceReservation placeReservation = PlaceReservation.builder()
                .visitors(5)
                .checkIn(LocalDateTime.of(2024, 1, 5, 19, 0, 0))
                .checkOut(LocalDateTime.of(2024, 1, 6, 7, 0, 0))
                .content("테스트")
                .notReviewing(true)
                .reservationStatus(PlaceReservationStatus.WAITING_PAYMENT)
                .place(placeOf2)
                .user(user1)
                .build();
        placeReservationRepository.save(placeReservation);

        List<PlaceReservation> reservations = new ArrayList<>();
        for (int i = 0; i < 6*3; i+=2) {
            reservations.add(PlaceReservation.builder()
                    .visitors(i+1)
                    .checkIn(LocalDateTime.of(2024, 2, 1, i, 0, 0))
                    .checkOut(LocalDateTime.of(2024, 2, 1, i+2, 0, 0))
                    .content("테스트")
                    .notReviewable(true)
                    .reservationStatus(PlaceReservationStatus.PENDING)
                    .place(placeOf2)
                    .user(user1)
                    .build());
        }
        placeReservationRepository.saveAll(reservations);
        em.flush();
        em.clear();
    }

    @DisplayName("중복예약시간 테스트 예약 가능 케이스")
    @ParameterizedTest
    @CsvSource({
            "5, 12, 5, 19", "5, 10, 5, 18",
            "6, 7, 6, 10", "6, 10, 6, 17"
    })
    void isOverlappingReservationTestCase1(int checkInDate, int checkInTime, int checkOutDate, int checkOutTime) {
        //given
        Long placeId = 1L;
        LocalDateTime checkIn = LocalDateTime.of(2024, 1, checkInDate, checkInTime, 0, 0);
        LocalDateTime checkOut = LocalDateTime.of(2024, 1, checkOutDate, checkOutTime, 0, 0);

        //when
        boolean isAvailable = placeReservationRepository.isOverlappingReservation(placeId, checkIn, checkOut);

        //then
        assertThat(isAvailable).isEqualTo(false);
    }

//               .checkIn(LocalDateTime.of(2024, 1, 5, 19, 0, 0))
//            .checkOut(LocalDateTime.of(2024, 1, 6, 7, 0, 0))
    @DisplayName("중복예약시간 테스트 예약 불가 케이스")
    @ParameterizedTest
    @CsvSource({
            "5, 18, 5, 20", // 사용자 체크아웃이 기존 체크인 후 && 사용자 체크인이 기존 체크인 전
            "5, 18, 6, 6",  // 사용자 체크인이 기존 체크인 후 && 사용자 체크아웃이 기존 체크아웃 전
            "5, 18, 6, 8",  // 사용자 체크인과 체크아웃이 기존 예약을 포함하는 경우
            "6, 6, 6, 7"    // 사용자 체크인이 기존 체크아웃 전 && 사용자 체크아웃이 기존 체크아웃 후
    })
    void isOverlappingReservationTestCase2(int checkInDate, int checkInTime, int checkOutDate, int checkOutTime) {
        //given
        Long placeId = 1L;
        LocalDateTime checkIn = LocalDateTime.of(2024, 1, checkInDate, checkInTime, 0, 0);
        LocalDateTime checkOut = LocalDateTime.of(2024, 1, checkOutDate, checkOutTime, 0, 0);

        //when
        boolean isAvailable = placeReservationRepository.isOverlappingReservation(placeId, checkIn, checkOut);

        //then
        assertThat(isAvailable).isEqualTo(true);
    }

    @Test
    @DisplayName("예약된 시간 리스트 조회")
    void findReservedTimeList(){
        Long placeId = 1L;
        LocalDateTime startTime = LocalDateTime.of(2024, 2, 1, 0, 0, 0);
        LocalDateTime endTime = startTime.plusDays(2);

        List<PlaceReservationTimeDto> reservedTimes = placeReservationRepository.findReservedTimes(placeId, startTime, endTime);

        reservedTimes.forEach(System.out::println);

    }




}
















