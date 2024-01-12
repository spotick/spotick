package com.app.spotick.service.user;

import org.springframework.web.bind.annotation.RequestParam;

public interface UserProfileFileService {
    void updateDefaultImg(String imgName, Long fileId);

    void updatePersonalImg(String fileName, String uuid, String uploadPath, Long fileId);
}
