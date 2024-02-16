package com.app.spotick.domain.entity.viewModel;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.app.spotick.domain.entity.viewModel.QPlaceReviewCount.placeReviewCount;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional @Commit
class PlaceReviewCountTest {
    @Autowired
    private JPAQueryFactory queryFactory;


    @Test
    void subSelectTest(){
        List<PlaceReviewCount> list = queryFactory.selectFrom(placeReviewCount)
                .fetch();

        System.out.println("list = " + list);

    }


}