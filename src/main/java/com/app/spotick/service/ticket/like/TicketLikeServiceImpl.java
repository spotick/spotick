package com.app.spotick.service.ticket.like;

import com.app.spotick.domain.entity.compositePk.TicketLikeId;
import com.app.spotick.domain.entity.ticket.Ticket;
import com.app.spotick.domain.entity.ticket.TicketLike;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.repository.ticket.TicketRepository;
import com.app.spotick.repository.ticket.like.TicketLikeRepository;
import com.app.spotick.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketLikeServiceImpl implements TicketLikeService {
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final TicketLikeRepository ticketLikeRepository;

    @Override
    public void doLike(Long ticketId, Long userId) {
        Ticket tmpTicket = ticketRepository.getReferenceById(ticketId);
        User tmpUser = userRepository.getReferenceById(userId);

        TicketLikeId ticketLikeId = new TicketLikeId(ticketId, userId);

        TicketLike entity = TicketLike.builder()
                .id(ticketLikeId)
                .user(tmpUser)
                .ticket(tmpTicket)
                .build();

        ticketLikeRepository.save(entity);
    }

    @Override
    public void undoLike(Long ticketId, Long userId) {
        Ticket tmpTicket = ticketRepository.getReferenceById(ticketId);
        User tmpUser = userRepository.getReferenceById(userId);

        ticketLikeRepository.deleteByTicketAndUser(tmpTicket, tmpUser);
    }
}
