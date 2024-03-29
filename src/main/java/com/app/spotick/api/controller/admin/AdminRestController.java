package com.app.spotick.api.controller.admin;

import com.app.spotick.api.dto.admin.AdminPostApproveDto;
import com.app.spotick.api.dto.admin.AdminPostSearchDto;
import com.app.spotick.api.dto.admin.AdminUserAuthorityConfigDto;
import com.app.spotick.api.dto.admin.AdminUserSearchDto;
import com.app.spotick.api.dto.user.UserStatusDto;
import com.app.spotick.domain.dto.admin.AdminPostListDto;
import com.app.spotick.domain.dto.admin.AdminUserListDto;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.domain.type.user.UserStatus;
import com.app.spotick.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.app.spotick.domain.dto.enumdto.DisplayableEnumDto.getDisplayableDtoList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admins/*")
public class AdminRestController {
    private final AdminService adminService;

    //  관리자 페이지 회원관리 전체 조회 및 페이징 처리(무한 스크롤)
    @GetMapping("/user/list")
    public ResponseEntity<Map<String, Object>> adminUserList(
            @PageableDefault(page = 0,
                    size = 12, sort = "id",
                    direction = Sort.Direction.DESC
            ) Pageable pageable, AdminUserSearchDto userSearchDto) {
        Slice<AdminUserListDto> adminUserListDto = adminService.findAdminUserList(pageable, userSearchDto);
        Map<String, Object> map = new HashMap<>();
        map.put("slice", adminUserListDto);
        map.put("enumValues", getDisplayableDtoList(UserStatus.values()));
        return ResponseEntity.ok(map);
    }

    //  관리자 페이지 장소글 전체 조회 및 페이징 처리(무한 스크롤)
    @GetMapping("/place/list")
    public ResponseEntity<Map<String, Object>> adminPlaceList(
            @PageableDefault(page = 0,
                    size = 12, sort = "id",
                    direction = Sort.Direction.DESC
            ) Pageable pageable, AdminPostSearchDto placeSearchDto) {

        Slice<AdminPostListDto> adminPlaceListDto = adminService.findAdminPlaceList(pageable, placeSearchDto);
        Map<String, Object> map = new HashMap<>();
        map.put("slice", adminPlaceListDto);
        map.put("enumValues", getDisplayableDtoList(PostStatus.values()));
        return ResponseEntity.ok(map);
    }

    //  관리자 페이지 장소글 이메일 조회 및 페이징 처리(무한 스크롤)
    @GetMapping("/place/email")
    public void adminPlaceEmailList() {
//        adminPlaceService.
    }

    @PostMapping("/user/status/change")
    public ResponseEntity<Void> adminChangeUserStatus(@RequestBody List<UserStatusDto> statusDtos) {
        adminService.updateUsersStatus(statusDtos);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/place/approve")
    public ResponseEntity<Boolean> adminPlaceApproved(@RequestBody AdminPostApproveDto.Request approveDto) {
        adminService.approveOrRejectPlace(approveDto);
        return ResponseEntity.ok(approveDto.getIsApprove());
    }

    @PostMapping("/user/authority/config")
    public ResponseEntity<Void> adminAuthorityConfig(@RequestBody AdminUserAuthorityConfigDto configDto) {
        adminService.grantOrRevokeUserAuthority(configDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ticket/list")
    public ResponseEntity<Map<String, Object>> adminTicketList(
            @PageableDefault(page = 0,
                    size = 12, sort = "id",
                    direction = Sort.Direction.DESC
            ) Pageable pageable,AdminPostSearchDto ticketSearchDto) {

        Slice<AdminPostListDto> adminPlaceListDto = adminService.findAdminTicketList(pageable,ticketSearchDto);
        Map<String, Object> map = new HashMap<>();
        map.put("slice", adminPlaceListDto);
        map.put("enumValues", getDisplayableDtoList(PostStatus.values()));
        return ResponseEntity.ok(map);
    }

    @PostMapping("/ticket/approve")
    public ResponseEntity<Boolean> adminTicketApproved(@RequestBody AdminPostApproveDto.Request approveDto) {
        adminService.approveOrRejectTicket(approveDto);
        return ResponseEntity.ok(approveDto.getIsApprove());
    }


}




