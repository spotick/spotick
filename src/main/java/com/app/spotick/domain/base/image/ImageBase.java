package com.app.spotick.domain.base.image;

import com.app.spotick.domain.base.Period;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

@MappedSuperclass
@Getter @ToString @NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ImageBase extends Period {
    private String fileName;
    private String uuid;
    private String uploadPath;

    public ImageBase(String fileName, String uuid, String uploadPath) {
        this.fileName = fileName;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
    }

    public void updateImage(String fileName, String uuid, String uploadPath) {
        this.fileName = fileName;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
    }
}
