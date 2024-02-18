package com.app.spotick.controller.dummy;


import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceFile;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.repository.place.PlaceRepository;
import com.app.spotick.repository.place.file.PlaceFileRepository;
import com.app.spotick.repository.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
@RequestMapping("/dummy/*")
@RequiredArgsConstructor
public class PlaceDummyController {
    private final PlaceRepository placeRepository;
    private final PlaceFileRepository placeFileRepository;
    private final UserRepository userRepository;

    private Random random = new Random();

    //    장소 더미데이터 생성을위한 클래스
//    @PostConstruct
    public void createPlaceDummy() {
        User user = userRepository.findById(1L).get();
        for (int i = 0; i < 70; i++) {
            List<PlaceFile> fileList = new ArrayList<>();
            Place place = Place.builder()
                    .title("장소 이름" + i)
                    .subTitle("장소 부제목" + i)
                    .info("장소 정보" + i)
                    .defaultPeople(4)
                    .rule("장소 규칙" + i)
                    .placeAddress(new PostAddress("서울특별시 강남구 테헤란로 " + i, "장소 상세주소" + i))
                    .lat(37.5546788388674)
                    .lng(126.970606917394)
                    .price((random.nextInt(10) + 1) * 10000)
                    .surcharge(50000)
                    .bankName("00은행")
                    .accountHolder("예금주" + i)
                    .accountNumber("123456-00-789123")
                    .placeStatus(PostStatus.APPROVED)
                    .user(user)
                    .build();

            placeRepository.save(place);

            for (int j = 0; j < 5; j++) {
                fileList.add(PlaceFile.builder()
                        .uuid(UUID.randomUUID().toString())
                        .fileName(i + "장소사진" + j)
                        .uploadPath(i + "path" + j)
                        .place(place)
                        .build());
            }
            placeFileRepository.saveAll(fileList);
        }


    }


}
