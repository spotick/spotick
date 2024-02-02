package com.app.spotick.repository.place.Review;

import com.app.spotick.domain.dto.place.review.PlaceReviewListDto;
import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceReservation;
import com.app.spotick.domain.entity.place.PlaceReview;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.place.PlaceReservationStatus;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.repository.place.PlaceRepository;
import com.app.spotick.repository.place.reservation.PlaceReservationRepository;
import com.app.spotick.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@SpringBootTest
@Transactional
@Commit
class PlaceReviewRepositoryTest {
    private Random random = new Random();

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PlaceReviewRepository placeReviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private PlaceReservationRepository placeReservationRepository;

    User user;
    PlaceReview review;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .build();
        userRepository.save(user);

        review = PlaceReview.builder()
                .score(4)
                .content("내용")
                .user(user)
                .build();
        placeReviewRepository.save(review);

        Place place = Place.builder()
                .title("테스트 제목")
                .subTitle("테스트 부제목")
                .price(10000)
                .placeStatus(PostStatus.APPROVED)
                .placeAddress(new PostAddress("서울특별시 강남구 테헤란로 202", "1111"))
                .user(user)
                .build();
        placeRepository.save(place);

        PlaceReservation placeReservation = PlaceReservation.builder()
                .visitors(5)
                .checkIn(LocalDateTime.of(2024, 1, 5, 19, 0, 0))
                .checkOut(LocalDateTime.of(2024, 1, 6, 7, 0, 0))
                .content("테스트")
                .notReviewing(true)
                .reservationStatus(PlaceReservationStatus.WAITING_PAYMENT)
                .place(place)
                .user(user)
                .build();
        placeReservationRepository.save(placeReservation);

        List<PlaceReservation> reservations = new ArrayList<>();
        for (int i = 0; i < 6 * 3; i += 2) {
            reservations.add(PlaceReservation.builder()
                    .visitors(i + 1)
                    .checkIn(LocalDateTime.of(2024, 2, 1, i, 0, 0))
                    .checkOut(LocalDateTime.of(2024, 2, 1, i + 2, 0, 0))
                    .content("테스트")
                    .notReviewing(true)
                    .reservationStatus(PlaceReservationStatus.COMPLETED)
                    .place(place)
                    .user(user)
                    .build());
        }
        placeReservationRepository.saveAll(reservations);

        List<PlaceReview> reviewList = new ArrayList<>();
        reservations.forEach(reserve -> {
            reviewList.add(PlaceReview.builder()
                    .content("잘 놀다감" + reserve.getId())
                    .score(random.nextInt(5) + 1)
                    .user(user)
                    .placeReservation(reserve)
                    .build());
        });

        placeReviewRepository.saveAll(reviewList);

        em.flush();
        em.clear();
    }

    @Test
    void findByIdAndUser() {
        PlaceReview placeReview = placeReviewRepository.findByIdAndUser(review.getId(), user).orElseThrow(
                NoSuchElementException::new
        );

        System.out.println("placeReview = " + placeReview);
    }

    @Test
    @DisplayName("장소 상세보기 페이지 리뷰리스트 조회")
    void findReviewListByPlaceIdTest(){
        Long placeId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 5);

        Slice<PlaceReviewListDto> reviewDtoList = placeReviewRepository.findReviewSliceByPlaceId(placeId, pageRequest);

        reviewDtoList.getContent().forEach(System.out::println);
        System.out.println("reviewDtoList.hasNext() = " + reviewDtoList.hasNext());

        PageRequest pageRequest2 = PageRequest.of(1, 5);

        Slice<PlaceReviewListDto> reviewDtoList2 = placeReviewRepository.findReviewSliceByPlaceId(placeId, pageRequest2);

        reviewDtoList2.getContent().forEach(System.out::println);
        System.out.println("reviewDtoList2.hasNext() = " + reviewDtoList2.hasNext());
        
    }






}


















