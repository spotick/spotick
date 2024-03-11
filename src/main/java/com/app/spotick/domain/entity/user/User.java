package com.app.spotick.domain.entity.user;

import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.type.user.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.aspectj.weaver.patterns.PerObject;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Table(name = "TBL_USER")
@SequenceGenerator(name = "SEQ_USER_GENERATOR", sequenceName = "SEQ_USER",allocationSize = 1)
@Getter @ToString @NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
public class User extends Period {
    @Id @GeneratedValue(generator = "SEQ_USER_GENERATOR")
    @Column(name = "USER_ID")
    private Long id;
    private String email;
    private String password;
    private String nickName;
    private String tel;
    @Enumerated(EnumType.STRING) @ColumnDefault("'ACTIVATE'")
    private UserStatus userStatus;
    private LocalDate suspensionEndDate;    // 정지 종료일

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "USER_PROFILE_FILE_ID")
    private UserProfileFile userProfileFile;

    @Builder
    public User(Long id, String email, String password, String nickName, String tel, UserStatus userStatus, LocalDate suspensionEndDate, UserProfileFile userProfileFile) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.tel = tel;
        this.userStatus = userStatus;
        this.suspensionEndDate = suspensionEndDate;
        this.userProfileFile = userProfileFile;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updateTel(String tel) {
        this.tel = tel;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateUserProfile(UserProfileFile userProfileFile){
        this.userProfileFile = userProfileFile;
    }

    public void updateUserStatus(UserStatus userStatus){
        this.userStatus = userStatus;
    }

    public void updateSuspensionEndDate(LocalDate suspensionEndDate){
        this.suspensionEndDate = suspensionEndDate;
    }
}




