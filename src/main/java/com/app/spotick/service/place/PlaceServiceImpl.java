package com.app.spotick.service.place;

import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.dto.place.PlaceRegisterDto;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.repository.place.PlaceRepository;
import com.app.spotick.repository.user.UserRepository;
import com.app.spotick.service.place.file.PlaceFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService{
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final PlaceFileService placeFileService;
    private final int PAGE_SIZE = 12;

    @Override
    public void registerPlace(PlaceRegisterDto placeRegisterDto,Long userId) throws IOException {
        User host = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원"));
        Place place = placeRegisterDto.toEntity();
        place.setHost(host);

        place = placeRepository.save(place);
//        저장된 장소로 사진도 저장해야함
        List<MultipartFile> placeFiles = placeRegisterDto.getPlaceFiles();

        placeFileService.registerAndSavePlaceFile(placeFiles,place);
    }

    @Override
    public Page<PlaceListDto> findPlaceListPagination(int pageRequest) {
        return placeRepository.findPlaceListPaging(PageRequest.of(pageRequest,PAGE_SIZE));
    }
}















