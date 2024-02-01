package com.app.spotick.controller.mypage;

import com.app.spotick.domain.dto.place.PlaceListDto;
import com.app.spotick.domain.dto.place.PlaceManageListDto;
import com.app.spotick.domain.dto.place.PlaceReservationListDto;
import com.app.spotick.domain.dto.place.reservation.PlaceReservedNotReviewedDto;
import com.app.spotick.domain.dto.place.review.PlaceReviewRegisterDto;
import com.app.spotick.domain.dto.place.review.MypageReviewListDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.domain.dto.user.UserProfileDto;
import com.app.spotick.domain.entity.place.PlaceReservation;
import com.app.spotick.domain.pagination.Pagination;
import com.app.spotick.domain.type.place.PlaceReservationStatus;
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

    @GetMapping("/reservation/{reservationId}/cancel")
    @ResponseBody
    public ResponseEntity<String> cancelReservation(@PathVariable("reservationId") Long reservationId,
                                                    @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        if (reservationId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("잘못된 요청입니다.");
        }

        // reservationId검증: userId 동일한지 검증 필요 -> 시간정보 찾아와서 checkIn시간 체크하여 지나지 않았는지 검증 -> accepted 검증(아닐 시 취소 불가능)
        PlaceReservation reservation = placeReservationService
                .findReservationByIdAndUser(reservationId, userDetailsDto.getId()).orElse(null);

        if (reservation == null) {
            // 예약 정보를 찾을 수 없을 시
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("예약 정보를 찾을 수 없습니다.");

        }

        if (!Objects.equals(reservation.getUser().getId(), userDetailsDto.getId())) {
            // 조회를 하여도 user의 정보가 해당 로그인 유저와 다를 시
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("예약 정보에 접근할 권한이 없습니다.");

        }

        if (reservation.getCheckIn().isBefore(LocalDateTime.now())) {
            // checkIn시간이 현재 시간보다 이전일 경우, 이미 승인된 예약일 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("예약 시간이 지나간 예약은<br>취소할 수 없습니다.");

        }

        if (reservation.getReservationStatus().equals(PlaceReservationStatus.APPROVED)) {
            // 승인된 예약은 취소 불가
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("이미 승인된 예약은<br>취소할 수 없습니다.");
        }

        placeReservationService.cancelReservation(reservationId);

        return ResponseEntity.status(HttpStatus.OK)
                .body("예약이 취소되었습니다.");
    }

    @GetMapping("/reservation/{reservationId}/delete")
    @ResponseBody
    public ResponseEntity<String> deleteReservation(@PathVariable("reservationId") Long reservationId,
                                                    @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        if (reservationId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("잘못된 요청입니다.");
        }

        // reservationId검증: userId 동일한지 검증 필요 -> 시간정보 찾아와서 checkOut시간 체크하여 지나갔는지 검증 -> accepted, rejected, cancled아닐 시 삭제 불가.
        PlaceReservation reservation = placeReservationService
                .findReservationByIdAndUser(reservationId, userDetailsDto.getId()).orElse(null);

        if (reservation == null) {
            // 예약 정보를 찾을 수 없을 시
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("예약 정보를 찾을 수 없습니다.");

        }

        if (!Objects.equals(reservation.getUser().getId(), userDetailsDto.getId())) {
            // 조회를 하여도 user의 정보가 해당 로그인 유저와 다를 시
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("예약 정보에 접근할 권한이 없습니다.");

        }

        if (reservation.getCheckOut().isAfter(LocalDateTime.now())) {
            // checkIn시간이 현재 시간보다 이전일 경우, 이미 승인된 예약일 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("예약시간이 지나지 않은 예약은<br>삭제할 수 없습니다.");

        }

        if (reservation.getReservationStatus().equals(PlaceReservationStatus.PENDING)
                || reservation.getReservationStatus().equals(PlaceReservationStatus.WAITING_PAYMENT)) {
            // 예약이 해지되지 못하고 유효한 상태일 시 삭제불가
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("예약이 해지되어있지 않습니다.<br>예약을 취소하고 삭제를 시도하여주십시오.");
        }

        placeReservationService.deleteReservation(reservationId);

        return ResponseEntity.status(HttpStatus.OK)
                .body("예약내역이 삭제되었습니다.");
    }

    @GetMapping("/inquiries")
    public void goToInquiries() {
    }

    @GetMapping("/reviews")
    public String goToReviews() {
        return "redirect:/mypage/reviews/reviewable";
    }

    @GetMapping("/reviews/reviewable")
    public void goToReviewsReviewable(@ModelAttribute("reviewRegisterDto")PlaceReviewRegisterDto placeReviewRegisterDto,
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

    @GetMapping("/tickets")
    public void goToTickets() {
    }

    @GetMapping("/tickets/inquiry-list")
    public void goToTicketsInquiryList() {
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
