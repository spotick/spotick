package com.app.spotick.domain.type.place;

import lombok.Getter;

@Getter
public enum PlaceReservationStatus {
    PENDING("승인 대기중"),
    WAITINGPAYMENT("결제 대기 중"),
    APPROVED("승인됨"),
    REJECTED("거절됨"),
    CANCELLED("취소")
    ;

    private final String displayName;

    PlaceReservationStatus(String displayName) {
        this.displayName = displayName;
    }
}
