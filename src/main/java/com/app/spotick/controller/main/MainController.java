package com.app.spotick.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping
    public String goToMain() {
        return "place/detail";
    }
}
