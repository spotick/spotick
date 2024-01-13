package com.app.spotick.service.user;

import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.entity.user.UserProfileFile;
import com.app.spotick.repository.user.UserProfileFileRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class UserProfileFileServiceImpl implements UserProfileFileService {
    private final UserProfileFileRepository userProfileFileRepository;

    private final EntityManager em;
    private Random random = new Random();
//    이 부분은 yml 등록 고려
    private final String DEFAULT_UPLOAD_PATH = "/imgs/defaultProfileImgs/";


    @Override
    public void updateDefaultImg(String imgName, Long fileId) {
        UserProfileFile userProfileFile = em.find(UserProfileFile.class, fileId);

        userProfileFile.updateImage(imgName, null, DEFAULT_UPLOAD_PATH);
    }

    @Override
    public void updatePersonalImg(String fileName, String uuid, String uploadPath, Long fileId) {
        UserProfileFile userProfileFile = em.find(UserProfileFile.class, fileId);

        userProfileFile.updateImage(fileName, uuid, uploadPath);
    }

    @Override
    public void saveDefaultRandomImgByUser(User user) {
        userProfileFileRepository.save(UserProfileFile.builder()
                .user(user)
                .uploadPath(DEFAULT_UPLOAD_PATH)
                .fileName((random.nextInt(11)+1)+".jpg")
                .build());
    }
}
