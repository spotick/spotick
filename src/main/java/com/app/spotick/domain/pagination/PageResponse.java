package com.app.spotick.domain.pagination;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class PageResponse<T> {
    private Page<T> data;
    private Pagination pagination;

    public PageResponse(Page<T> data, Pagination pagination) {
        this.data = data;
        this.pagination = pagination;
    }
}
