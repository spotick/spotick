package com.app.spotick.domain.dto.place.review;

import com.app.spotick.domain.dto.promotion.FileDto;
import com.app.spotick.domain.embedded.post.PostAddress;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlaceSearchListDto {
    private Long id;
    private String title;
    private Integer price;
    private PostAddress placeAddress;
    private FileDto placeFiles;
    private double evalAvg;
    private Long evalCount;
    private Long bookmarkCount;
    private boolean isBookmarkChecked;

    public PlaceSearchListDto(Long id, String title, Integer price, PostAddress placeAddress, FileDto placeFiles, double evalAvg, Long evalCount, Long bookmarkCount, boolean isBookmarkChecked) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.placeAddress = placeAddress;
        this.placeFiles = placeFiles;
        this.evalAvg = evalAvg;
        this.evalCount = evalCount;
        this.bookmarkCount = bookmarkCount;
        this.isBookmarkChecked = isBookmarkChecked;
    }
}
