package com.app.spotick.service.place;

import com.app.spotick.domain.dto.place.PlaceDetailDto;
import com.app.spotick.domain.dto.place.PlaceDto;
import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.dto.place.PlaceRegisterDto;
import com.app.spotick.domain.dto.place.reservation.PlaceReserveBasicInfoDto;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceReservation;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.place.PlaceReservationStatus;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.repository.place.PlaceRepository;
import com.app.spotick.repository.place.reservation.PlaceReservationRepository;
import com.app.spotick.repository.user.UserRepository;
import com.app.spotick.service.place.file.PlaceFileService;
import com.app.spotick.util.type.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;
    private final PlaceReservationRepository placeReservationRepository;
    private final UserRepository userRepository;
    private final PlaceFileService placeFileService;
    private final int PAGE_SIZE = 12;

    @Override
    public void registerPlace(PlaceRegisterDto placeRegisterDto, Long userId) throws IOException {
        User host = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원"));
        Place place = placeRegisterDto.toEntity();
        place.setHost(host);

        place = placeRepository.save(place);
//        저장된 장소로 사진도 저장해야함
        List<MultipartFile> placeFiles = placeRegisterDto.getPlaceFiles();

        placeFileService.registerAndSavePlaceFile(placeFiles, place);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<PlaceListDto> findPlaceListPagination(Pageable pageable, Long userId, SortType sortType) {
        return placeRepository.findPlaceListPaging(pageable, userId, sortType);
    }

    @Override
    public PlaceDetailDto findPlaceDetailById(Long placeId, Long userId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 장소 게시글"));

        place.increaseViewCount();

        return placeRepository.findPlaceDetailById(placeId, userId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 장소 게시글"));
    }

    @Override
    @Transactional(readOnly = true)
    public PlaceReserveBasicInfoDto findPlaceReserveDefaultInfo(Long placeId) {
        return placeRepository.findPlaceReserveBasicInfo(placeId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 장소 게시글"));
    }

    @Override
    public Optional<Place> findPlace(Long placeId, Long userId) {
        User tmpUser = userRepository.getReferenceById(userId);

        return placeRepository.findByIdAndUser(placeId, tmpUser);
    }

    @Override
    public void updateStatus(Long placeId, PostStatus postStatus) {
        Place foundPlace = placeRepository.findById(placeId).orElseThrow(
                NoSuchElementException::new
        );

        foundPlace.setPlaceStatus(postStatus);
    }

    @Override
    public void rejectAllReservationRequests(Long placeId) {
        Place tmpPlace = placeRepository.getReferenceById(placeId);

        List<PlaceReservation> foundPlaces = placeReservationRepository.findAllByPlace(tmpPlace);

        foundPlaces.forEach(foundPlace -> {
            foundPlace.updateStatus(PlaceReservationStatus.REJECTED);
        });
    }

    @Override
    public Optional<PlaceDto> findPlaceInfo(Long placeId, Long userId) {
        return placeRepository.findPlaceInfo(placeId, userId);
    }
}















