package com.app.spotick.domain.dto.place;

import com.app.spotick.domain.entity.place.Place;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PlaceDetailDto {
    private Long placeId;
    private String placeTitle;
    private String placeSubTitle;
    private String placeInfo;
    private String placeRule;
    private Integer placeDefaultPeople;
    private String placeAddress;
    private String placeAddressDetail;
    private Integer placePrice;  //기본요금
    private Integer placeSurcharge;  //추가요금
    private Long hostId;
    private double placeLat;
    private double placeLng;

    private double evalAvg;
    private long evalCount;
    private long eval5ScoreCount;
    private long inquiryCount;
    private boolean isBookmarkChecked;

    private List<PlaceFileDto> placeFileList;

    public PlaceDetailDto(Long placeId, String placeTitle, String placeSubTitle, String placeInfo, String placeRule, Integer placeDefaultPeople, String placeAddress, String placeAddressDetail, Integer placePrice, Integer placeSurcharge, Long hostId, double placeLat, double placeLng, double evalAvg, long evalCount,long eval5ScoreCount, long inquiryCount, boolean isBookmarkChecked, List<PlaceFileDto> placeFileList) {
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
        this.eval5ScoreCount = eval5ScoreCount;
    }

    public static PlaceDetailDto from(Place place) {
        PlaceDetailDto placeDetailDto = new PlaceDetailDto();
        placeDetailDto.setPlaceId(place.getId());
        placeDetailDto.setPlaceTitle(place.getTitle());
        placeDetailDto.setPlaceSubTitle(place.getSubTitle());
        placeDetailDto.setPlaceInfo(place.getInfo());
        placeDetailDto.setPlaceRule(place.getRule());
        placeDetailDto.setPlaceDefaultPeople(place.getDefaultPeople());
        placeDetailDto.setPlaceAddress(place.getPlaceAddress().getAddress());
        placeDetailDto.setPlaceAddressDetail(place.getPlaceAddress().getAddressDetail());
        placeDetailDto.setPlacePrice(place.getPrice());
        placeDetailDto.setPlaceSurcharge(place.getSurcharge());
        placeDetailDto.setHostId(place.getUser().getId());
        placeDetailDto.setPlaceLat(place.getLat());
        placeDetailDto.setPlaceLng(place.getLng());
        return placeDetailDto;
    }


}
