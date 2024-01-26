package com.app.spotick.domain.dto.promotion;

import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.promotion.PromotionBoard;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.domain.type.promotion.PromotionCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PromotionRegisterDto {
    private Long promotionId;
    private Long userId;
    @NotBlank(message = "행사제목은 필수 입력사항입니다")
    @Size(max = 250, message = "입력한 글자수가 너무 많습니다")
    private String promotionTitle;

    @NotBlank(message = "행사 부 제목은 필수 입력사항입니다")
    @Size(max = 250, message = "입력한 글자수가 너무 많습니다")
    private String promotionSubTitle;

//    @NotBlank(message = "행사 내용을 입력해주세요")
    private String promotionContent;

    @NotBlank(message = "주소는 필수 입력사항입니다")
    private String placeAddress;

    @NotBlank(message = "주소 및 상세주소는 필수 입력사항입니다")
    private String placeAddressDetail;
    @NotNull(message = "카테고리를 선택해주세요")
    private PromotionCategory promotionCategory;
    @NotNull(message = "지도에 장소의 위치를 찍어주세요")
    private Double placeLat;
    @NotNull(message = "지도에 장소의 위치를 찍어주세요")
    private Double placeLng;
    @Size(min=1, max = 1,message = "대표 사진을 선택해주세요")
    private MultipartFile placeFile;

//    private List<MultipartFile> promotionFiles = new ArrayList<>();



    //    조회수 제외됨
    public PromotionBoard toEntity(){
        return PromotionBoard.builder()
                .id(promotionId)
                .title(promotionTitle)
                .subTitle(promotionSubTitle)
                .content(promotionContent)
                .promotionAddress(new PostAddress(placeAddress,placeAddressDetail))
                .promotionCategory(promotionCategory)
                .lat(placeLat)
                .lng(placeLng)
                .build();
    }
}
