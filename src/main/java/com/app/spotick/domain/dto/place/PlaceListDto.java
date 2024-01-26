package com.app.spotick.domain.dto.place;

import com.app.spotick.domain.embedded.post.PostAddress;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PlaceListDto {
    private Long id;
    private String title;
    private Integer price;
    private PostAddress placeAddress;
    private List<PlaceFileDto> placeFiles;
    private double evalAvg;
    private Long evalCount;
    private Long bookmarkCount;
    private boolean isBookmarkChecked;

    public PlaceListDto(Long id, String title, Integer price, PostAddress placeAddress, double evalAvg, Long evalCount, Long bookmarkCount, boolean isBookmarkChecked) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.placeAddress = placeAddress;
        this.evalAvg = evalAvg;
        this.evalCount = evalCount;
        this.bookmarkCount = bookmarkCount;
        this.isBookmarkChecked = isBookmarkChecked;
    }

    public void updatePlaceFiles(List<PlaceFileDto> placeFiles) {
        this.placeFiles = placeFiles;
    }

    public void cutPlaceFilesForListPage(){
        placeFiles = placeFiles.stream().limit(5).toList();
    }

}
