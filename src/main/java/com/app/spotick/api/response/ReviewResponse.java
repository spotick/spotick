package com.app.spotick.api.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data @NoArgsConstructor
public class ReviewResponse {
    private boolean success;
    private List<Map<String, String>> errors;

    public ReviewResponse(boolean success) {
        this.success = success;
        this.errors =  new ArrayList<>();
    }

    public void addError(String field, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("field", field);
        error.put("message", message);
        errors.add(error);
    }
}
