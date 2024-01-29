package com.app.spotick.api.dto.response;

import com.app.spotick.domain.pagination.Pagination;
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
