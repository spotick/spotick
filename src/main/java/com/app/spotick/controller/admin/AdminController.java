package com.app.spotick.controller.admin;

import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.domain.type.user.UserStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

import static com.app.spotick.domain.dto.enumdto.DisplayableEnumDto.getDisplayableDtoList;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/user")
    public String adminUser(Model model){
        model.addAttribute("enumValues",getDisplayableDtoList(UserStatus.values()));
        return "admin/adminUser";
    }

    @GetMapping("/place")
    public String adminBoard(Model model){
        model.addAttribute("enumValues",getDisplayableDtoList(PostStatus.values()));
        return "admin/adminPlace";
    }
    @GetMapping("/ticket")
    public String adminTicket(Model model){
        model.addAttribute("enumValues",getDisplayableDtoList(PostStatus.values()));
        return "admin/adminTicket";
    }

    @GetMapping("/reply")
    public String adminReply(){

        return "admin/adminReply";
    }

    @GetMapping("/pay")
    public String adminPay(){

        return "admin/adminPayment";
    }

    @GetMapping("/approval")
    public String adminApproval(){

        return "admin/adminApproval";
    }
}
