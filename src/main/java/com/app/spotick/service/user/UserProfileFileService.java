package com.app.spotick.service.user;

import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.entity.user.UserProfileFile;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserProfileFileService {
    void updateDefaultImg(String imgName, Long fileId);

    void updatePersonalImg(String fileName, String uuid, Long fileId);

//    회원가입시 기본 이미지 저장
    UserProfileFile saveDefaultRandomImgByUser();
}
