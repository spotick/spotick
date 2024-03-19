package com.app.spotick.api.controller.notice;

import com.app.spotick.api.response.CommonResponse;
import com.app.spotick.api.dto.notice.NoticeDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice/api")
@RequiredArgsConstructor
public class NoticeRestController {
    private final NoticeService noticeService;

    @GetMapping("/list")
    public ResponseEntity<CommonResponse<List<NoticeDto>>> checkNoticeList(@AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        List<NoticeDto> contents = noticeService.getNoticeList(userDetailsDto.getId());

        return ResponseEntity.ok(
                CommonResponse.<List<NoticeDto>>builder()
                .success(true)
                .message("조회 성공")
                .data(contents)
                .build()
        );
    }

    @PatchMapping("/updateStatus")
    public ResponseEntity<Void> updateStatus(@RequestBody NoticeDto.RequestStatus requestStatus,
                                             @AuthenticationPrincipal UserDetailsDto userDetailsDto) {

        try {
            noticeService.updateStatus(requestStatus.getNoticeId(), userDetailsDto.getId(), requestStatus.getNoticeStatus());

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
