package com.app.spotick.repository.notice;

import com.app.spotick.api.dto.notice.NoticeDto;
import com.app.spotick.domain.entity.notice.Notice;
import com.app.spotick.domain.entity.user.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<Notice> findByIdAndUser(Long id, User user);

    @Query("""
                    select new com.app.spotick.api.dto.notice.NoticeDto(
                        n.id, n.title, n.content, n.link, n.noticeStatus, n.createdDate
                    )
                    from Notice n
                    where n.user.id = :userId and n.noticeStatus != 'DELETED'
                    order by n.id desc
            """)
    List<NoticeDto> findNoticeListByUserId(@Param("userId") Long userId);

}
