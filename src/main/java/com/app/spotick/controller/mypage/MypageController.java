package com.app.spotick.controller.mypage;

import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.domain.dto.user.UserProfileDto;
import com.app.spotick.domain.entity.user.User;
import com.app.spotick.service.user.UserProfileFileService;
import com.app.spotick.service.user.UserService;
import com.app.spotick.service.user.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {
    private final UserService userService;
    private final UserProfileFileService profileFileService;


    @GetMapping("/user-info")
    public void goToUserInfo(Model model, @AuthenticationPrincipal UserDetailsDto userDetailsDto) {

        System.out.println("userDetailsDto = " + userDetailsDto);

        String maskedEmail = getMaskedEmail(userDetailsDto.getEmail());
        model.addAttribute("maskedEmail", maskedEmail);

        Optional<UserProfileDto> userProfile = userService.getUserProfile(userDetailsDto.getId());
        System.out.println("userProfile = " + userProfile);
        model.addAttribute("userProfile", userProfile.get());
    }

    @PostMapping("/updateDefaultImg")
    public RedirectView updateDefaultImg(@RequestParam("imgName") String imgName, @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        System.out.println("imgName = " + imgName);

        profileFileService.updateDefaultImg(imgName, userDetailsDto.getId());

        return new RedirectView("/mypage/user-info");
    }

    @PostMapping("/updatePersonalImg")
    public RedirectView updatePersonalImg(@RequestParam("uuid") String uuid,
                                          @RequestParam("uploadFile") MultipartFile uploadFile,
                                          @AuthenticationPrincipal UserDetailsDto userDetailsDto) {

        profileFileService.updatePersonalImg(uploadFile.getOriginalFilename(), uuid, getPath(), userDetailsDto.getId());

        return new RedirectView("/mypage/user-info");
    }

    @GetMapping("/bookmarks")
    public void goToBookmarks(){}

    @GetMapping("/reservations")
    public void goToReservations(){}

    @GetMapping("/inquiries")
    public void goToInquiries(){}

    @GetMapping("/reviews")
    public void goToReviews(){}

    @GetMapping("/tickets")
    public void goToTickets(){}

    @GetMapping("/tickets/inquiry-list")
    public void goToTicketsInquiryList(){}

    @GetMapping("/places")
    public void goToPlaces(){}

    @GetMapping("/places/inquiry-list")
    public void goToPlacesInquiryList(){}

    @GetMapping("/places/reservation-list")
    public void goToPlacesReservationList(){}

    private String getMaskedEmail(String email) {
        // 이메일 주소의 처음 4글자와 마지막 @ 이전 부분을 유지하고 나머지를 *로 마스킹
        return email.substring(0, 4) + "****@" + email.substring(email.indexOf('@') + 1);
    }

    private String getPath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
}
