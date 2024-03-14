package com.app.spotick.domain.dto.place;

import com.app.spotick.domain.dto.place.file.PlaceFileDto;
import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.type.post.PostStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor
public class PlaceEditDto {
    private Long placeId;
    private Long userId;
    @NotBlank(message = "장소이름은 필수 입력사항입니다")
    @Size(max = 250, message = "입력한 글자수가 너무 많습니다")
    private String placeName;

    @NotBlank(message = "장소 소제목은 필수 입력사항입니다")
    @Size(max = 250, message = "입력한 글자수가 너무 많습니다")
    private String placeSubTitle;

    @Size(max = 2000, message = "입력한 글자수가 너무 많습니다")
    @NotBlank(message = "장소 소개는 필수 입력사항입니다")
    private String placeInfo;

    @NotBlank(message = "장소 규칙은 필수 입력사항입니다")
    @Size(max = 2000, message = "입력한 글자수가 너무 많습니다")
    private String placeRule;

    @Min(value = 1,message = "기본 인원은 최소 1명 이상 입력해주세요")
    private Integer placeDefaultPeople;

    @NotBlank(message = "주소는 필수 입력사항입니다")
    private String placeAddress;

    @NotBlank(message = "주소 및 상세주소는 필수 입력사항입니다")
    private String placeAddressDetail;

    @Min(message = "기본요금을 올바르게 입력해주세요",value = 0)
    private Integer placeDefaultPrice;  //기본요금

    @Min(message = "추가요금을 올바르게 입력해주세요",value = 0)
    private Integer placeSurcharge;  //추가요금
    private String bankName;
//    @Pattern(message = "계좌번호를 올바르게 입력해주세요",regexp = "^\\d{10,16}$")
    @Size(max = 250, message = "입력한 글자수가 너무 많습니다")
    private String accountNumber; //계좌번호
    @NotBlank(message = "예금주를 정확히 입력해주세요")
    @Size(max = 250, message = "입력한 글자수가 너무 많습니다")
    private String accountHolder; //예금주
    @NotNull(message = "지도에 장소의 위치를 찍어주세요")
    private Double placeLat;
    @NotNull(message = "지도에 장소의 위치를 찍어주세요")
    private Double placeLng;
    private List<PlaceFileDto> placeSavedFileDtos;

    private List<Long> saveFileIdList = new ArrayList<>();
    private List<MultipartFile> placeNewFiles = new ArrayList<>();

    public Place toEntity(){
        return Place.builder()
                .title(placeName)
                .subTitle(placeSubTitle)
                .info(placeInfo)
                .defaultPeople(placeDefaultPeople)
                .rule(placeRule)
                .placeAddress(new PostAddress(placeAddress,placeAddressDetail))
                .lat(placeLat)
                .lng(placeLng)
                .price(placeDefaultPrice)
                .surcharge(placeSurcharge)
                .bankName(bankName)
                .accountHolder(accountHolder)
                .accountNumber(accountNumber)
                .placeStatus(PostStatus.MODIFICATION_REQUESTED )
                .build();
    }

    public PlaceEditDto(Long placeId, Long userId, String placeName, String placeSubTitle, String placeInfo, String placeRule, Integer placeDefaultPeople, String placeAddress, String placeAddressDetail, Integer placeDefaultPrice, Integer placeSurcharge, String bankName, String accountNumber, String accountHolder, Double placeLat, Double placeLng) {
        this.placeId = placeId;
        this.userId = userId;
        this.placeName = placeName;
        this.placeSubTitle = placeSubTitle;
        this.placeInfo = placeInfo;
        this.placeRule = placeRule;
        this.placeDefaultPeople = placeDefaultPeople;
        this.placeAddress = placeAddress;
        this.placeAddressDetail = placeAddressDetail;
        this.placeDefaultPrice = placeDefaultPrice;
        this.placeSurcharge = placeSurcharge;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.placeLat = placeLat;
        this.placeLng = placeLng;
    }
}
