package com.app.spotick.domain.entity.place;

import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.place.PlaceReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name = "TBL_PLACE_RESERVATION")
@SequenceGenerator(name = "SEQ_PLACE_RESERVATION_GENERATOR", sequenceName = "SEQ_PLACE_RESERVATION",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceReservation extends Period {
    @Id @GeneratedValue(generator = "SEQ_PLACE_RESERVATION_GENERATOR")
    @Column(name = "PLACE_RESERVATION_ID")
    private Long id;
    private Integer visitors;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    @Column(length = 1000)
    private String content; // 예약 전달 내용
    @Enumerated(EnumType.STRING)
    private PlaceReservationStatus reservationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLACE_ID")
    private Place place;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Builder
    public PlaceReservation(Long id, Integer visitors, LocalDateTime checkIn, LocalDateTime checkOut, String content, PlaceReservationStatus reservationStatus, Place place, User user) {
        this.id = id;
        this.visitors = visitors;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.content = content;
        this.reservationStatus = reservationStatus;
        this.place = place;
        this.user = user;
    }

    public void updateStatus(PlaceReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
}
