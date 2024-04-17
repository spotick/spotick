package com.app.spotick.domain.entity.ticket;

import com.app.spotick.domain.base.Period;
import com.app.spotick.domain.entity.compositePk.TicketLikeId;
import com.app.spotick.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Entity @Table(name = "TBL_TICKET_LIKE")
@Getter @ToString(callSuper = true) @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketLike extends Period implements Persistable<TicketLikeId> {
    // Persistable<>은 새로운 엔티티 여부를 판단하는 기준점(isNew())을 사용자가 직접 오버라이딩 함으로써 사용되어진다.
    // (isNew()를 재정의 하지 않을 시, @Id필드가 null인지를 기준으로 새로운 엔티티인지 아닌지를 구분하게 된다.)

    // 현재 이 엔티티와 같이 복합키인 경우엔 insert를 하게 될 시 Id값이 null이 되어질 수 없으므로
    // JPA Repository의 save메소드를 사용시 select를 하고 난뒤 insert를 하게 되는 문제가 생기게 된다(N+1 문제).

    // 이 문제를 타개 하기 위해서 Persistable을 가져와 isNew메소드로 null값이 될 수 밖에 없는 createdDate필드를 null체크하면
    // 이 필드를 기준점으로 새로운 엔티티인지 아닌지 여부를 판가름 할 수 있다. 이로써 N+1문제를 해결 할 수 있다.
    // 다만, 현 사용법은 모든 상황에서 통용되어질 수 있는 해결책은 아니다는 점을 잘 인지 하여야 한다.


    @EmbeddedId
    private TicketLikeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_ID", insertable = false, updatable = false)
    private Ticket ticket;

    @Builder
    public TicketLike(TicketLikeId id, User user, Ticket ticket) {
        this.id = id;
        this.user = user;
        this.ticket = ticket;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
}
