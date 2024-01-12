package com.app.spotick.controller.user;

import com.app.spotick.domain.dto.user.UserJoinDto;
import com.app.spotick.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/user/*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping("/login")
    public String userLogin(){
        return "user/login";
    }


    @GetMapping("/join")
    public String userJoin(){
        return "user/join";
    }

    @PostMapping("/join")
    public RedirectView userJoin(UserJoinDto userJoinDto){
        userService.join(userJoinDto);
        return new RedirectView("/user/login");
    }

    @GetMapping("/find")
    public String userFind(){
        return "user/find";
    }
}











