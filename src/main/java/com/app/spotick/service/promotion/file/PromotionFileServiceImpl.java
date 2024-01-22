package com.app.spotick.service.promotion.file;

import com.app.spotick.domain.entity.promotion.PromotionBoard;
import com.app.spotick.domain.entity.promotion.PromotionFile;
import com.app.spotick.repository.promotion.PromotionFileRepository;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
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
public class PromotionFileServiceImpl implements PromotionFileService {
    private final PromotionFileRepository promotionFileRepository;

    @Value("${root.dir}")
    private String ROOT_DIR;
    @Override
    public void registerAndSavePromotionFile(List<MultipartFile> promotionFiles, PromotionBoard promotionBoard) throws IOException {
        for(MultipartFile file:promotionFiles){
            PromotionFile promotionFile = saveFile(file);
            promotionFile.setPromotionBoard(promotionBoard);
            promotionFileRepository.save(promotionFile);
        }
    }

    private PromotionFile saveFile(MultipartFile promotionFile) throws IOException {
        String originName = promotionFile.getOriginalFilename();
        originName = originName.replace("\\s",""); //파일 이름에 공백 제거
        UUID uuid = UUID.randomUUID();

        String sysName = uuid.toString()+"_"+originName;

        File uploadPath = new File(ROOT_DIR,getUploadPath());

        if(!uploadPath.exists()){
            uploadPath.mkdirs();
        }

        File uploadFile = new File(uploadPath,sysName);

        promotionFile.transferTo(uploadFile);

        if(Files.probeContentType(uploadFile.toPath()).startsWith("image")){
            File thumbnailFile = new File(uploadPath,"t_"+sysName);

            try(FileOutputStream out = new FileOutputStream(thumbnailFile);
            InputStream in = promotionFile.getInputStream()){
            Thumbnailator.createThumbnail(promotionFile.getInputStream(),out,300,225);
            }
            // try resource 를 활용해 자동으로 리소스를 닫아준다 out.close 생략 가능
        }

        return PromotionFile.builder()
                .uuid(uuid.toString())
                .fileName(originName)
                .uploadPath(getUploadPath())
                .build();
    }

    private String getUploadPath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

}
