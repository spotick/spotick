package com.app.spotick.domain.entity.place;

import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.entity.compositePk.PlaceBookmarkId;
import com.app.spotick.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Entity @Table(name = "TBL_PLACE_BOOKMARK")
//@SequenceGenerator(name = "SEQ_PLACE_BOOKMARK_GENERATOR", sequenceName = "SEQ_PLACE_BOOKMARK",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceBookmark extends Period implements Persistable<PlaceBookmarkId> {

    @EmbeddedId
    private PlaceBookmarkId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLACE_ID", insertable = false, updatable = false)
    private Place place;

    @Builder
    public PlaceBookmark(PlaceBookmarkId id, User user, Place place) {
        this.id = id;
        this.user = user;
        this.place = place;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
}
