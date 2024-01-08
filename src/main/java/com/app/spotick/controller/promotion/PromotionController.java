package com.app.spotick.controller.promotion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/promotion")
public class PromotionController {

    @GetMapping("/promotion-list")
    public void goToList(){

    }
}
