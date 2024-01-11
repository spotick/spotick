package com.app.spotick.domain.type.payment;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CREDIT_CARD("신용카드"),
    DEBIT_CARD("체크카드"),
    BANK_TRANSFER("은행 송금"),
    MOBILE_PAYMENT("모바일 결제"),
    PAYPAL("페이팔"),
    CASH("현금"),
    BANK_DEPOSIT("무통장 입금")
    ;
    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }
}
