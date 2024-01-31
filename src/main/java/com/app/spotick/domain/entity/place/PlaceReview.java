package com.app.spotick.domain.entity.place;

import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "TBL_PLACE_REVIEW")
@SequenceGenerator(name = "SEQ_PLACE_REVIEW_GENERATOR", sequenceName = "SEQ_PLACE_REVIEW",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceReview extends Period {
    @Id @GeneratedValue(generator = "SEQ_PLACE_REVIEW_GENERATOR")
    @Column(name = "PLACE_REVIEW_ID")
    private Long id;
    private String content;
    private Integer score;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLACE_RESERVATION_ID")
    private PlaceReservation placeReservation;

    @Builder
    public PlaceReview(Long id, String content, Integer score, User user, PlaceReservation placeReservation) {
        this.id = id;
        this.content = content;
        this.score = score;
        this.user = user;
        this.placeReservation = placeReservation;
    }

    public void updateReview(String content, Integer score) {
        this.content = content;
        this.score = score;
    }
}
