package com.app.spotick.domain.dto.place;

import com.app.spotick.domain.base.image.ImageBase;
import com.app.spotick.domain.embedded.post.PostAddress;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor
public class PlaceListDto {
    private Long id;
    private String title;
    private Integer price;
    private PostAddress placeAddress;
    private List<PlaceFileDto> placeFiles;
    private Double evalAvg;
    private Long evalCount;
    private Long bookmarkCount;


    public PlaceListDto(Long id, String title, Integer price, PostAddress placeAddress, List<PlaceFileDto> placeFiles, Double evalAvg, Long evalCount, Long bookmarkCount) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.placeAddress = placeAddress;
        this.placeFiles = placeFiles;
        this.evalAvg = evalAvg;
        this.evalCount = evalCount;
        this.bookmarkCount = bookmarkCount;
    }
}
