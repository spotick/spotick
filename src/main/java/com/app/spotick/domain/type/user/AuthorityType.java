package com.app.spotick.domain.type.user;

import lombok.Getter;

@Getter
public enum AuthorityType {
    ROLE_USER("일반회원"),
    ROLE_ADMIN("관리자"),
    ;

    private final String displayName;

    AuthorityType(String displayName) {
        this.displayName = displayName;
    }
}
