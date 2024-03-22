package com.app.spotick.domain.type.user;

import com.app.spotick.domain.base.type.Displayable;
import lombok.Getter;

@Getter
public enum UserStatus implements Displayable {
    ACTIVATE("활성"),
    SUSPENDED_7_DAYS("7일 정지"),
    SUSPENDED_30_DAYS("30일 정지"),
    PERMANENTLY_SUSPENDED("영구 정지"),
    DEACTIVATE("탈퇴");

    private final String displayName;

    UserStatus(String displayName) {
        this.displayName = displayName;
    }

    public boolean isTemporarilySuspended(){
        return this == SUSPENDED_7_DAYS || this == SUSPENDED_30_DAYS;
    }


}
