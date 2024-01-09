package com.app.spotick.controller.ticket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    @GetMapping("/list")
    public void goToList(){

    }

    @GetMapping("/detail")
    public void goToDetail(){

    }

    @GetMapping("/register")
    public void goToRegister(){

    }
}
