package com.app.spotick.service.place.reservation;

import com.app.spotick.domain.dto.place.reservation.PlaceReserveRegisterDto;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceReservation;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.place.PlaceReservationStatus;
import com.app.spotick.repository.place.PlaceRepository;
import com.app.spotick.repository.place.reservation.PlaceReservationRepository;
import com.app.spotick.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceReservationServiceImpl implements PlaceReservationService {
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
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

        @Override
        public void registerPlaceReservation(PlaceReserveRegisterDto placeReserveRegisterDto, Long userId) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            User userProxy = userRepository.getReferenceById(userId);
            Place placeProxy = placeRepository.getReferenceById(placeReserveRegisterDto.getPlaceId());

            placeReservationRepository.save(PlaceReservation.builder()
                    .place(placeProxy)
                    .user(userProxy)
                    .checkIn(LocalDateTime.parse(placeReserveRegisterDto.getReservationCheckIn(),formatter))
                    .checkOut(LocalDateTime.parse(placeReserveRegisterDto.getReservationCheckOut(),formatter))
                    .content(placeReserveRegisterDto.getReservationContent())
                    .amount(placeReserveRegisterDto.getReservationAmount())
                    .visitors(placeReserveRegisterDto.getReservationVisitors())
                    .reservationStatus(PlaceReservationStatus.PENDING)
                    .build());
    }

    @Override
    public void updateNotReviewing(Long reservationId) {
        PlaceReservation foundReservation = placeReservationRepository.findById(reservationId).orElseThrow(
                NoSuchElementException::new
        );

        foundReservation.updateNotReviewing(true);
    }

}
