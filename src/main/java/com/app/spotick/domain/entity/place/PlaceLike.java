package com.app.spotick.domain.entity.place;

import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "TBL_PLACE_LIKE")
@SequenceGenerator(name = "SEQ_PLACE_LIKE_GENERATOR", sequenceName = "SEQ_PLACE_LIKE",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceLike extends Period {
    @Id @GeneratedValue(generator = "SEQ_PLACE_LIKE_GENERATOR")
    @Column(name = "PLACE_LIKE_ID")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLACE_ID")
    Place place;

    @Builder
    public PlaceLike(Long id, User user, Place place) {
        this.id = id;
        this.user = user;
        this.place = place;
    }
}
