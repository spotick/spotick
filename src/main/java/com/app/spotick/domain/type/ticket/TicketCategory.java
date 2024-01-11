package com.app.spotick.domain.type.ticket;

import lombok.Getter;

@Getter
public enum TicketCategory {
    MUSICAL("뮤지컬"),
    CONCERT("콘서트"),
    CLASSIC_DANCE("클래식/무용"),
    SPORTS("스포츠"),
    EXHIBITION_EVENT("전시/행사"),
    ETC("기타");

    private final String displayName;

    TicketCategory(String displayName) {
        this.displayName = displayName;
    }
}
