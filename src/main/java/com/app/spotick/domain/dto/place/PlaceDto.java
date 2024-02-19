package com.app.spotick.domain.dto.place;

import com.app.spotick.domain.dto.place.file.PlaceFileDto;
import com.app.spotick.domain.embedded.post.PostAddress;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor
public class PlaceDto {
    private Long id;
    private String title;
    private String subtitle;
    private Double lat;
    private Double lng;
    private PostAddress postAddress;
    private List<PlaceFileDto> placeFiles;
    private String info;
    private String rule;
    private Integer defaultPeople;
    private Integer price;
    private Integer surcharge;
    private String bankName;
    private String accountNumber;
    private String accountHolder;

    public PlaceDto(Long id, String title, String subtitle, Double lat, Double lng, PostAddress postAddress, List<PlaceFileDto> placeFiles, String info, String rule, Integer defaultPeople, Integer price, Integer surcharge, String bankName, String accountNumber, String accountHolder) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.lat = lat;
        this.lng = lng;
        this.postAddress = postAddress;
        this.placeFiles = placeFiles;
        this.info = info;
        this.rule = rule;
        this.defaultPeople = defaultPeople;
        this.price = price;
        this.surcharge = surcharge;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
    }
}
