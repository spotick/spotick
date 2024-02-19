package com.app.spotick.domain.dto.place;

import com.app.spotick.domain.dto.place.file.PlaceFileDto;
import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.type.place.PlaceReservationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor
public class PlaceReservationListDto {
    private Long id;
    private Long reservationId;
    private String title;
    private Integer price;
    private PostAddress placeAddress;
    private PlaceFileDto placeFileDto;
    private double evalAvg;
    private Long evalCount;
    private Long bookmarkCount;
    private Integer visitors;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String content;
    private PlaceReservationStatus reservationStatus;

    public PlaceReservationListDto(Long id, Long reservationId, String title, Integer price, PostAddress placeAddress, PlaceFileDto placeFileDto, double evalAvg, Long evalCount, Long bookmarkCount, Integer visitors, LocalDateTime checkIn, LocalDateTime checkOut, String content, PlaceReservationStatus reservationStatus) {
        this.id = id;
        this.reservationId = reservationId;
        this.title = title;
        this.price = price;
        this.placeAddress = placeAddress;
        this.placeFileDto = placeFileDto;
        this.evalAvg = evalAvg;
        this.evalCount = evalCount;
        this.bookmarkCount = bookmarkCount;
        this.visitors = visitors;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.content = content;
        this.reservationStatus = reservationStatus;
    }

    public void updateEvalAvg(Double evalAvg) {
        this.evalAvg = evalAvg;
    }
}
