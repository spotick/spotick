package com.app.spotick.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/user")
    public String adminUser(){

        return "admin/adminUser";
    }

    @GetMapping("/place")
    public String adminBoard(){

        return "admin/adminPlace";
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
