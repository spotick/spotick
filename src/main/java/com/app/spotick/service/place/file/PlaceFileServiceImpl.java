package com.app.spotick.service.place.file;

import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceFile;
import com.app.spotick.repository.place.PlaceFileRepository;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceFileServiceImpl implements PlaceFileService{
    private final PlaceFileRepository placeFileRepository;

    private final String ROOT_DIR = "C:/spotickFilesTest/uploadFiles/";
    @Override
    public void registerAndSavePlaceFile(List<MultipartFile> placeFiles, Place place) throws IOException {
        for(MultipartFile file:placeFiles){
            PlaceFile placeFile = saveFile(file);
            placeFile.setPlace(place);
            placeFileRepository.save(placeFile);
        }
    }

    private PlaceFile saveFile(MultipartFile placeFile) throws IOException {
        String originName = placeFile.getOriginalFilename();
        originName = originName.replace("\\s",""); //파일 이름에 공백 제거
        UUID uuid = UUID.randomUUID();

        String sysName = uuid.toString()+"_"+originName;

        File uploadPath = new File(ROOT_DIR,getUploadPath());

        if(!uploadPath.exists()){
            uploadPath.mkdirs();
        }

        File uploadFile = new File(uploadPath,sysName);

        placeFile.transferTo(uploadFile);

        if(Files.probeContentType(uploadFile.toPath()).startsWith("image")){
            File thumbnailFile = new File(uploadPath,"t_"+sysName);

            try(FileOutputStream out = new FileOutputStream(thumbnailFile);
            InputStream in = placeFile.getInputStream()){
            Thumbnailator.createThumbnail(placeFile.getInputStream(),out,300,225);
            }
            // try resource 를 활용해 자동으로 리소스를 닫아준다 out.close 생략 가능
        }

        return PlaceFile.builder()
                .uuid(uuid.toString())
                .fileName(originName)
                .uploadPath(getUploadPath())
                .build();
    }





    private String getUploadPath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

}
