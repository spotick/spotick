package com.app.spotick.api.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserFindEmailDto {

    @Data
    @NoArgsConstructor
    public static class Request{
        private String nickname;
        private String tel;
        private String certCode;
    }

    @Data
    @NoArgsConstructor
    public static class Response{
        private String email;
        private LocalDateTime createdDate;
        private String createdDateStr;
        public Response(String email, LocalDateTime createdDate) {
            this.email = email;
            this.createdDate = createdDate;
        }
    }

}
