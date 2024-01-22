package com.app.spotick.domain.type.promotion;

import lombok.Getter;

@Getter
public enum PromotionCategory {
    EDUCATION("강연/교육/세미나"),
    WORKSHOP("컨퍼런스/워크숍"),
    TEST_PBT("시험_P.B.T"),
    MOVIE_FILMING("드라마/영화/MV촬영"),
    CF_FILMING("화보/제품/CF촬영"),
    POP_UP_STORE("팝업 스토어"),
    ADVERTISEMENT("옥외광고"),
    CAMP("캠프/연수"),
    ATHLETICS_CONTEST("체육/경연대회"),
    CONCERT("공연/콘서트"),
    EXHIBITION_EVENT("박람회/전시"),
    ETC_WORK("기타(work)"),
    STUDIO_FILMING("스튜디오 촬영"),
    SOCIAL_GATHERING("친목모임"),
    DANCE("댄스 연습"),
    ONE_DAY_CLASS("원데이클래스/레슨");

    private final String displayName;

    PromotionCategory(String displayName) {
        this.displayName = displayName;
    }
}
