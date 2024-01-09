package com.app.spotick.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

//    상시 메인페이지는 장소리스트페이지로 함
//    /place/list 요청시에도 같은 페이지가 나와야 함.
    @RequestMapping
    public String goToMain() {
        return "place/list";
    }
}
