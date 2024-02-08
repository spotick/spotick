package com.app.spotick.security.type;

public enum OAuthType {
    KAKAO("kakao"),
//    NAVER("naver"),
//    GOOGLE("google"),
//    NONE("none")
    ;

    private final String registrationId;
    OAuthType(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getRegistrationId() {
        return registrationId;
    }
}
