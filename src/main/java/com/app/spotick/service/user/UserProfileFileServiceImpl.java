package com.app.spotick.service.user;

import com.app.spotick.domain.entity.user.UserProfileFile;
import com.app.spotick.repository.user.UserProfileFileRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserProfileFileServiceImpl implements UserProfileFileService {
    private final UserProfileFileRepository userProfileFileRepository;

    private final EntityManager em;

    @Override
    public void updateDefaultImg(String imgName, Long fileId) {
        UserProfileFile userProfileFile = em.find(UserProfileFile.class, fileId);

        userProfileFile.updateImage(imgName, null, "/imgs/defaultProfileImgs/");
    }

    @Override
    public void updatePersonalImg(String fileName, String uuid, String uploadPath, Long fileId) {
        UserProfileFile userProfileFile = em.find(UserProfileFile.class, fileId);

        userProfileFile.updateImage(fileName, uuid, uploadPath);
    }
}
