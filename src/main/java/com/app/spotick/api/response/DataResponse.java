package com.app.spotick.api.response;

import lombok.Builder;
import lombok.Data;

@Data
public class DataResponse<T> {
    private boolean success;
    private String message;
    private T data;

    @Builder
    public DataResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
