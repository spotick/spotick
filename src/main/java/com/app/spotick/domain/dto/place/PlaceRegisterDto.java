package com.app.spotick.domain.dto.place;

import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.post.PostStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor
public class PlaceRegisterDto {
    private Long placeId;
    private Long userId;
    private String placeName;
    private String placeSubTitle;
    private String placeInfo;
    private String placeRule;
    private Integer placeDefaultPeople;
    private String placeAddress;
    private String placeAddressDetail;
    private Integer placeDefaultPrice;  //기본요금
    private Integer placeSurcharge;  //추가요금
    private String bankName;
    private String accountNumber; //계좌번호
    private String accountHolder; //예금주
    private Double placeLat;
    private Double placeLng;
    private List<MultipartFile> placeFiles = new ArrayList<>();

}
