package com.app.spotick.domain.dto.place.reservation;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor
public class ReservationRequestListDto {
    private Long id;
    private Integer visitors;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Integer amount;
    private String content;
    private String nickName;
    private String fileName;
    private String uuid;
    private String uploadPath;
    private boolean isDefaultImage;

    public ReservationRequestListDto(Long id, Integer visitors, LocalDateTime checkIn, LocalDateTime checkOut, Integer amount, String content, String nickName, String fileName, String uuid, String uploadPath, boolean isDefaultImage) {
        this.id = id;
        this.visitors = visitors;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.amount = amount;
        this.content = content;
        this.nickName = nickName;
        this.fileName = fileName;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
        this.isDefaultImage = isDefaultImage;
    }
}
