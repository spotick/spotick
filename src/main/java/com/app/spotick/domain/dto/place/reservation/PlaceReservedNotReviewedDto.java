package com.app.spotick.domain.dto.place.reservation;

import com.app.spotick.domain.dto.place.file.PlaceFileDto;
import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.type.place.PlaceReservationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data @NoArgsConstructor
public class PlaceReservedNotReviewedDto {
    private Long id;
    private Long reservationId;
    private String title;
    private Integer price;
    private PostAddress placeAddress;
    private List<PlaceFileDto> placeFiles;
    private double evalAvg;
    private Long evalCount;
    private Long bookmarkCount;
    private boolean isBookmarkChecked;
    private Integer visitors;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String content;
    private PlaceReservationStatus reservationStatus;

    public PlaceReservedNotReviewedDto(Long id, Long reservationId, String title, Integer price, PostAddress placeAddress, double evalAvg, Long evalCount, Long bookmarkCount, boolean isBookmarkChecked, Integer visitors, LocalDateTime checkIn, LocalDateTime checkOut, String content, PlaceReservationStatus reservationStatus) {
        this.id = id;
        this.reservationId = reservationId;
        this.title = title;
        this.price = price;
        this.placeAddress = placeAddress;
        this.evalAvg = evalAvg;
        this.evalCount = evalCount;
        this.bookmarkCount = bookmarkCount;
        this.isBookmarkChecked = isBookmarkChecked;
        this.visitors = visitors;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.content = content;
        this.reservationStatus = reservationStatus;
    }

    public void updatePlaceFiles(List<PlaceFileDto> placeFiles) {
        this.placeFiles = placeFiles;
    }
}
