package com.app.spotick.domain.dto.user;

import com.app.spotick.domain.entity.user.UserAuthority;
import com.app.spotick.domain.type.user.UserStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@NoArgsConstructor @Data
public class UserProfileDto {
    private Long id;
    private String email;
    private String nickName;
    private String tel;
    private UserStatus userStatus;
    private LocalDateTime createdDate;
    private String fileName;
    private String uuid;
    private String uploadPath;

    public UserProfileDto(Long id, String email, String nickName, String tel, UserStatus userStatus, LocalDateTime createdDate, String fileName, String uuid, String uploadPath) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
        this.tel = tel;
        this.userStatus = userStatus;
        this.createdDate = createdDate;
        this.fileName = fileName;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
    }
}
