package com.app.spotick.api.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data @NoArgsConstructor
public class FindIdCertDto {
    private String nickname;
    private String tel;
}
