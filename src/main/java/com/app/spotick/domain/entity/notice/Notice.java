package com.app.spotick.domain.entity.notice;

import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.notice.NoticeStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity @Table(name = "TBL_NOTICE")
@SequenceGenerator(name = "SEQ_NOTICE_GENERATOR", sequenceName = "SEQ_NOTICE",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Notice extends Period {
    @Id @GeneratedValue(generator = "SEQ_NOTICE_GENERATOR")
    @Column(name = "NOTICE_ID")
    private Long id;
    private String title;
    @Column(length = 1000)
    private String content;
    private String link;
    @Enumerated(EnumType.STRING)
    private NoticeStatus noticeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Builder
    public Notice(Long id, String title, String content, String link, NoticeStatus noticeStatus, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.link = link;
        this.noticeStatus = noticeStatus;
        this.user = user;
    }

    public void setStatus(NoticeStatus noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    public void setDescription(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
