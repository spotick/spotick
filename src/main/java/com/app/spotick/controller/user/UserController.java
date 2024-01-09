package com.app.spotick.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/*")
public class UserController {

    @GetMapping("/login")
    public String userLogin(){
        return "user/login";
    }

    @GetMapping("/join")
    public String userJoin(){
        return "user/join";
    }

    @GetMapping("/find")
    public String userFind(){
        return "user/find";
    }
}











