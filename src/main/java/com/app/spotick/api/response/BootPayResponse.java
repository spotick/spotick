package com.app.spotick.api.response;

import lombok.Builder;
import lombok.Data;

@Data
public class BootPayResponse<T> {
    private boolean success;
    private String message;
    private Integer code;
    private T data;

    @Builder
    public BootPayResponse(boolean success, String message, Integer code, T data) {
        this.success = success;
        this.message = message;
        this.code = code;
        this.data = data;
    }
}
