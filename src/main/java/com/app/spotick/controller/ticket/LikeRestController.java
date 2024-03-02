package com.app.spotick.controller.ticket;

import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.service.ticket.like.TicketLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeRestController {
    private final TicketLikeService ticketLikeService;

    /*
        티켓 -> 좋아요를 등록 혹은 삭제하는 쿼리문이며 결과값은
        등록될 시 => true 반환
        삭제될 시 => false 반환

        status : 현재 좋아요 상태에 따른 결과값 처리이다. true일 시 좋아요가 되어있는 상태 / false일 시 좋아요가 되어있지 않은 상태
     */
    @GetMapping("/like")
    public boolean doLike(@RequestParam("status") boolean status,
                          @RequestParam("ticketId") Long ticketId,
                          @AuthenticationPrincipal UserDetailsDto userDetailsDto) {

        // status가 true이면 이미 like 되어있는 상태 -> delete
        // status가 false이면 like가 되어있지 않은 상태 -> insert

        if (!status) {
            ticketLikeService.doLike(ticketId, userDetailsDto.getId());

            return true;
        } else {
            ticketLikeService.undoLike(ticketId, userDetailsDto.getId());

            return false;
        }
    }
}
