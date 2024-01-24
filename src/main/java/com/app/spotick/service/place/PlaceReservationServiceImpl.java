package com.app.spotick.service.place;

import com.app.spotick.domain.entity.place.PlaceReservation;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.place.PlaceReservationStatus;
import com.app.spotick.repository.place.reservation.PlaceReservationRepository;
import com.app.spotick.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceReservationServiceImpl implements PlaceReservationService {
    private final UserRepository userRepository;
    private final PlaceReservationRepository placeReservationRepository;

    @Override
    public Optional<PlaceReservation> findReservationByIdAndUser(Long reservationId, Long userId) {
        User tmpUser = userRepository.getReferenceById(userId);

        return placeReservationRepository.findByIdAndUser(reservationId, tmpUser);
    }

    @Override
    public void cancelReservation(Long reservationId) {
        PlaceReservation foundReservation = placeReservationRepository.findById(reservationId).orElseThrow(
                NoSuchElementException::new
        );

        foundReservation.updateStatus(PlaceReservationStatus.CANCELLED);
    }

    @Override
    public void deleteReservation(Long reservationId) {
        PlaceReservation foundReservation = placeReservationRepository.findById(reservationId).orElseThrow(
                NoSuchElementException::new
        );

        placeReservationRepository.delete(foundReservation);
    }
}
