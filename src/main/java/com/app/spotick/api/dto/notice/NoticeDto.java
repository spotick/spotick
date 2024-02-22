package com.app.spotick.api.dto.notice;

import com.app.spotick.domain.type.notice.NoticeStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NoticeDto {
    private Long noticeId;
    private String title;
    private String content;
    private String link;
    private NoticeStatus noticeStatus;
    private LocalDateTime createdDate;

    public NoticeDto(Long noticeId, String title, String content, String link, NoticeStatus noticeStatus, LocalDateTime createdDate) {
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
        this.link = link;
        this.noticeStatus = noticeStatus;
        this.createdDate = createdDate;
    }

    @Data
    @NoArgsConstructor
    public static class RequestStatus {
        private Long noticeId;
        private NoticeStatus noticeStatus;

        public RequestStatus(Long noticeId, NoticeStatus noticeStatus) {
            this.noticeId = noticeId;
            this.noticeStatus = noticeStatus;
        }
    }
}
