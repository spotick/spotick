package com.app.spotick.domain.type.place;

import lombok.Getter;

@Getter
public enum PlaceModifyStatus {
    PENDING("수정 대기중"), // 등록 대기중
    APPROVED("승인"),             // 승인
    REJECTED("거절"),             // 거절
    ;

    private final String displayName;

    PlaceModifyStatus(String displayName) {
        this.displayName = displayName;
    }
}
