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
}
