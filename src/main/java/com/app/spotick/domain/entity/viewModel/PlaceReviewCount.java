package com.app.spotick.domain.entity.viewModel;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Subselect;

@Entity
@Subselect("""
        SELECT pr.PLACE_ID as placeId, count(p.PLACE_REVIEW_ID) as reveiwCont FROM TBL_PLACE_REVIEW p
        JOIN TBL_PLACE_RESERVATION pr
        ON p.PLACE_RESERVATION_ID = pr.PLACE_RESERVATION_ID
        GROUP BY pr.PLACE_ID
        """)
@Getter @ToString
public class PlaceReviewCount {
    @Id
    private Long placeId;
    private Long reviewCount;
}
