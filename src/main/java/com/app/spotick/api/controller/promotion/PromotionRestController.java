package com.app.spotick.api.controller.promotion;

import com.app.spotick.api.response.CommonResponse;
import com.app.spotick.domain.dto.promotion.PromotionListDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.domain.type.promotion.PromotionCategory;
import com.app.spotick.service.promotion.PromotionService;
import com.app.spotick.service.promotion.like.PromotionLikeService;
import com.app.spotick.util.type.PromotionSortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/promotion/api")
@RequiredArgsConstructor
public class PromotionRestController {
    private final PromotionService promotionService;
    private final PromotionLikeService promotionLikeService;

    @GetMapping("/list")
    public ResponseEntity<?> getList(@RequestParam("page") int page,
                                     @RequestParam(value = "category", required = false) PromotionCategory category,
                                     @RequestParam(value = "sort", required = false) PromotionSortType sortType,
                                     @RequestParam(value = "keyword", required = false) String keyword) {

        Pageable pageable = PageRequest.of(page, 12);

        Slice<PromotionListDto> contents = promotionService.getPromotionBoards(pageable, category, sortType, keyword);

        return new ResponseEntity<>(CommonResponse.builder()
                .success(true)
                .message("조회 성공")
                .data(contents)
                .build(), HttpStatus.OK
        );
    }

    @GetMapping("/list/{userId}/{promotionId}")
    public ResponseEntity<?> getListOfUser(@PathVariable Long userId,
                                           @PathVariable Long promotionId,
                                           @RequestParam("page") int page) {

        Pageable pageable = PageRequest.of(page, 3);

        Slice<PromotionListDto> contents = promotionService.getPromotionBoardsOfUser(pageable, userId, promotionId);

        return new ResponseEntity<>(CommonResponse.builder()
                .success(true)
                .message("조회 성공")
                .data(contents)
                .build(), HttpStatus.OK
        );
    }

    @GetMapping("/like")
    public boolean like(@RequestParam("status") boolean status,
                        @RequestParam("promotionId") Long promotionId,
                        @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        if (!status) {
            promotionLikeService.doLike(promotionId, userDetailsDto.getId());

            return true;
        } else {
            promotionLikeService.undoLike(promotionId, userDetailsDto.getId());

            return false;
        }
    }
}
