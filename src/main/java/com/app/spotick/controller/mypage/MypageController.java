package com.app.spotick.controller.mypage;

import com.app.spotick.domain.dto.page.TicketPage;
import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.dto.place.PlaceManageListDto;
import com.app.spotick.domain.dto.place.PlaceReservationListDto;
import com.app.spotick.domain.dto.place.reservation.PlaceReservedNotReviewedDto;
import com.app.spotick.domain.dto.place.review.ContractedPlaceDto;
import com.app.spotick.domain.dto.place.review.PlaceReviewRegisterDto;
import com.app.spotick.domain.dto.place.review.MypageReviewListDto;
import com.app.spotick.domain.dto.ticket.TicketInfoDto;
import com.app.spotick.domain.dto.ticket.TicketManageListDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.domain.dto.user.UserProfileDto;
import com.app.spotick.domain.entity.place.PlaceReservation;
import com.app.spotick.domain.pagination.Pagination;
import com.app.spotick.domain.type.place.PlaceReservationStatus;
import com.app.spotick.domain.type.ticket.TicketRequestType;
import com.app.spotick.service.place.reservation.PlaceReservationService;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Objects;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {
    private final UserService userService;
    private final UserProfileFileService profileFileService;
    private final PlaceReservationService placeReservationService;
    private final RedisService redisService;

    /* ================================================유저정보수정=================================================== */
    @GetMapping("/user-info")
    public void goToUserInfo(Model model, @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        // 가려진 이메일 정보
        String maskedEmail = getMaskedEmail(userDetailsDto.getEmail());
        model.addAttribute("maskedEmail", maskedEmail);
        // 유저 프로필 정보
        UserProfileDto userProfileDto = userService.getUserProfile(userDetailsDto.getId());

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

    @PatchMapping("/updateNickName")
    @ResponseBody
    public ResponseEntity<String> updateNickName(@RequestParam("nickname") String nickname,
                                                 @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                                                 RedirectAttributes redirectAttributes) {

        // 검증
        if (nickname == null || nickname.length() < 2 || nickname.length() > 10) {
            return ResponseEntity.badRequest().body("닉네임은 최소 2자에서 최대 10자까지 가능합니다.");
        }

        userDetailsDto.updateNickName(nickname);
        userService.updateNickName(userDetailsDto.getId(), nickname);

        redirectAttributes.addFlashAttribute("successName", "닉네임이 수정되었습니다.");
        return ResponseEntity.ok().body("닉네임이 수정되었습니다.");
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
    public ResponseEntity<String> authenticateTel(@RequestParam("tel") String tel,
                                                  @RequestParam("code") String code,
                                                  @AuthenticationPrincipal UserDetailsDto userDetailsDto) {

        // 검증
        if (Objects.equals(redisService.getValues(tel), code)) {
            System.out.println("인증 성공");
            
            userService.updateTel(userDetailsDto.getId(), tel);

            redisService.deleteValues(tel);

            return ResponseEntity.ok().body("전화번호가 수정되었습니다");
        }

        return ResponseEntity.badRequest().body("잘못된 코드입니다.");
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
        Pagination<PlaceListDto> pagination = new Pagination<>(5, pageable, bookmarkedPlaces);

        model.addAttribute("placeDtoList", bookmarkedPlaces);
        model.addAttribute("pagination", pagination);
    }

    /* =================================================예약내역====================================================== */
    @GetMapping("/reservations")
    public void goToReservations(@RequestParam(value = "page", defaultValue = "1") int page,
                                 @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                                 Model model) {
        Pageable pageable = PageRequest.of(page - 1, 6);

        Page<PlaceReservationListDto> reservations = userService.findReservationsByUserId(userDetailsDto.getId(), pageable);
        Pagination<PlaceReservationListDto> pagination = new Pagination<>(5, pageable, reservations);

        model.addAttribute("reservationDtoList", reservations);
        model.addAttribute("pagination", pagination);
    }
    @GetMapping("/inquiries")
    public void goToInquiries() {
    }

    @GetMapping("/reviews")
    public RedirectView goToReviews() {
        return new RedirectView("/mypage/reviews/reviewable");
    }

    @GetMapping("/reviews/reviewable")
    public void goToReviewsReviewable(@ModelAttribute("reviewRegisterDto") PlaceReviewRegisterDto placeReviewRegisterDto,
                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                      @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                                      Model model) {

        Pageable pageable = PageRequest.of(page - 1, 6);

        Page<PlaceReservedNotReviewedDto> notReviewedList
                = userService.findPlacesNotReviewed(userDetailsDto.getId(), pageable);
        Pagination<PlaceReservedNotReviewedDto> pagination
                = new Pagination<>(5, pageable, notReviewedList);

        model.addAttribute("notReviewedList", notReviewedList);
        model.addAttribute("pagination", pagination);
    }

    @GetMapping("/reviews/wrote")
    public void goToReviewsWrote(@RequestParam(value = "page", defaultValue = "1") int page,
                                 @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                                 Model model) {
        Pageable pageable = PageRequest.of(page - 1, 5);

        Page<MypageReviewListDto> reviewedList
                = userService.findReviewedList(userDetailsDto.getId(), pageable);
        Pagination<MypageReviewListDto> pagination
                = new Pagination<>(5, pageable, reviewedList);

        model.addAttribute("reviewedList", reviewedList);
        model.addAttribute("pagination", pagination);
    }

    @GetMapping("/places")
    public void goToPlaces(@RequestParam(value = "page", defaultValue = "1") int page,
                           @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                           Model model) {
        Pageable pageable = PageRequest.of(page - 1, 6);

        Page<PlaceManageListDto> hostPlacesPage
                = userService.findHostPlacesPage(userDetailsDto.getId(), pageable);
        Pagination<PlaceManageListDto> pagination
                = new Pagination<>(5, pageable, hostPlacesPage);

        model.addAttribute("hostPlacesPage", hostPlacesPage);
        model.addAttribute("pagination", pagination);
    }

    @GetMapping("/places/inquiries/{placeId}")
    public String goToPlacesInquiryList(@PathVariable("placeId") Long placeId,
                                        @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                                        Model model) {

        ContractedPlaceDto placeDto = userService.findPlaceBriefly(placeId, userDetailsDto.getId()).orElseThrow(
                NoSuchElementException::new
        );

        model.addAttribute("placeDto", placeDto);

        return "/mypage/places/inquiries";
    }

    @GetMapping("/places/reservations/{placeId}")
    public String goToPlacesReservationList(@PathVariable("placeId") Long placeId,
                                            @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                                            Model model) {

        ContractedPlaceDto placeDto = userService.findPlaceBriefly(placeId, userDetailsDto.getId()).orElseThrow(
                NoSuchElementException::new
        );

        model.addAttribute("placeDto", placeDto);

        return "/mypage/places/reservations";
    }

    @GetMapping("/tickets")
    public void goToTickets(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "view", defaultValue = "all") String viewType,
                            @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                            Model model) {
        Pageable pageable = PageRequest.of(page - 1, 5);

        TicketRequestType ticketRequestType = TicketRequestType.valueOf(viewType);

        TicketPage ticketPage
                = userService.findHostTicketsPage(userDetailsDto.getId(), pageable, ticketRequestType);
        Pagination<TicketManageListDto> pagination
                = new Pagination<>(5, pageable, ticketPage.getPage());


        model.addAttribute("ticketPage", ticketPage);
        model.addAttribute("pagination", pagination);
        model.addAttribute("viewType", viewType);
    }

    @GetMapping("/tickets/{ticketId}/inquiries")
    public String goToTicketsInquiryList(@PathVariable("ticketId") Long ticketId,
                                         @AuthenticationPrincipal UserDetailsDto userDetailsDto,
                                         Model model) {

        TicketInfoDto ticketInfo = userService.findTicketInfo(ticketId, userDetailsDto.getId()).orElseThrow(
                NoSuchElementException::new
        );

        model.addAttribute("ticketInfo", ticketInfo);

        return "/mypage/tickets/inquiries";
    }

    private String getMaskedEmail(String email) {
        // 이메일 주소의 처음 4글자와 마지막 @ 이전 부분을 유지하고 나머지를 *로 마스킹
        return email.substring(0, 4) + "****@" + email.substring(email.indexOf('@') + 1);
    }

    private String getPath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
}
