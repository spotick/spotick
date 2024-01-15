package com.app.spotick.service.place;

import com.app.spotick.domain.dto.place.PlaceRegisterDto;

import java.io.IOException;

public interface PlaceService {

    void registerPlace(PlaceRegisterDto placeRegisterDto,Long userId) throws IOException;

}
