package com.app.spotick.vo;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Data @NoArgsConstructor
public class GptChatVo {
    String role;
    String content;

    @Builder
    public GptChatVo(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
