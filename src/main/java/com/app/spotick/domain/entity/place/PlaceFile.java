package com.app.spotick.domain.entity.place;

import com.app.spotick.domain.base.image.ImageBase;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "TBL_PLACE_FILE")
@SequenceGenerator(name = "SEQ_PLACE_FILE_GENERATOR", sequenceName = "SEQ_PLACE_FILE",allocationSize = 1)
@Getter @ToString(callSuper = true,exclude = "place") @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceFile extends ImageBase {
    @Id @GeneratedValue(generator = "SEQ_PLACE_FILE_GENERATOR")
    @Column(name = "PLACE_FILE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLACE_ID")
    private Place place;

    @Builder
    public PlaceFile(String fileName, String uuid, String uploadPath, Long id, Place place) {
        super(fileName, uuid, uploadPath);
        this.id = id;
        this.place = place;
    }

    public void setPlace(Place place){
        this.place = place;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
