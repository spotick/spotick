package com.app.spotick.domain.dto.place;

import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.type.post.PostStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor
public class PlaceManageListDto {
    private Long id;
    private String title;
    private Integer price;
    private PostAddress placeAddress;
    private List<PlaceFileDto> placeFiles;
    private double evalAvg;
    private Long evalCount;
    private Long bookmarkCount;
    private PostStatus postStatus;
    private Long reservationRequestCount;
    private Long inquiriesCount;

    public PlaceManageListDto(Long id, String title, Integer price, PostAddress placeAddress, double evalAvg, Long evalCount, Long bookmarkCount, PostStatus postStatus, Long reservationRequestCount, Long inquiriesCount) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.placeAddress = placeAddress;
        this.evalAvg = evalAvg;
        this.evalCount = evalCount;
        this.bookmarkCount = bookmarkCount;
        this.postStatus = postStatus;
        this.reservationRequestCount = reservationRequestCount;
        this.inquiriesCount = inquiriesCount;
    }

    public void updatePlaceFiles(List<PlaceFileDto> placeFiles) {
        this.placeFiles = placeFiles;
    }
}
