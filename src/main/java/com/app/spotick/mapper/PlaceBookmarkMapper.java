package com.app.spotick.mapper;

import com.app.spotick.domain.dto.place.PlaceListDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlaceBookmarkMapper {
    public List<PlaceListDto> selectBookmarkedPlacesByUserId(Long userId);
}
