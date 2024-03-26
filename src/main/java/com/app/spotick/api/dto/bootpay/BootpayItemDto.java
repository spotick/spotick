package com.app.spotick.api.dto.bootpay;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BootpayItemDto {
    private Long id;
    private String name;
    private int qty;
    private Integer price;

    public BootpayItemDto(Long id, String name, int qty, Integer price) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.price = price;
    }
}
