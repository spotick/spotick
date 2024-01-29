package com.app.spotick.service.place.review;

import com.app.spotick.domain.dto.place.reservation.PlaceReviewRegisterDto;
import com.app.spotick.domain.entity.place.PlaceReservation;
import com.app.spotick.domain.entity.place.PlaceReview;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.repository.place.Review.PlaceReviewRepository;
import com.app.spotick.repository.place.reservation.PlaceReservationRepository;
import com.app.spotick.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceReviewServiceImpl implements PlaceReviewService {
    private final PlaceReviewRepository placeReviewRepository;
    private final UserRepository userRepository;
    private final PlaceReservationRepository placeReservationRepository;

    @Override
    public void registerReview(PlaceReviewRegisterDto placeReviewRegisterDto) {
        User tmpUser = userRepository.getReferenceById(placeReviewRegisterDto.getUserId());
        PlaceReservation tmpReservation
                = placeReservationRepository.getReferenceById(placeReviewRegisterDto.getReservationId());

        PlaceReview placeReview = PlaceReview.builder()
                .user(tmpUser)
                .placeReservation(tmpReservation)
                .score(placeReviewRegisterDto.getScore())
                .content(placeReviewRegisterDto.getContent())
                .build();
        placeReviewRepository.save(placeReview);
    }
}
