package com.app.spotick.file;

import net.coobird.thumbnailator.Thumbnailator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@SpringBootTest
public class FileTest {


    @Test
    @DisplayName("이미지 url로 로컬저장 테스트")
    void fileSaveTest() {
//        String imgUrlString = "http://k.kakaocdn.net/dn/iJnKP/btrUSvYAhV1/fjatPrXDsk2A8vl8cYeft0/img_640x640.jpg";
        String imgUrlString = "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/bd23ab90-ef8c-48d3-a613-38556452ab8a/dfy0fj1-fc3b5023-e28f-40de-a4c5-bf676d7563aa.jpg/v1/fill/w_1024,h_768,q_75,strp/orora_borealis_2_by_amadeusaya_dfy0fj1-fullview.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9NzY4IiwicGF0aCI6IlwvZlwvYmQyM2FiOTAtZWY4Yy00OGQzLWE2MTMtMzg1NTY0NTJhYjhhXC9kZnkwZmoxLWZjM2I1MDIzLWUyOGYtNDBkZS1hNGM1LWJmNjc2ZDc1NjNhYS5qcGciLCJ3aWR0aCI6Ijw9MTAyNCJ9XV0sImF1ZCI6WyJ1cm46c2VydmljZTppbWFnZS5vcGVyYXRpb25zIl19.kT2LBdKJKa4npu48LuUJdHrQlcIXuTz7JndcGU857NY";

        String ROOT_DIR = "C:/spotickFilesTest/uploadFiles/";

        try {
            URL imgUrl = new URL(imgUrlString);
            String originName = extractFileName(imgUrl);
            UUID uuid = UUID.randomUUID();
            String sysName = uuid.toString() + "_" + originName;
            String thumbnailName = "t_"+sysName;

            String imgUploadPath = getUploadPath();

            File uploadPath = new File(ROOT_DIR, imgUploadPath);

            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            Path savePath = Path.of(ROOT_DIR, imgUploadPath, sysName);
            Path thumbnailPath = Path.of(ROOT_DIR, imgUploadPath, thumbnailName);

            try (InputStream in = imgUrl.openStream()) {
                Files.copy(in, savePath);
            }

            try(InputStream in = new FileInputStream(savePath.toFile());
                FileOutputStream out = new FileOutputStream(thumbnailPath.toFile())){
                Thumbnailator.createThumbnail(in, out, 300, 200);
            }

        }catch (Exception e) {
            e.printStackTrace();
//            사진 저장 중 오류가 나면 기본이미지로 저장
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
