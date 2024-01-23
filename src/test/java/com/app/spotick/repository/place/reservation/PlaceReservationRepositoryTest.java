package com.app.spotick.repository.place.reservation;

import com.app.spotick.domain.dto.place.PlaceReservationListDto;
import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceFile;
import com.app.spotick.domain.entity.place.PlaceReservation;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.place.PlaceReservationStatus;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.domain.type.user.UserStatus;
import com.app.spotick.repository.place.PlaceRepository;
import com.app.spotick.repository.place.bookmark.PlaceBookmarkRepository;
import com.app.spotick.repository.place.file.PlaceFileRepository;
import com.app.spotick.repository.user.UserAuthorityRepository;
import com.app.spotick.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional @Commit
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
                .id(1L)
                .visitors(5)
                .checkIn(LocalDateTime.of(2024, 1, 5, 19, 0, 0))
                .checkOut(LocalDateTime.of(2024, 1, 6, 7, 0,0))
                .content("테스트")
                .reservationStatus(PlaceReservationStatus.WATINGPAYMENT)
                .place(placeOf2)
                .user(user1)
                .build();
        placeReservationRepository.save(placeReservation);

        em.flush();
        em.clear();
    }

    @Test
    void reservationListTest() {
        Page<PlaceReservationListDto> reservationsByUserId = placeReservationRepository.findReservationsByUserId(user1.getId(), PageRequest.of(0, 3));

        System.out.println("reservationsByUserId = " + reservationsByUserId);
        List<PlaceReservationListDto> content = reservationsByUserId.getContent();
        content.forEach(System.out::println);
    }
}