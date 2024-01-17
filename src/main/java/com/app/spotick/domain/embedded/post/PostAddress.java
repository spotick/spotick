package com.app.spotick.domain.embedded.post;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter @ToString
@EqualsAndHashCode @NoArgsConstructor
public class PostAddress {
    private String address;
    private String addressDetail;

    public PostAddress(String address, String addressDetail) {
        this.address = address;
        this.addressDetail = addressDetail;
    }

    // 리스트 화면에서 뿌리기 위해 사용할 주소 가공 함수
    // 서울특별시 강남구 테헤란로 202 => 서울특별시 강남구
    public void cutAddress(){
        int cutPoint = address.indexOf(" ", address.indexOf(" ") + 1);
        address = address.substring(0, cutPoint);
    }
}
