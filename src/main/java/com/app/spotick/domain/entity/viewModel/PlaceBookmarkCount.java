package com.app.spotick.domain.entity.viewModel;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Subselect;

//@Entity
@Subselect("""
        select PLACE_ID as placeId,count(PLACE_ID) as bookmarkCount from TBL_PLACE_BOOKMARK
        group by PLACE_ID
        """)
@Getter
@ToString
public class PlaceBookmarkCount {
//    @Id
    private Long placeId;
    private Long bookmarkCount;
}

