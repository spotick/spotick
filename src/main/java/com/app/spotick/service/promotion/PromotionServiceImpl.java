package com.app.spotick.service.promotion;

import com.app.spotick.domain.dto.promotion.FileDto;
import com.app.spotick.domain.dto.promotion.PromotionRegisterDto;
import com.app.spotick.domain.entity.promotion.PromotionBoard;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.repository.promotion.PromotionRepository;
import com.app.spotick.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    @Value("${root.dir}")
    private String ROOT_DIR;

    private final PromotionRepository promotionRepository;
    private final UserRepository userRepository;

    @Override
    public Long promotionBoardSave(PromotionRegisterDto promotionRegisterDto) throws IOException {
        User tmpUser = userRepository.getReferenceById(promotionRegisterDto.getUserId());

        FileDto fileDto = saveFile(promotionRegisterDto.getFile());

        PromotionBoard entity = PromotionBoard.builder()
                .title(promotionRegisterDto.getTitle())
                .content(promotionRegisterDto.getContent())
                .promotionCategory(promotionRegisterDto.getCategory())
                .user(tmpUser)
                .fileName(fileDto.getFileName())
                .uuid(fileDto.getUuid())
                .uploadPath(fileDto.getUploadPath())
                .build();

        PromotionBoard savedEntity = promotionRepository.save(entity);

        return savedEntity.getId();
    }

    private FileDto saveFile(MultipartFile file) throws IOException {
        String originName = file.getOriginalFilename();
        originName = originName.replace("\\s",""); //파일 이름에 공백 제거
        UUID uuid = UUID.randomUUID();

        String sysName = uuid.toString()+"_"+originName;

        File uploadPath = new File(ROOT_DIR,getUploadPath());

        if(!uploadPath.exists()){
            uploadPath.mkdirs();
        }

        File uploadFile = new File(uploadPath,sysName);

        file.transferTo(uploadFile);

        if(Files.probeContentType(uploadFile.toPath()).startsWith("image")){
            File thumbnailFile = new File(uploadPath,"t_"+sysName);

            try(FileOutputStream out = new FileOutputStream(thumbnailFile);
                InputStream in = file.getInputStream()){
                Thumbnailator.createThumbnail(file.getInputStream(),out,300,225);
            }
            // try resource 를 활용해 자동으로 리소스를 닫아준다 out.close 생략 가능
        }

        return new FileDto(originName, uuid.toString(), getUploadPath());
    }

    private String getUploadPath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
}
