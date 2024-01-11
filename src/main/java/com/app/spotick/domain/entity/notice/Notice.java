package com.app.spotick.domain.entity.notice;

import com.app.spotick.domain.base.Period;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "TBL_NOTICE")
@SequenceGenerator(name = "SEQ_NOTICE_GENERATOR", sequenceName = "SEQ_NOTICE",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends Period {
    @Id @GeneratedValue(generator = "SEQ_NOTICE_GENERATOR")
    @Column(name = "NOTICE_ID")
    private Long id;
    private String title;
    @Column(length = 1000)
    private String content;

    @Builder
    public Notice(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
