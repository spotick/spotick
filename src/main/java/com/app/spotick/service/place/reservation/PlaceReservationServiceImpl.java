package com.app.spotick.service.place.reservation;

import com.app.spotick.domain.dto.place.reservation.PlaceReservationTimeDto;
import com.app.spotick.domain.dto.place.reservation.PlaceReserveRegisterDto;
import com.app.spotick.domain.dto.place.reservation.ReservationRequestListDto;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceReservation;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.place.PlaceReservationStatus;
import com.app.spotick.repository.place.PlaceRepository;
import com.app.spotick.repository.place.reservation.PlaceReservationRepository;
import com.app.spotick.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
            User userProxy = userRepository.getReferenceById(userId);
            Place placeProxy = placeRepository.getReferenceById(placeReserveRegisterDto.getPlaceId());

            placeReservationRepository.save(PlaceReservation.builder()
                    .place(placeProxy)
                    .user(userProxy)
                    .checkIn(parseToLocalDateTime(placeReserveRegisterDto.getReservationCheckIn()))
                    .checkOut(parseToLocalDateTime(placeReserveRegisterDto.getReservationCheckOut()))
                    .content(placeReserveRegisterDto.getReservationContent())
                    .amount(placeReserveRegisterDto.getReservationAmount())
                    .visitors(placeReserveRegisterDto.getReservationVisitors())
                    .reservationStatus(PlaceReservationStatus.PENDING)
                    .notReviewing(false)
                    .build());
    }
    @Override
    public boolean isReservationAvailable(PlaceReserveRegisterDto placeReserveRegisterDto) {
        LocalDateTime checkIn = parseToLocalDateTime(placeReserveRegisterDto.getReservationCheckIn());
        LocalDateTime checkOut = parseToLocalDateTime(placeReserveRegisterDto.getReservationCheckOut());
        return !placeReservationRepository
                .isOverlappingReservation(placeReserveRegisterDto.getPlaceId(), checkIn,checkOut);
    }

    @Override
    public void updateNotReviewing(Long reservationId) {
        PlaceReservation foundReservation = placeReservationRepository.findById(reservationId).orElseThrow(
                NoSuchElementException::new
        );
        foundReservation.updateNotReviewing(true);
    }
    private LocalDateTime parseToLocalDateTime(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateString,formatter);
    }

    @Override
    public List<PlaceReservationTimeDto> findReservedTimes(Long placeId, String selectedDate) {

        LocalDateTime startTime = parseToLocalDateTime(selectedDate + " 00:00:00");
        LocalDateTime endTime = startTime.plusDays(1).plusHours(23);
        return placeReservationRepository.findReservedTimes(placeId,startTime,endTime);
    }

    @Override
    public Slice<ReservationRequestListDto> getReservationsOfPlace(Long placeId, Long userId, Pageable pageable) {
        return placeReservationRepository.findReservationsByPlaceIdAndUserIdSlice(placeId, userId, pageable);
    }

    @Override
    public void updateReservationStatus(Long reservationId, Long userId, PlaceReservationStatus status) {
        PlaceReservation foundReservation = placeReservationRepository.findByIdAndHost(reservationId, userId).orElseThrow(
                NoSuchElementException::new
        );

        foundReservation.updateStatus(status);
    }
}













