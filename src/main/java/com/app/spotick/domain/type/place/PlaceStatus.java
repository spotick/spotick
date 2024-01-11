package com.app.spotick.domain.type.place;

import lombok.Getter;

@Getter
public enum PlaceStatus {
    REGISTRATION_PENDING("등록 대기중"), // 등록 대기중
    APPROVED("승인"),             // 승인
    REJECTED("거절"),             // 거절
    REPLACED("대체됨"),             // 대체됨
    MODIFICATION_PENDING("수정 대기중")  // 수정 대기중
    ;

    private final String displayName;
    PlaceStatus(String displayName) {
        this.displayName = displayName;
    }


}
