package com.app.spotick.repository.place.payment;

import com.app.spotick.api.dto.place.PlacePaymentInfoDto;
import com.app.spotick.domain.entity.place.QPlace;
import com.app.spotick.domain.entity.place.QPlacePayment;
import com.app.spotick.domain.entity.place.QPlaceReservation;
import com.app.spotick.domain.entity.user.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.app.spotick.domain.entity.place.QPlace.place;
import static com.app.spotick.domain.entity.place.QPlacePayment.placePayment;
import static com.app.spotick.domain.entity.place.QPlaceReservation.placeReservation;
import static com.app.spotick.domain.entity.user.QUser.user;

@RequiredArgsConstructor
public class PlacePaymentQDSLRepositoryImpl implements PlacePaymentQDSLRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<PlacePaymentInfoDto> findPaymentDetailById(Long paymentId) {

        return Optional.ofNullable(queryFactory.select(Projections.constructor(PlacePaymentInfoDto.class,
                        placePayment.id,
                        placePayment.amount,
                        placePayment.paymentMethod,
                        placeReservation.id,
                        place.title,
                        placeReservation.checkIn,
                        placeReservation.checkOut,
                        user.id,
                        user.nickName,
                        user.tel,
                        user.email
                ))
                .from(placePayment)
                .where(placePayment.id.eq(paymentId))
                .join(placePayment.placeReservation, placeReservation)
                .join(placeReservation.place, place)
                .join(placePayment.user, user)
                .fetchOne());
    }
}
