package com.app.spotick.domain.dto.admin;

import com.app.spotick.domain.type.user.AuthorityType;
import com.app.spotick.domain.type.user.UserStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class AdminUserListDto {
    private Long id;
    private String email;
    private String nickName;
    private String tel;
    private UserStatus userStatus;
    private AuthorityType authorityType;
    private LocalDateTime createdDate;
    private String createdDateStr;

    public AdminUserListDto(Long id, String email, String nickName, String tel, UserStatus userStatus, LocalDateTime createdDate) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
        this.tel = tel;
        this.userStatus = userStatus;
        this.createdDate = createdDate;
    }

    public void formatCreatedDate(){
        createdDateStr = this.createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }



}
