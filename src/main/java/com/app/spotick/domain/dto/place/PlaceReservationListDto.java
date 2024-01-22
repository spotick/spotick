package com.app.spotick.domain.dto.place;

import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.place.PlaceFile;
import com.app.spotick.domain.type.place.PlaceReservationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data @NoArgsConstructor
public class PlaceReservationListDto {
    private Long id;
    private String title;
    private Integer price;
    private PostAddress placeAddress;
    private PlaceFileDto placeFileDto;
    private Double evalAvg;
    private Long evalCount;
    private Long bookmarkCount;
    private Integer visitors;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String content;
    private PlaceReservationStatus reservationStatus;

    public PlaceReservationListDto(Long id, String title, Integer price, PostAddress placeAddress, PlaceFileDto placeFileDto, Double evalAvg, Long evalCount, Long bookmarkCount, Integer visitors, LocalDateTime checkIn, LocalDateTime checkOut, String content, PlaceReservationStatus reservationStatus) {
        this.id = id;
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
