package com.app.spotick.service.notice;

import com.app.spotick.api.dto.notice.NoticeDto;
import com.app.spotick.domain.entity.notice.Notice;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.notice.NoticeStatus;
import com.app.spotick.domain.type.notice.NoticeType;
import com.app.spotick.repository.notice.NoticeRepository;
import com.app.spotick.repository.user.UserRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NoticeDto> getNoticeList(Long userId) {
        return noticeRepository.findNoticeListByUserId(userId);
    }

    @Override
    public void updateStatus(Long noticeId, Long userId, NoticeStatus noticeStatus) {
        User tmpUser = userRepository.getReferenceById(userId);

        Notice foundNotice = noticeRepository.findByIdAndUser(noticeId, tmpUser).orElseThrow(
                NoSuchFieldError::new
        );

        foundNotice.setStatus(noticeStatus);
    }

    @Override
    public void saveNotice(@NotNull NoticeType noticeType, @NotNull Long recipientId) {
        User recipient = userRepository.getReferenceById(recipientId);

        Notice notice = Notice.builder()
                        .title(noticeType.getTitle())
                        .content(noticeType.getContent())
                        .link(noticeType.getLink())
                        .noticeStatus(NoticeStatus.UNREAD)
                        .user(recipient)
                        .build();

        noticeRepository.save(notice);
    }
}
