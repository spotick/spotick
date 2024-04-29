package com.app.spotick.service.user;

import com.app.spotick.domain.dto.promotion.FileDto;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.entity.user.UserProfileFile;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

public interface UserProfileFileService {
    void updateDefaultImg(String imgName, Long fileId);

    FileDto updatePersonalImg(MultipartFile uploadFile, Long userId) throws IOException;
//    회원가입시 기본 이미지 저장
    UserProfileFile saveDefaultRandomImgByUser();

//    oauth2 로그인시 이미지 url로 사진 저장
    UserProfileFile saveImgFromImgUrl(String imgUrl);
}
