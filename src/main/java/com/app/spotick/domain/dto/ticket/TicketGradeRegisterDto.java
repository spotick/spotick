package com.app.spotick.domain.dto.ticket;

import com.app.spotick.domain.entity.ticket.TicketGrade;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicketGradeRegisterDto {
    private String gradeName;
    private Integer price;
    private Integer maxPeople;

    public TicketGradeRegisterDto(String gradeName, Integer price, Integer maxPeople) {
        this.gradeName = gradeName;
        this.price = price;
        this.maxPeople = maxPeople;
    }

    public TicketGrade toEntity() {
        return TicketGrade.builder()
                .gradeName(gradeName)
                .price(price)
                .maxPeople(maxPeople)
                .build();
    }
}
