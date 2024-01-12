package com.app.spotick.controller.file;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/file/*")
@RequiredArgsConstructor
public class FileController {

    //    파일 업로드
    @PostMapping("upload")
    public List<String> uploadFile(@RequestParam("uploadFile") List<MultipartFile> uploadFiles) {
        // 해당 부분은 일시적으로 개인의 c드라이브에 경로 설정하여 테스트 한다.
        String rootPath = "C:/spotickFilesTest/uploadFiles/" + getPath();
        List<String> uuids = new ArrayList<>();
        File file = new File(rootPath);

        // 해당 경로에 파일 없을 시 생성
        if (!file.exists()) {
            file.mkdirs();
        }

//        전달받은 파일들 업로드 처리 및 uuid 반환
        for (MultipartFile uploadFile : uploadFiles) {
            // uuid 추가
            String uuid = UUID.randomUUID().toString();
            uuids.add(uuid);

            String originalName = uploadFile.getOriginalFilename();
            String fileName = Objects.requireNonNull(originalName).substring(originalName.lastIndexOf("\\") + 1);

            // 파일 이동
            try {
                // 전달받은 파일이 이미지일 시 같은 이름에 t_를 붙혀 썸네일 생성
                if (Objects.requireNonNull(uploadFile.getContentType()).startsWith("image")) {
                    FileOutputStream out = new FileOutputStream(new File(
                            rootPath, "t_" + uuid + "_" + fileName)
                    );
                    Thumbnailator.createThumbnail(uploadFile.getInputStream(), out, 200, 200);
                    out.close();
                }
                uploadFile.transferTo(new File(rootPath, uuid + "_" + fileName));
            } catch (IOException | IllegalStateException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("uuids = " + uuids);
        return uuids;
    }

    //    파일 display 함수
    @GetMapping("display")
    public byte[] display(String fileName) throws IOException {
        // 임시 테스트 경로 유지
        return FileCopyUtils.copyToByteArray(new File("C:/spotickFilesTest/uploadFiles/", fileName));
    }

    //    파일 경로에 연 월 일로 구분하여 파일 저장
    private String getPath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
}
