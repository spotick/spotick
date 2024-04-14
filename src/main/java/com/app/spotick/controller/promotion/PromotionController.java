package com.app.spotick.controller.promotion;

import com.app.spotick.domain.dto.promotion.PromotionRegisterDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.service.promotion.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/promotion")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    @GetMapping
    public String goToList(){
        return "promotion/list";
    }

    @GetMapping("/{id}")
    public String goToDetail(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetailsDto userDetailsDto){

        return "promotion/detail";
    }

    ///////////////////////////////////////////// 프로모션 등록 //////////////////////////////////////////////
    @GetMapping("/register")
    public String promotionRegister(@ModelAttribute("promotionRegisterDto")
                                        PromotionRegisterDto promotionRegisterDto){
        return "promotion/register";
    }

    @PostMapping("/register")
    public String promotionRegister(@Valid PromotionRegisterDto promotionRegisterDto,
                                    BindingResult result,
                                    @AuthenticationPrincipal UserDetailsDto userDetailsDto) throws IOException {
        promotionRegisterDto.setUserId(userDetailsDto.getId());

        System.out.println("promotionRegisterDto = " + promotionRegisterDto);

        if (result.hasErrors()) {
            return "/promotion/register";
        }

        Long id = promotionService.promotionBoardSave(promotionRegisterDto);

        return "redirect:/promotion/" + id;
    }
}
