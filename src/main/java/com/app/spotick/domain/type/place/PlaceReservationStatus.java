package com.app.spotick.domain.type.place;

import lombok.Getter;

@Getter
public enum PlaceReservationStatus {
    PENDING("승인 대기중"),
    WAITING_PAYMENT("결제 대기 중"),
    APPROVED("승인됨"),
    REJECTED("거절됨"),
    CANCELLED("취소"),
    COMPLETED("이용 완료")
    ;

    private final String displayName;

    PlaceReservationStatus(String displayName) {
        this.displayName = displayName;
    }
}
