package com.app.spotick.service.notice;

import com.app.spotick.api.dto.notice.NoticeDto;
import com.app.spotick.domain.entity.notice.Notice;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.domain.type.notice.NoticeStatus;
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
    public void saveNotice(@NotNull String noticeType, @NotNull Long userId, @Nullable String title, @Nullable String content) {
        User tmpUser = userRepository.getReferenceById(userId);

        Notice notice;

        switch (noticeType) {
            case ("inquiryResponse"): {
                notice = Notice.builder()
                        .title("문의 답변")
                        .content("문의가 답변되었습니다. 지금 확인해보세요.")
                        .link("/mypage/inquiries")
                        .noticeStatus(NoticeStatus.UNREAD)
                        .user(tmpUser)
                        .build();
                break;
            }

            default: {
                notice = Notice.builder()
                        .title(title)
                        .content(content)
                        .noticeStatus(NoticeStatus.UNREAD)
                        .build();
            }
        }

        noticeRepository.save(notice);
    }
}
