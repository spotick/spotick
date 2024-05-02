package com.app.spotick.api.controller.file;

import com.app.spotick.api.response.DataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    @Value("${default.profileFileDir}")
    private String DEFAULT_UPLOAD_PATH;
    @Value("${root.dir}")
    private String ROOT_PATH;
    @Value("${summernote.dir}")
    private String SUMMERNOTE_PATH;


    //    파일 업로드
    @PostMapping("/upload")
    public List<String> uploadFile(@RequestParam("uploadFile") List<MultipartFile> uploadFiles) {
        String rootPath = ROOT_PATH + getPath();
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
    @GetMapping("/display")
    public byte[] display(@RequestParam("fileName") String fileName) throws IOException {
        // 임시 테스트 경로 유지
        return FileCopyUtils.copyToByteArray(new File(ROOT_PATH, fileName));
    }

    // 기본이미지 display
    @GetMapping("/default/display")
    public byte[] defaultDisplay(@RequestParam("fileName") String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(DEFAULT_UPLOAD_PATH + fileName);
        return FileCopyUtils.copyToByteArray(resource.getInputStream());
    }

    @GetMapping("/sum")
    public byte[] sumDisplay(@RequestParam("fileName") String fileName) throws IOException {
        // 임시 테스트 경로 유지
        return FileCopyUtils.copyToByteArray(new File(SUMMERNOTE_PATH, fileName));
    }

    @PostMapping("/summernote/upload")
    public ResponseEntity<?> uploadNoteFile(@RequestParam("uploadFile") MultipartFile uploadFile) {
        String path = getPath();
        String rootPath = SUMMERNOTE_PATH + path;
        File file = new File(rootPath);

        // 해당 경로에 파일 없을 시 생성
        if (!file.exists()) {
            file.mkdirs();
        }

        String uuid = UUID.randomUUID().toString();

        String originalName = uploadFile.getOriginalFilename();
        String fileName = Objects.requireNonNull(originalName).substring(originalName.lastIndexOf("\\") + 1);

        // 파일 이동
        try {
            uploadFile.transferTo(new File(rootPath, uuid + "_" + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return new ResponseEntity<>(DataResponse.builder()
                .success(true)
                .message("성공")
                .data(path + "/" + uuid + "_" + fileName)
                .build(), HttpStatus.OK);
    }

    //    파일 경로에 연 월 일로 구분하여 파일 저장
    private String getPath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
}
