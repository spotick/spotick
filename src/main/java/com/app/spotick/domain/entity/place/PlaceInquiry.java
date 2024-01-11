package com.app.spotick.domain.entity.place;

import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.base.post.InquiryBase;
import com.app.spotick.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "TBL_PLACE_INQUIRY")
@SequenceGenerator(name = "SEQ_PLACE_INQUIRY_GENERATOR", sequenceName = "SEQ_PLACE_INQUIRY",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceInquiry extends InquiryBase {
    @Id @GeneratedValue(generator = "SEQ_PLACE_INQUIRY_GENERATOR")
    @Column(name = "PLACE_INQUIRY_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLACE_ID")
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Builder
    public PlaceInquiry(String title, String content, String response, Long id, Place place, User user) {
        super(title, content, response);
        this.id = id;
        this.place = place;
        this.user = user;
    }
}
