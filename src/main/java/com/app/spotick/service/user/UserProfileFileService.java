package com.app.spotick.service.user;

import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.entity.user.UserProfileFile;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URL;

public interface UserProfileFileService {
    void updateDefaultImg(String imgName, Long fileId);

    void updatePersonalImg(String fileName, String uuid, Long fileId);

//    회원가입시 기본 이미지 저장
    UserProfileFile saveDefaultRandomImgByUser();

//    oauth2 로그인시 이미지 url로 사진 저장
    UserProfileFile saveImgFromImgUrl(String imgUrl);
}
