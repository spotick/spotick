package com.app.spotick.util.type;

import lombok.Getter;

@Getter
public enum PlaceManagerSortType {
    RESERVATION("예약 요청 순"),
    INQUIRY("문의 순"),
    POPULARITY("인기순"),
    NEWEST("최신순"),
    INTEREST("관심순"),
    PRICE_LOW_TO_HIGH("낮은 가격순"),
    PRICE_HIGH_TO_LOW("높은 가격순"),
    VIEWS("조회순"),
    REVIEWS("후기순"),
    RATING_HIGH("평점 높은 순");

    private final String displayName;

    PlaceManagerSortType(String displayName) {
        this.displayName = displayName;
    }
}
