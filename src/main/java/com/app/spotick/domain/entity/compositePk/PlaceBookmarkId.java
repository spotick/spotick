package com.app.spotick.domain.entity.compositePk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
public class PlaceBookmarkId implements Serializable {
    @Column(name = "PLACE_ID")
    private Long placeId;
    @Column(name = "USER_ID")
    private Long userId;

    public PlaceBookmarkId(Long placeId, Long userId) {
        this.placeId = placeId;
        this.userId = userId;
    }
}
