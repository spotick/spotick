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
    public void goToList(){

    }

    @GetMapping("/detail")
    public void goToDetail(){

    }

    @GetMapping("/register")
    public String goToRegister(
            @ModelAttribute("ticketRegisterDto")TicketRegisterDto ticketRegisterDto
            , Model model){
        return "ticket/register";
    }

    @PostMapping("/register")
    public String promotionRegister(@Valid TicketRegisterDto ticketRegisterDto,
                                    BindingResult result,
                                    Model model,
                                    @AuthenticationPrincipal UserDetailsDto userDetailsDto){
        log.info("================================================================");
        log.info("들어옴");
        log.info(ticketRegisterDto.toString());
        log.info("================================================================");
        if (result.hasErrors()){
            log.info("들어옴2");
            return "promotion/register";
        }
        log.info(ticketRegisterDto.toString());

        try {
            ticketService.registerTicket(ticketRegisterDto, 3L);
        } catch (IOException e) {
            e.printStackTrace();
            return  "ticket/register";
        }
        return "redirect:/ticket/list";
    }
}
