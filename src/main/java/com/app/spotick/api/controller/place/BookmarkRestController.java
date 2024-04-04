package com.app.spotick.api.controller.place;

import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.service.place.bookmark.PlaceBookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookmarkRestController {
    private final PlaceBookmarkService placeBookmarkService;

    /*
        북마크를 등록 혹은 삭제하는 쿼리문이며 결과값은
        등록될 시 => true 반환
        삭제될 시 => false 반환
     */
    @GetMapping("/bookmark")
    public boolean bookmark(@RequestParam("status") boolean status,
                            @RequestParam("placeId") Long placeId,
                            @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        // status가 true이면 이미 like 되어있는 상태 -> delete
        // status가 false이면 like가 되어있지 않은 상태 -> insert

        if (!status) {
            placeBookmarkService.bookmark(placeId, userDetailsDto.getId());

            return true;
        } else {
            placeBookmarkService.undoBookmark(placeId, userDetailsDto.getId());

            return false;
        }
    }

    /*
        해당 게시물이 북마크가 되어있는 지 확인하는 용도이며 결과값은
        등록되어있을 시 => true 반환
        삭제될 시 => false 반환

        이것을 사용하기보단 dto에서 값을 담아오는 것을 권장. 어쩔 수 없는 경우에만 사용할 것.
     */
    @GetMapping("/bookmarkCheck")
    public boolean bookmarkCheck(@RequestParam("placeId") Long placeId,
                                 @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        return placeBookmarkService.bookmarkCheck(placeId, userDetailsDto.getId());
    }
}
