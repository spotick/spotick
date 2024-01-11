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
    Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    User user;

    @Builder
    public PlaceFile(String fileName, String uuid, String uploadPath, Long id, User user) {
        super(fileName, uuid, uploadPath);
        this.id = id;
        this.user = user;
    }
}
