package com.app.spotick.domain.entity.place;

import com.app.spotick.domain.base.post.PostBase;
import com.app.spotick.domain.embedded.post.PostAddress;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.post.PostStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBL_PLACE")
@SequenceGenerator(name = "SEQ_PLACE_GENERATOR", sequenceName = "SEQ_PLACE", allocationSize = 1)
@Getter @ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place extends PostBase {
    @Id
    @GeneratedValue(generator = "SEQ_PLACE_GENERATOR")
    @Column(name = "PLACE_ID")
    private Long id;
    private String subTitle;
    @Column(length = 2000)
    private String info;
    @Column(length = 2000)
    private String rule;
    private Integer defaultPeople;
    @Embedded
    private PostAddress placeAddress;
    private Integer price;  //기본요금
    private Integer surcharge;  //추가요금
    private String bankName;
    private String accountNumber; //계좌번호
    private String accountHolder; //예금주
    @Enumerated(EnumType.STRING)
    private PostStatus placeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY)
    private List<PlaceFile> placeFileList = new ArrayList<>();

//    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY)
//    private List<PlaceReview> placeReviewList = new ArrayList<>();

    @Builder
    public Place(String title, int viewCount, Double lat, Double lng, Long id, String subTitle, String info, String rule, Integer defaultPeople, PostAddress placeAddress, Integer price, Integer surcharge, String bankName, String accountNumber, String accountHolder, PostStatus placeStatus, User user,  List<PlaceFile> placeFileList) {
        super(title, viewCount, lat, lng);
        this.id = id;
        this.subTitle = subTitle;
        this.info = info;
        this.rule = rule;
        this.defaultPeople = defaultPeople;
        this.placeAddress = placeAddress;
        this.price = price;
        this.surcharge = surcharge;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.placeStatus = placeStatus;
        this.user = user;
        this.placeFileList = placeFileList;
    }


    //    장소 호스트 설정 메소드
    public void setHost(User user) {
        this.user = user;
    }

    //    장소 상태 설정 메소드
    public void setPlaceStatus(PostStatus placeStatus) {
        this.placeStatus = placeStatus;
    }


}



