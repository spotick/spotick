package com.app.spotick.domain.base.post;

import com.app.spotick.domain.base.Period;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@MappedSuperclass
@Getter @ToString @NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class PostBase extends Period {
    private String title;
    private int viewCount;
    private Double lat; //위도
    private Double lng; //경도

    public PostBase(String title, int viewCount, Double lat, Double lng) {
        this.title = title;
        this.viewCount = viewCount;
        this.lat = lat;
        this.lng = lng;
    }
}
