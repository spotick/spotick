package com.app.spotick.domain.entity.place;

import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.type.place.PlaceModifyStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "TBL_PLACE_MODIFY_REQUEST")
@SequenceGenerator(name = "SEQ_PLACE_MODIFY_REQUEST_GENERATOR", sequenceName = "SEQ_PLACE_MODIFY_REQUEST",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceModifyRequest extends Period {
    @Id @GeneratedValue(generator = "SEQ_PLACE_MODIFY_REQUEST_GENERATOR")
    @Column(name = "PLACE_MODIFY_REQUEST_ID")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORIGINAL_PLACE_ID")
    private Place originalPlace;    //수정 요청된 원본 장소
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHANGED_PLACE_ID")
    private Place changedPlace;     //승인 후 변경될 장소아이디
    @Enumerated(EnumType.STRING)
    private PlaceModifyStatus placeModifyStatus;

    @Builder
    public PlaceModifyRequest(Long id, Place originalPlace, Place changedPlace, PlaceModifyStatus placeModifyStatus) {
        this.id = id;
        this.originalPlace = originalPlace;
        this.changedPlace = changedPlace;
        this.placeModifyStatus = placeModifyStatus;
    }
}
