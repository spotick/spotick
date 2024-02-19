package com.app.spotick.service.place;

import com.app.spotick.domain.dto.place.*;
import com.app.spotick.domain.dto.place.PlaceEditDto;
import com.app.spotick.domain.dto.place.file.PlaceFileDto;
import com.app.spotick.domain.dto.place.reservation.PlaceReserveBasicInfoDto;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.place.PlaceFile;
import com.app.spotick.domain.entity.place.PlaceReservation;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.place.PlaceReservationStatus;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.repository.place.PlaceRepository;
import com.app.spotick.repository.place.file.PlaceFileRepository;
import com.app.spotick.repository.place.reservation.PlaceReservationRepository;
import com.app.spotick.repository.user.UserRepository;
import com.app.spotick.service.place.file.PlaceFileService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    @PersistenceContext
    private final EntityManager em;

    private final PlaceRepository placeRepository;
    private final PlaceFileRepository placeFileRepository;
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
    public Slice<PlaceListDto> findPlaceListPagination(Pageable pageable, Long userId) {
        return placeRepository.findPlaceListPaging(pageable,userId);
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
    public Optional<PlaceEditDto> findPlaceInfo(Long placeId, Long userId) {
        return placeRepository.findPlaceInfoByPlaceIdAndUserId(placeId, userId);
    }

    @Override
    public void updatePlace(PlaceEditDto placeEditDto, Long userId) throws IOException {
        User tmpUser = userRepository.getReferenceById(userId);

        Place foundPlace = placeRepository.findByIdAndUser(placeEditDto.getPlaceId(), tmpUser).orElseThrow(
                NoSuchElementException::new
        );
        // 기존의 장소정보는 status만 교체한다.
        foundPlace.setStatus(PostStatus.MODIFICATION_REQUESTED);


        // 업데이트된 장소정보는 통째로 새로 등록시킨다.
        Place updatedPlace = placeEditDto.toEntity();
        updatedPlace.setHost(tmpUser);

        Place newPlace = placeRepository.save(updatedPlace);
        System.out.println("newPlace = " + newPlace);

        // 새로 등록된 장소에 사진정보도 옮겨준다.
        List<PlaceFileDto> files = placeFileRepository.findPlaceFilesByPlaceIdNotFileIds(
                foundPlace.getId(),
                placeEditDto.getSaveFileIdList()
        );
        System.out.println("files = " + files);

        List<PlaceFile> copiedFiles = new ArrayList<>();
        files.forEach(file -> {
            PlaceFile copiedFile = file.toEntity();
            copiedFile.setId(null);
            copiedFile.setPlace(newPlace);
            copiedFiles.add(copiedFile);
        });

        System.out.println("copiedFiles = " + copiedFiles);

        placeFileRepository.saveAll(copiedFiles);


        List<MultipartFile> placeFiles = placeEditDto.getPlaceNewFiles();
        // 새파일이 비어있지 않은 경우에만 처리
        if (placeFiles.stream().anyMatch(file -> file.getSize() > 0)) {
            System.out.println("placeFiles = " + placeFiles);
            placeFileService.registerAndSavePlaceFile(placeFiles, newPlace);
        }
    }
}















