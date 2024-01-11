package com.app.spotick.domain.type.payment;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING("대기중"),
    APPROVED("승인됨"),
    COMPLETED("완료됨"),
    DECLINED("거부됨"),
    CANCELLED("취소됨"),
    REFUNDED("환불됨"),
    FAILED("실패함");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }
}
