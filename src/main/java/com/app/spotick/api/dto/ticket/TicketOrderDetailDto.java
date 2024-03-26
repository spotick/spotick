package com.app.spotick.api.dto.ticket;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicketOrderDetailDto {
    private Long gradeId;
    private int quantity;
}
