package com.app.spotick.domain.base.post;

import com.app.spotick.domain.base.Period;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@MappedSuperclass
@Getter @ToString @NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class InquiryBase extends Period {
    private String title;
    @Column(length = 1000)
    private String content;
    @Column(length = 1000)
    private String response;

    public InquiryBase(String title, String content, String response) {
        this.title = title;
        this.content = content;
        this.response = response;
    }

    public void updateResponse(String response) {
        this.response = response;
    }
}
