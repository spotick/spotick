package com.app.spotick.domain.entity.place;

import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "TBL_PLACE_INQUIRY")
@SequenceGenerator(name = "SEQ_PLACE_INQUIRY_GENERATOR", sequenceName = "SEQ_PLACE_INQUIRY",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceInquiry extends Period {
    @Id @GeneratedValue(generator = "SEQ_PLACE_INQUIRY_GENERATOR")
    @Column(name = "PLACE_INQUIRY_ID")
    private Long id;
    private String title;
    @Column(length = 1000)
    private String content;
    @Column(length = 1000)
    private String response;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLACE_ID")
    private Place place;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Builder
    public PlaceInquiry(Long id, String title, String content, String response, Place place, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.response = response;
        this.place = place;
        this.user = user;
    }
}
