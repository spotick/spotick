package com.app.spotick.repository.place;

import com.app.spotick.domain.dto.place.PlaceListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlaceQDSLRepository {
//    메인화면에 뿌려줄 게시글 리스트
    List<PlaceListDto> findPlaceListPaging(Pageable pageable, Long userId);
}
