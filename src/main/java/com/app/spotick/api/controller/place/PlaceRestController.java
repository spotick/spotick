package com.app.spotick.api.controller.place;

import com.app.spotick.domain.dto.place.PlaceDto;
import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.domain.entity.place.Place;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.service.place.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/place/api")
@RequiredArgsConstructor
public class PlaceRestController {
    private final PlaceService placeService;

    @PatchMapping("/disable/{placeId}")
    public ResponseEntity<String> disablePlace(@PathVariable("placeId") Long placeId,
                                               @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        Place foundPlace = placeService.findPlace(placeId, userDetailsDto.getId()).orElse(null);

        if (foundPlace == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("등록된 장소를 조회하는데 실패했습니다.<br>다시 시도해주세요.");
        }

        if (foundPlace.getPlaceStatus() != PostStatus.APPROVED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("해당 장소는 이미 활성화 되어있지 않은 상태입니다.");
        }

        placeService.rejectAllReservationRequests(placeId);

        placeService.updateStatus(placeId, PostStatus.DISABLED);

        return ResponseEntity.status(HttpStatus.OK)
                .body("해당 장소를 비활성화시켰습니다.");
    }

    @PatchMapping("/approved/{placeId}")
    public ResponseEntity<String> enablePlace(@PathVariable("placeId") Long placeId,
                                              @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        Place foundPlace = placeService.findPlace(placeId, userDetailsDto.getId()).orElse(null);

        if (foundPlace == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("등록된 장소를 조회하는데 실패했습니다.<br>다시 시도해주세요.");
        }

        if (foundPlace.getPlaceStatus() != PostStatus.DISABLED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("비활성화된 장소만 활성화로 전환 할 수 있습니다.");
        }

        placeService.updateStatus(placeId, PostStatus.APPROVED);

        return ResponseEntity.status(HttpStatus.OK)
                .body("해당 장소를 활성화시켰습니다. 이제부터 대여가 가능합니다.");
    }

    @PatchMapping("/delete/{placeId}")
    public ResponseEntity<String> softDeletePlace(@PathVariable("placeId") Long placeId,
                                                  @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        Place foundPlace = placeService.findPlace(placeId, userDetailsDto.getId()).orElse(null);

        if (foundPlace == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("등록된 장소를 조회하는데 실패했습니다.<br>다시 시도해주세요.");
        }

        placeService.rejectAllReservationRequests(placeId);

        placeService.updateStatus(placeId, PostStatus.DELETED);

        return ResponseEntity.status(HttpStatus.OK)
                .body("해당 장소를 삭제했습니다.");
    }

    @GetMapping("/list")
    public ResponseEntity<Slice<PlaceListDto>> placeList(@AuthenticationPrincipal UserDetailsDto userDetailsDto,
                            @PageableDefault(page =0,
                                    size = 12, sort = "id",
                                    direction = Sort.Direction.DESC
                            ) Pageable pageable){
        Long userId = userDetailsDto==null? null: userDetailsDto.getId();
        Slice<PlaceListDto> placeList = placeService.findPlaceListPagination(pageable,userId);
        return ResponseEntity.ok(placeList);
    }

}
