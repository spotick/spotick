package com.app.spotick.api.response;

import lombok.Builder;
import lombok.Data;

@Data
public class MessageResponse {
    private boolean success;
    private String message;

    @Builder
    public MessageResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
