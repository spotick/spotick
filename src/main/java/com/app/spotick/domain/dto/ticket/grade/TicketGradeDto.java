package com.app.spotick.domain.dto.ticket.grade;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class TicketGradeDto {
    private String gradeName;
    private Integer price;
    private Integer maxPeople;

    public TicketGradeDto(String gradeName, Integer price, Integer maxPeople) {
        this.gradeName = gradeName;
        this.price = price;
        this.maxPeople = maxPeople;
    }
}
