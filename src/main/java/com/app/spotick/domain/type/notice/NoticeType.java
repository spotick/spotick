package com.app.spotick.domain.type.notice;

import lombok.Getter;

@Getter
public enum NoticeType {
    INQUIRY_RESPONSE("문의 답변",
            "문의가 답변되었습니다. 지금 확인해보세요.",
            "/mypage/inquiries"),

    PLACE_INQUIRY_REGISTER("문의 등록",
            "새로 들어온 장소 문의가 있습니다. 지금 확인해보세요.",
            "/mypage/places"),

    TICKET_INQUIRY_REGISTER("문의 등록",
                             "새로 들어온 티켓 문의가 있습니다. 지금 확인해보세요.",
                             "/mypage/tickets");



    private final String title;
    private final String content;
    private final String link;

    NoticeType(String title, String content, String link) {
        this.title = title;
        this.content = content;
        this.link = link;
    }
}
