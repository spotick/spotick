package com.app.spotick.api.controller.ticket;

import com.app.spotick.api.response.CommonResponse;
import com.app.spotick.domain.dto.ticket.TicketGradeSaleInfoDto;
import com.app.spotick.service.ticket.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/ticket/api")
@RequiredArgsConstructor
public class TicketRestController {
    private final TicketService ticketService;

    @GetMapping("/getGrades")
    public ResponseEntity<CommonResponse<List<TicketGradeSaleInfoDto>>> getTicketGrades(@RequestParam("ticketId") Long ticketId,
                                                                                        @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<TicketGradeSaleInfoDto> ticketGrades = null;
        try {
            ticketGrades = ticketService.findTicketGrades(ticketId, date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(new CommonResponse<>(true, "조회 성공", ticketGrades));
    }

}
