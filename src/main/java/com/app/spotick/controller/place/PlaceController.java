package com.app.spotick.controller.place;

import com.app.spotick.domain.dto.place.PlaceRegisterDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@Controller
@RequestMapping("/place/*")
public class PlaceController {

    @GetMapping("/detail")
    public String placeDetail(){
        return "place/detail";
    }

    @GetMapping("/list")
    public String placeList(){

        return "place/list";
    }

    @GetMapping("/register")
    public String placeRegister(@ModelAttribute PlaceRegisterDto placeRegisterDto){
        return "place/register";
    }

    @PostMapping("/register")
    public RedirectView placeRegister(PlaceRegisterDto placeRegisterDto, HttpServletRequest req){
        log.info("{}",placeRegisterDto);
        log.info("{}",placeRegisterDto.getPlaceFiles().size());
        return new RedirectView("/place/list");
    }

    @GetMapping("/reserve")
    public String placeReserve(){
        return "place/reserve";
    }
}










