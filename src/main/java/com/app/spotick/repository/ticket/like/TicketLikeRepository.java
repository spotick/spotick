package com.app.spotick.repository.ticket.like;

import com.app.spotick.domain.entity.ticket.Ticket;
import com.app.spotick.domain.entity.ticket.TicketLike;
import com.app.spotick.domain.entity.user.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketLikeRepository extends JpaRepository<TicketLike, Long> {

    // @Query 어노테이션으로 굳이 사용하여 삭제하는 이유 :
    // 일반적으로 Query어노테이션을 사용하지 않은 JPQL을 통한 delete쿼리는 조회를 한 뒤 삭제를 하게되는 과정을 거치게된다.
    // 삭제의 목적이 아주 명확한 경우에서는 그럴 필요가 전혀 없으므로(특히 대량의 데이터를 삭제할 경우에는 더더욱)
    // @Query @Modifying을 이용하여 영속성 컨텍스트를 거치지 않고 바로 삭제할 수 있다.
    // 즉, N+1문제를 해결하기 위함이다.
    @Modifying
    @Query("DELETE FROM TicketLike t WHERE t.ticket = :ticket AND t.user = :user")
    void deleteByTicketAndUser(@Param("ticket") Ticket ticket, @Param("user") User user);

}
