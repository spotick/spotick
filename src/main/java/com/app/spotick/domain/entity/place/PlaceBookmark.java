package com.app.spotick.domain.entity.place;

import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "TBL_PLACE_BOOKMARK")
@SequenceGenerator(name = "SEQ_PLACE_BOOKMARK_GENERATOR", sequenceName = "SEQ_PLACE_BOOKMARK",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceBookmark extends Period {
    @Id @GeneratedValue(generator = "SEQ_PLACE_BOOKMARK_GENERATOR")
    @Column(name = "PLACE_BOOKMARK_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLACE_ID")
    private Place place;

    @Builder
    public PlaceBookmark(Long id, User user, Place place) {
        this.id = id;
        this.user = user;
        this.place = place;
    }
}
