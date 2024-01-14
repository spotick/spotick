package com.app.spotick.domain.entity.user;

import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.type.user.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.aspectj.weaver.patterns.PerObject;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;

@Entity @Table(name = "TBL_USER")
@SequenceGenerator(name = "SEQ_USER_GENERATOR", sequenceName = "SEQ_USER",allocationSize = 1)
@Getter @ToString @NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
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

    @Builder
    public User(Long id, String email, String password, String nickName, String tel, UserStatus userStatus, LocalDate suspensionEndDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.tel = tel;
        this.userStatus = userStatus;
        this.suspensionEndDate = suspensionEndDate;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updateTel(String tel) {
        this.tel = tel;
    }
}




