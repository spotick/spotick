package com.app.spotick.controller.main;

import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.service.place.PlaceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final PlaceService placeService;

//    상시 메인페이지는 장소리스트페이지로 함
//    /place/list 요청시에도 같은 페이지가 나와야 함.
    @RequestMapping
    public String goToMain(Model model,@AuthenticationPrincipal UserDetailsDto userDetailsDto){
        Long userId = userDetailsDto==null? null: userDetailsDto.getId();

        List<PlaceListDto> placeList = placeService.findPlaceListPagination(0, userId);
        model.addAttribute("placeList",placeList);
        return "/place/list";
    }
}
