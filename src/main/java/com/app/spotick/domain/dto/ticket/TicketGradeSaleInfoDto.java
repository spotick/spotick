package com.app.spotick.domain.dto.ticket;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class TicketGradeSaleInfoDto {
    private String gradeName;
    private Integer price;
    private Long sold;
    private Integer maxPeople;

    public TicketGradeSaleInfoDto(String gradeName, Integer price, Long sold, Integer maxPeople) {
        this.gradeName = gradeName;
        this.price = price;
        this.sold = sold;
        this.maxPeople = maxPeople;
    }
}
