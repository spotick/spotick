package com.app.spotick.service.user;

import com.app.spotick.domain.entity.user.UserProfileFile;
import com.app.spotick.repository.user.UserProfileFileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserProfileFileServiceImpl implements UserProfileFileService {
    private final UserProfileFileRepository userProfileFileRepository;
    private final Random random = new Random();

    @Value("${default.profileFileDir}")
    private String DEFAULT_UPLOAD_PATH;
    @Value("${root.dir}")
    private String ROOT_DIR;

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
    public UserProfileFile saveDefaultRandomImgByUser() {
        return userProfileFileRepository.save(UserProfileFile.builder()
                .fileName((random.nextInt(11) + 1) + ".jpg")
                .uuid(null)
                .uploadPath(DEFAULT_UPLOAD_PATH)
                .isDefaultImage(true)
                .build());
    }

    private String getFilePath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    @Override
    public UserProfileFile saveImgFromImgUrl(String imgUrlString) {
        try {
            URL imgUrl = new URL(imgUrlString);
            String originName = extractFileName(imgUrl);
            UUID uuid = UUID.randomUUID();
            String sysName = uuid.toString() + "_" + originName;
            String thumbnailName = uuid.toString() + "t_" + originName;

            String imgUploadPath = getUploadPath();

            File uploadPath = new File(ROOT_DIR, imgUploadPath);

            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            Path savePath = Path.of(ROOT_DIR, imgUploadPath, sysName);
            Path thumbnailPath = Path.of(ROOT_DIR, imgUploadPath, thumbnailName);
            
            System.out.println("=============================================");
            System.out.println(savePath);
            System.out.println(imgUrlString);
            System.out.println("=============================================");

            try (InputStream in = imgUrl.openStream();
                 FileOutputStream out = new FileOutputStream(thumbnailPath.toFile())) {
                Files.copy(in, savePath);
                Thumbnailator.createThumbnail(in,out,300,200);
            }

            return UserProfileFile.builder()
                    .fileName(originName)
                    .uploadPath(imgUploadPath)
                    .uuid(uuid.toString())
                    .isDefaultImage(false)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
//            사진 저장 중 오류가 나면 기본이미지로 저장
            return saveDefaultRandomImgByUser();
        }
    }

    private String extractFileName(URL url) throws Exception {
        String fileName = url.getPath();
        // URL의 경로 부분에서 마지막 '/' 이후의 문자열을 추출
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    private String getUploadPath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
}















