package com.app.spotick.repository.place.inquiry;

import com.app.spotick.domain.dto.place.PlaceInquiryListDto;
import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceFile;
import com.app.spotick.domain.entity.place.PlaceInquiry;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.domain.type.user.UserStatus;
import com.app.spotick.repository.place.PlaceRepository;
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
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
@Commit
class PlaceInquiryRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAuthorityRepository authorityRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private PlaceInquiryRepository placeInquiryRepository;
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

        PlaceInquiry placeInquiry = PlaceInquiry.builder()
                .id(1L)
                .place(placeOf2)
                .user(user1)
                .title("타이틀")
                .content("테스트")
                .build();
        placeInquiryRepository.save(placeInquiry);

        em.flush();
        em.clear();
    }
    
    @Test
    void listTest() {
        Page<PlaceInquiryListDto> inquiriesByUserId = placeInquiryRepository.findInquiriesByUserId(user1.getId(), PageRequest.of(0, 5));

        System.out.println("inquiriesByUserId.getContent() = " + inquiriesByUserId.getContent());
    }
}