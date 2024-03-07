package com.app.spotick.domain.dto.user;

import com.app.spotick.domain.type.user.AuthorityType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserAuthorityDto {
    private Long userId;
    private AuthorityType authorityType;

    public UserAuthorityDto(Long userId, AuthorityType authorityType) {
        this.userId = userId;
        this.authorityType = authorityType;
    }
}
