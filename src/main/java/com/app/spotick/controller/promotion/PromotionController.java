package com.app.spotick.controller.promotion;

import com.app.spotick.domain.dto.promotion.PromotionRegisterDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.service.promotion.PromotionService;
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

@Slf4j
@Controller
@RequestMapping("/promotion")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    @GetMapping("/list")
    public void goToList(){

    }

    @GetMapping("/detail")
    public void goToDetail(){

    }

    @GetMapping("/register")
    public String promotionRegister(
            @ModelAttribute("promotionRegisterDto")PromotionRegisterDto promotionRegisterDto
            , Model model){
        return "promotion/register";
    }

    @PostMapping("/register")
    public String promotionRegister(@Valid PromotionRegisterDto promotionRegisterDto,
                                    BindingResult result,
                                    Model model,
                                    @AuthenticationPrincipal UserDetailsDto userDetailsDto){
        log.info("================================================================");
        log.info("들어옴");
        log.info(promotionRegisterDto.toString());
        log.info("================================================================");
        if (result.hasErrors()){
            return "promotion/register";
        }
        log.info(promotionRegisterDto.toString());

        try {
            promotionService.registerPromotion(promotionRegisterDto, 1L);
        } catch (IOException e) {
            e.printStackTrace();
            return  "promotion/register";
        }
        return "redirect:/prmotion/list";
    }
}
