package com.app.spotick.controller.mypage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    @GetMapping("/user-info")
    public void goToUserInfo(){;}

    @GetMapping("/bookmarks")
    public void goToBookmarks(){;}

    @GetMapping("/reservations")
    public void goToReservations(){;}

    @GetMapping("/inquiries")
    public void goToInquiries(){;}

    @GetMapping("/reviews")
    public void goToReviews(){;}

    @GetMapping("/tickets")
    public void goToTickets(){;}

    @GetMapping("/tickets/inquiry-list")
    public void goToTicketsInquiryList(){;}

    @GetMapping("/places")
    public void goToPlaces(){;}

    @GetMapping("/places/inquiry-list")
    public void goToPlacesInquiryList(){;}

    @GetMapping("/places/reservation-list")
    public void goToPlacesReservationList(){;}
}
