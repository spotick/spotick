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
    private List<ImageBase> placeFiles;
    private double evalAvg;
    private int evalCount;
    private int bookmarkCount;

    public PlaceListDto(Long id, String title, Integer price, PostAddress placeAddress, List<ImageBase> placeFiles, double evalAvg, int evalCount, int bookmarkCount) {
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
