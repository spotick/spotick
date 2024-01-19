package com.app.spotick.domain.dto.place;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class PlaceDetailDto {
    private Long placeId;
    private String placeTitle;
    private String placeSubTitle;
    private String placeInfo;
    private String placeRule;
    private Integer placeDefaultPeople;
    private String  placeAddress;
    private String placeAddressDetail;
    private Integer placePrice;  //기본요금
    private Integer placeSurcharge;  //추가요금
    private Long hostId;
    private double placeLat;
    private double placeLng;

    private Double evalAvg;
    private Long evalCount;
    private Long inquiryCount;
    private boolean isBookmarkChecked;

    public PlaceDetailDto(Long placeId, String placeTitle, String placeSubTitle, String placeInfo,
                          String placeRule, Integer placeDefaultPeople, String placeAddress, String placeAddressDetail,
                          Integer placePrice, Integer placeSurcharge, Long hostId, double placeLat, double placeLng,
                          Double evalAvg, Long evalCount, Long inquiryCount, boolean isBookmarkChecked) {
        this.placeId = placeId;
        this.placeTitle = placeTitle;
        this.placeSubTitle = placeSubTitle;
        this.placeInfo = placeInfo;
        this.placeRule = placeRule;
        this.placeDefaultPeople = placeDefaultPeople;
        this.placeAddress = placeAddress;
        this.placeAddressDetail = placeAddressDetail;
        this.placePrice = placePrice;
        this.placeSurcharge = placeSurcharge;
        this.hostId = hostId;
        this.placeLat = placeLat;
        this.placeLng = placeLng;
        this.evalAvg = evalAvg;
        this.evalCount = evalCount;
        this.inquiryCount = inquiryCount;
        this.isBookmarkChecked = isBookmarkChecked;
    }
}
