package com.app.spotick.domain.type.place;

import lombok.Getter;

@Getter
public enum PlaceReservationStatus {
    PENDING("대기중"),
    WATINGPAYMENT("결제 대기중"),
    APPROVED("승인"),
    REJECTED("거절"),
    CANCELLED("취소")
    ;

    private final String displayName;

    PlaceReservationStatus(String displayName) {
        this.displayName = displayName;
    }
}
