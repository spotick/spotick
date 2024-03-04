package com.app.spotick.domain.type.ticket;

import lombok.Getter;

@Getter
public enum TicketRatingType {
    ALL("전체관람가"),
    ABOVE12("12세 이상 관람가"),
    ABOVE15("15세 이상 관람가"),
    ABOVE18("청소년 관람불가")
    ;


    private final String displayName;

    TicketRatingType(String displayName) {
        this.displayName = displayName;
    }
}
