package com.app.spotick.domain.type.notice;

import lombok.Getter;

@Getter
public enum NoticeStatus {
    UNREAD("읽지 않음"),
    READ("읽음"),
    DELETED("삭제 됨"),
    ;
    private final String displayName;

    NoticeStatus(String displayName) {
        this.displayName = displayName;
    }
}
