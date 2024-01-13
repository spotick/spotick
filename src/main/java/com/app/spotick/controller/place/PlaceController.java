package com.app.spotick.controller.place;

import com.app.spotick.domain.dto.user.UserDetailsDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String placeRegister(){
        return "place/register";
    }

    @GetMapping("/reserve")
    public String placeReserve(){
        return "place/reserve";
    }
}










