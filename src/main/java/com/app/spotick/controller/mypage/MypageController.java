package com.app.spotick.controller.mypage;

import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.domain.dto.user.UserProfileDto;
import com.app.spotick.service.redis.RedisService;
import com.app.spotick.service.user.UserProfileFileService;
import com.app.spotick.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {
    private final UserService userService;
    private final UserProfileFileService profileFileService;
    private final RedisService redisService;


    /* ================================================유저정보수정=================================================== */
    @GetMapping("/user-info")
    public void goToUserInfo(Model model, @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        // 가려진 이메일 정보
        String maskedEmail = getMaskedEmail(userDetailsDto.getEmail());
        model.addAttribute("maskedEmail", maskedEmail);
        // 유저 프로필 정보
        UserProfileDto userProfileDto = userService.getUserProfile(userDetailsDto.getId());
        System.out.println("userProfileDto = " + userProfileDto);
        model.addAttribute("userProfile", userProfileDto);
    }

    @PostMapping("/updateDefaultImg")
    public RedirectView updateDefaultImg(@RequestParam("imgName") String imgName,
                                         @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                                         RedirectAttributes redirectAttributes) {

        userDetailsDto.updateProfileImage(imgName, null, null, true);
        profileFileService.updateDefaultImg(imgName, userDetailsDto.getId());
        redirectAttributes.addFlashAttribute("successProfile", "프로필 사진이 수정되었습니다.");
        return new RedirectView("/mypage/user-info");
    }

    @PostMapping("/updatePersonalImg")
    public RedirectView updatePersonalImg(@RequestParam("uuid") String uuid,
                                          @RequestParam("uploadFile") MultipartFile uploadFile,
                                          @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                                          RedirectAttributes redirectAttributes) {

        userDetailsDto.updateProfileImage(uploadFile.getOriginalFilename(), uuid, getPath(), false);
        profileFileService.updatePersonalImg(uploadFile.getOriginalFilename(), uuid, userDetailsDto.getId());

        redirectAttributes.addFlashAttribute("successProfile", "프로필 사진이 수정되었습니다.");
        return new RedirectView("/mypage/user-info");
    }

    @PostMapping("/updateNickName")
    public RedirectView updateNickName(@RequestParam("nickName") String nickName,
                                       @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                                       RedirectAttributes redirectAttributes) {

        // 검증
        if (nickName == null || nickName.length() < 2 || nickName.length() > 10) {
            redirectAttributes.addFlashAttribute("errorName", "닉네임은 최소 2자에서 최대 10자까지 가능합니다.");
            return new RedirectView("/mypage/user-info");
        }

        userDetailsDto.updateNickName(nickName);
        userService.updateNickName(userDetailsDto.getId(), nickName);

        redirectAttributes.addFlashAttribute("successName", "닉네임이 수정되었습니다.");
        return new RedirectView("/mypage/user-info");
    }

    @PostMapping("/authenticateTelStart")
    @ResponseBody
    public ResponseEntity<Void> sendAuthenticatingCode(@RequestParam("tel") String tel) {
        // 검증
        if (tel != null) {
            userService.sendAuthCodeToTel(tel);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/authenticateTel")
    @ResponseBody
    public ResponseEntity<Void> authenticateTel(@RequestParam("tel") String tel,
                                                @RequestParam("code") String code,
                                                @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                                                RedirectAttributes redirectAttributes) {
//        todo : 결과값을 화면으로 반환할 수 있도록 강구하여야 함.

        // 검증
        if (Objects.equals(redisService.getValues(tel), code)) {
            System.out.println("인증 성공");

            userService.updateTel(userDetailsDto.getId(), tel);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/mypage/user-info");

            return new ResponseEntity<>(headers, HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/changePassword")
    public RedirectView changePassword(@RequestParam("password") String password,
                                       @RequestParam("passwordCheck") String passwordCheck,
                                       @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                                       RedirectAttributes redirectAttributes) {
        String passwordRegex = "^(?=.*[a-zA-Z])(?=.*[0-9!@#$%^&*()-+=<>]).{6,15}$";

        if (password != null && Objects.equals(password, passwordCheck) && password.matches(passwordRegex)) {
            userService.updatePassword(userDetailsDto.getId(), password);

            redirectAttributes.addFlashAttribute("successPassword", "비밀번호가 수정되었습니다.");
            return new RedirectView("/mypage/user-info");
        }

        redirectAttributes.addFlashAttribute("errorPassword", "비밀번호 수정에 실패했습니다.");
        return new RedirectView("/mypage/user-info");
    }

    /* =================================================북마크====================================================== */
    @GetMapping("/bookmarks")
    public void goToBookmarks(@RequestParam(value = "page", defaultValue = "1") int page,
                              @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                              Model model) {
        Pageable pageable = PageRequest.of(page - 1, 6);

        Page<PlaceListDto> bookmarkedPlaces = userService.findBookmarkedPlacesByUserId(userDetailsDto.getId(), pageable);

        System.out.println("bookmarkedPlaces.getTotalPages() = " + bookmarkedPlaces.getTotalPages());
        System.out.println("pageable.getPageNumber() = " + pageable.getPageNumber());
        System.out.println("pageableSize = " + pageable.getPageSize());

        model.addAttribute("placeDtoList", bookmarkedPlaces);
    }

    /* =================================================예약내역====================================================== */
    @GetMapping("/reservations")
    public void goToReservations(@RequestParam(value = "page", defaultValue = "1") int page,
                                 @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                                 Model model) {
        Pageable pageable = PageRequest.of(page - 1, 6);

        model.addAttribute("reservationDtoList",
                userService.findReservationsByUserId(userDetailsDto.getId(), pageable));
    }

    @GetMapping("/inquiries")
    public void goToInquiries() {
    }

    @GetMapping("/reviews")
    public void goToReviews() {
    }

    @GetMapping("/tickets")
    public void goToTickets() {
    }

    @GetMapping("/tickets/inquiry-list")
    public void goToTicketsInquiryList() {
    }

    @GetMapping("/places")
    public void goToPlaces() {
    }

    @GetMapping("/places/inquiry-list")
    public void goToPlacesInquiryList() {
    }

    @GetMapping("/places/reservation-list")
    public void goToPlacesReservationList() {
    }

    private String getMaskedEmail(String email) {
        // 이메일 주소의 처음 4글자와 마지막 @ 이전 부분을 유지하고 나머지를 *로 마스킹
        return email.substring(0, 4) + "****@" + email.substring(email.indexOf('@') + 1);
    }

    private String getPath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
}
