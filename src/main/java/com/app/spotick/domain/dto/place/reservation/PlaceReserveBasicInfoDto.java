package com.app.spotick.domain.dto.place.reservation;


import com.app.spotick.domain.dto.place.file.PlaceFileDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class PlaceReserveBasicInfoDto {
    // todo 장소 예약하기 화면에 뿌려줄 기본정보 세팅하기
    private Long placeId;
    private String placeTitle;
    private String placeSubTitle;
    private Integer placeDefaultPeople;
    private Integer placePrice;
    private Integer placeSurcharge;
    private PlaceFileDto placeFileDto;

    public PlaceReserveBasicInfoDto(Long placeId, String placeTitle, String placeSubTitle, Integer placeDefaultPeople, Integer placePrice, Integer placeSurcharge, PlaceFileDto placeFileDto) {
        this.placeId = placeId;
        this.placeTitle = placeTitle;
        this.placeSubTitle = placeSubTitle;
        this.placeDefaultPeople = placeDefaultPeople;
        this.placePrice = placePrice;
        this.placeSurcharge = placeSurcharge;
        this.placeFileDto = placeFileDto;
    }
}
