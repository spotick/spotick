package com.app.spotick.service.notice;

import com.app.spotick.api.dto.notice.NoticeDto;
import com.app.spotick.domain.entity.notice.Notice;
import com.app.spotick.domain.type.notice.NoticeStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface NoticeService {

    void saveNotice(Notice notice);

    List<NoticeDto> getNoticeList(Long userId);

    void updateStatus(Long noticeId, Long userId, NoticeStatus noticeStatus);

    void getNoticeLines(@NotNull String noticeType, @NotNull Long userId, @Nullable String title, @Nullable String content);

}
