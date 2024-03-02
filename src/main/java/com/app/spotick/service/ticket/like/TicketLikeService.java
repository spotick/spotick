package com.app.spotick.service.ticket.like;

public interface TicketLikeService {

    void doLike(Long ticketId, Long userId);

    void undoLike(Long ticketId, Long userId);

}
