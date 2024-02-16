package com.app.spotick.controller.main;

import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.service.place.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public String goToMain(Model model, @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                           @PageableDefault(page = 0,
                                   size = 10, sort = "id",
                                   direction = Sort.Direction.DESC
                           ) Pageable pageable) {
        Long userId = userDetailsDto == null ? null : userDetailsDto.getId();

        Slice<PlaceListDto> placeList = placeService.findPlaceListPagination(pageable, userId);

        model.addAttribute("placeList", placeList);
        return "place/list";
    }
}
