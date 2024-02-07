package com.app.spotick.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class CommonResponse<T> {
    private boolean success;
    private String message;
    private T data;

    @Builder
    public CommonResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
