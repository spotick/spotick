package com.app.spotick.controller.ticket;

import com.app.spotick.domain.dto.ticket.TicketDetailDto;
import com.app.spotick.domain.dto.ticket.TicketListDto;
import com.app.spotick.domain.dto.ticket.TicketRegisterDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.service.ticket.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.NoSuchElementException;

@Controller
@Slf4j
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @GetMapping("/list")
    public void goToList(@RequestParam(name = "page", defaultValue = "0") int page,
                         @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                         Model model) {
        Pageable pageable = PageRequest.of(page, 12);
        Long userId = userDetailsDto == null ? null : userDetailsDto.getId();

        Slice<TicketListDto> ticketList = ticketService.findTicketListPage(pageable, userId);

        model.addAttribute("ticketList", ticketList);
    }

    @GetMapping("/detail/{ticketId}")
    public String goToDetail(@PathVariable("ticketId") Long ticketId,
                             @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                             Model model) {

        Long userId = userDetailsDto == null ? null : userDetailsDto.getId();

        TicketDetailDto ticketDetail = ticketService.findTicketDetailById(ticketId, userId);

        model.addAttribute("ticketDetail", ticketDetail);

        return "ticket/detail";
    }

    @GetMapping("/register")
    public String goToRegister(@ModelAttribute("ticketRegisterDto") TicketRegisterDto ticketRegisterDto) {
        return "ticket/register";
    }

    @PostMapping("/register")
    public String ticketRegister(@Valid TicketRegisterDto ticketRegisterDto,
                                 BindingResult result,
                                 Model model,
                                 @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        ticketRegisterDto.setUserId(userDetailsDto.getId());

        System.out.println("ticketRegisterDto = " + ticketRegisterDto);

        if (result.hasErrors()) {
            return "/ticket/register";
        }

        try {
            ticketService.registerTicket(ticketRegisterDto, userDetailsDto.getId());
        } catch (IOException e) {
            log.error("Exception [Err_Msg]: {}", e.getMessage());
            model.addAttribute("registerError", "서비스 저장중 오류가 발생 했습니다. 다시 시도해 주세요.");
            return "/ticket/register";
        }

        return "redirect:/ticket/list";
    }
}
