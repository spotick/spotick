package com.app.spotick.service.user;

import com.app.spotick.domain.dto.promotion.FileDto;
import com.app.spotick.domain.entity.user.UserProfileFile;
import com.app.spotick.repository.user.UserProfileFileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Objects;
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
    public FileDto updatePersonalImg(MultipartFile profileFile, Long userId) {
        try {
            FileDto savedFile = saveProfilefileAndGet(profileFile);

            UserProfileFile userProfileFile = userProfileFileRepository.findById(userId).orElseThrow(
                    NoSuchElementException::new
            );

            userProfileFile.updateImage(savedFile.getFileName(), savedFile.getUuid(), savedFile.getUploadPath());
            userProfileFile.setDefaultImage(false);

            return savedFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FileDto saveProfilefileAndGet(MultipartFile profileFile) throws IOException {
        String originalFileName = profileFile.getOriginalFilename();
        originalFileName = Objects.requireNonNull(originalFileName).replace("\\s", "");
        UUID uuid = UUID.randomUUID();

        String sysName = uuid + "_" + originalFileName;

        File uploadPath = new File(ROOT_DIR, getUploadPath());

        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        File uploadFile = new File(uploadPath, sysName);

        profileFile.transferTo(uploadFile);

        if (Files.probeContentType(uploadFile.toPath()).startsWith("image")) {
            File thumbnailFile = new File(uploadPath, "t_" + sysName);

            try (FileOutputStream out = new FileOutputStream(thumbnailFile);
                 InputStream in = profileFile.getInputStream()) {
                Thumbnailator.createThumbnail(profileFile.getInputStream(), out, 300, 225);
            }
            // try resource 를 활용해 자동으로 리소스를 닫아준다 out.close 생략 가능
        }

        return FileDto.builder()
                .uuid(uuid.toString())
                .fileName(originalFileName)
                .uploadPath(getUploadPath())
                .build();
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

    @Override
    public UserProfileFile saveImgFromImgUrl(String imgUrlString) {
        try {
            URL imgUrl = new URL(imgUrlString);
            String originName = extractFileName(imgUrl);
            UUID uuid = UUID.randomUUID();
            String sysName = uuid.toString() + "_" + originName;
            String thumbnailName = "t_" + sysName;

            String imgUploadPath = getUploadPath();

            File uploadPath = new File(ROOT_DIR, imgUploadPath);

            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            Path imgPath = Path.of(ROOT_DIR, imgUploadPath, sysName);
            Path thumbnailPath = Path.of(ROOT_DIR, imgUploadPath, thumbnailName);

            try (InputStream in = imgUrl.openStream()) {
                Files.copy(in, imgPath);
            }

            try (InputStream in = new FileInputStream(imgPath.toFile());
                 OutputStream out = new FileOutputStream(thumbnailPath.toFile())) {
                Thumbnailator.createThumbnail(in, out, 300, 200);
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















