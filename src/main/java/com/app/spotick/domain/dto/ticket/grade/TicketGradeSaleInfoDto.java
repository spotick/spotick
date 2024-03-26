package com.app.spotick.domain.dto.ticket.grade;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class TicketGradeSaleInfoDto {
    private Long gradeId;
    private String gradeName;
    private Integer price;
    private Long sold;
    private Integer maxPeople;

    public TicketGradeSaleInfoDto(Long gradeId, String gradeName, Integer price, Long sold, Integer maxPeople) {
        this.gradeId = gradeId;
        this.gradeName = gradeName;
        this.price = price;
        this.sold = sold;
        this.maxPeople = maxPeople;
    }
}
