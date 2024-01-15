package com.app.spotick.service.user;

import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.entity.user.UserProfileFile;
import com.app.spotick.repository.user.UserProfileFileRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class UserProfileFileServiceImpl implements UserProfileFileService {
    private final UserProfileFileRepository userProfileFileRepository;
    private final Random random = new Random();

    @Value("${default.profileFileDir}")
    private String DEFAULT_UPLOAD_PATH;

    @Override
    public void updateDefaultImg(String imgName, Long fileId) {
        UserProfileFile userProfileFile = userProfileFileRepository.findById(fileId).orElseThrow(
                NoSuchElementException::new
        );

        userProfileFile.updateImage(imgName, null, null);
        userProfileFile.setDefaultImage(true);
    }

    @Override
    public void updatePersonalImg(String fileName, String uuid, Long fileId) {
        UserProfileFile userProfileFile = userProfileFileRepository.findById(fileId).orElseThrow(
                NoSuchElementException::new
        );

        userProfileFile.updateImage(fileName, uuid, getFilePath());
        userProfileFile.setDefaultImage(false);
    }

    @Override
    public void saveDefaultRandomImgByUser(User user) {
        userProfileFileRepository.save(UserProfileFile.builder()
                .user(user)
                .uploadPath(null)
                .fileName((random.nextInt(11) + 1) + ".jpg")
                .isDefaultImage(true)
                .build());
    }

    private String getFilePath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
}
