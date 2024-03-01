package com.app.spotick.service.notice;

import com.app.spotick.api.dto.notice.NoticeDto;
import com.app.spotick.domain.entity.notice.Notice;
import com.app.spotick.domain.type.notice.NoticeStatus;
import com.app.spotick.domain.type.notice.NoticeType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface NoticeService {

    List<NoticeDto> getNoticeList(Long userId);

    void updateStatus(Long noticeId, Long userId, NoticeStatus noticeStatus);

    void saveNotice(@NotNull NoticeType noticeType, @NotNull Long userId);

}
