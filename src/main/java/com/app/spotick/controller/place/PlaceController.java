package com.app.spotick.controller.place;

import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.dto.place.PlaceRegisterDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.service.place.PlaceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/place/*")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping("/detail")
    public String placeDetail(){
        return "place/detail";
    }

    @GetMapping("/list")
    public String placeList(Model model,@AuthenticationPrincipal UserDetailsDto userDetailsDto){
        Long userId = userDetailsDto==null? null: userDetailsDto.getId();

        Page<PlaceListDto> placeList = placeService.findPlaceListPagination(0,userId);
        model.addAttribute("placeList",placeList);
        return "place/list";
    }

    @GetMapping("/register")
    public String placeRegister(@ModelAttribute PlaceRegisterDto placeRegisterDto){
        return "place/register";
    }

    @PostMapping("/register")
    public String placeRegister(@Valid PlaceRegisterDto placeRegisterDto,
                                      BindingResult result,
                                @AuthenticationPrincipal UserDetailsDto userDetailsDto){

        if (result.hasErrors()){
            return "place/register";
        }

        try {
            placeService.registerPlace(placeRegisterDto,userDetailsDto.getId());
        } catch (IOException e) {
            e.printStackTrace();
            return  "place/register";
        }

        return "redirect:/place/list";
    }

    @GetMapping("/reserve")
    public String placeReserve(){
        return "place/reserve";
    }
}










