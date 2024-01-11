package com.app.spotick.domain.entity.user;

import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.type.user.AuthorityType;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "TBL_USER_AUTHORITY")
@SequenceGenerator(name = "SEQ_USER_AUTHORITY_GENERATOR", sequenceName = "SEQ_USER_AUTHORITY",allocationSize = 1)
@Getter @ToString @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAuthority extends Period {
    @Id @GeneratedValue(generator = "SEQ_USER_AUTHORITY_GENERATOR")
    @Column(name = "AUTHORITY_ID")
    private Long id;
    @Enumerated(EnumType.STRING)
    private AuthorityType authorityType;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "USER_ID")
    private User user;

    @Builder
    public UserAuthority(Long id, AuthorityType authorityType, User user) {
        this.id = id;
        this.authorityType = authorityType;
        this.user = user;
    }
}
