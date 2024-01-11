package com.app.spotick.domain.type.user;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVATE("활성"),
    SUSPENDED_7_DAYS("7일 정지"),
    SUSPENDED_30_DAYS("30일 정지"),
    PERMANENTLY_SUSPENDED("영구 정지"),
    DEACTIVATE("탈퇴")
    ;

    private final String displayName;

    UserStatus(String displayName) {
        this.displayName = displayName;
    }

}
