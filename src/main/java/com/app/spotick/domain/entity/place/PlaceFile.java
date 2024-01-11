package com.app.spotick.domain.entity.place;

import com.app.spotick.domain.base.image.ImageBase;
import com.app.spotick.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "TBL_PLACE_FILE")
@SequenceGenerator(name = "SEQ_PLACE_FILE_GENERATOR", sequenceName = "SEQ_PLACE_FILE",allocationSize = 1)
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceFile extends ImageBase {
    @Id @GeneratedValue(generator = "SEQ_PLACE_FILE_GENERATOR")
    @Column(name = "PLACE_FILE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLACE_ID")
    private Place place;

    @Builder
    public PlaceFile(String fileName, String uuid, String uploadPath, Long id, User user, Place place) {
        super(fileName, uuid, uploadPath);
        this.id = id;
        this.user = user;
        this.place = place;
    }
}
