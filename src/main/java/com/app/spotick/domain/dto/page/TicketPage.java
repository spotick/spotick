package com.app.spotick.domain.dto.page;

import com.app.spotick.domain.dto.ticket.TicketManageListDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data @NoArgsConstructor
public class TicketPage {
    private Page<TicketManageListDto> page;
    private Long total;
    private Long upcomingCount; // 예정된 이벤트 카운트
    private Long pastCount; // 종료된 이벤트 카운트
}
