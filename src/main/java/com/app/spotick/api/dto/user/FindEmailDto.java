package com.app.spotick.api.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Data @NoArgsConstructor
@Component
public class FindEmailDto {
    private String nickname;
    private String tel;
}
