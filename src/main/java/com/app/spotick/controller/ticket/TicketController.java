package com.app.spotick.controller.ticket;

import com.app.spotick.domain.dto.promotion.PromotionRegisterDto;
import com.app.spotick.domain.dto.ticket.TicketRegisterDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.service.ticket.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@Slf4j
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @GetMapping("/list")
    public void goToList() {

    }

    @GetMapping("/detail")
    public void goToDetail() {

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
            e.printStackTrace();
            model.addAttribute("registerError", "서비스 저장중 오류가 발생 했습니다. 다시 시도해 주세요.");
            return "/ticket/register";
        }

        return "redirect:/ticket/list";
    }
}
