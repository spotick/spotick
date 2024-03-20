package com.app.spotick.repository.admin.ticket;

import com.app.spotick.api.dto.admin.AdminPostSearchDto;
import com.app.spotick.domain.dto.admin.AdminPostListDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface AdminTicketQDSLRepository {
    public Slice<AdminPostListDto> findAdminTicketList(Pageable pageable, AdminPostSearchDto ticketSearchDto);
}
