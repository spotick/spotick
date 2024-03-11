package com.app.spotick.api.controller.admin;

import com.app.spotick.api.dto.admin.AdminUserSearchDto;
import com.app.spotick.api.dto.user.UserStatusDto;
import com.app.spotick.domain.dto.admin.AdminPlaceListDto;
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

import com.app.spotick.domain.dto.enumdto.DisplayableEnumDto;

import static com.app.spotick.domain.dto.enumdto.DisplayableEnumDto.getDisplayableDtoList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admins/*")
public class AdminRestController {
    private final AdminService adminService;

    //  관리자 페이지 회원관리 전체 조회 및 페이징 처리(무한 스크롤)
    @GetMapping("/user/list")
    public ResponseEntity<Map<String,Object>> adminUserList(
            @PageableDefault(page = 0,
                    size = 12, sort = "id",
                    direction = Sort.Direction.DESC
            ) Pageable pageable, AdminUserSearchDto userSearchDto) {
        Slice<AdminUserListDto> adminUserListDto = adminService.findAdminUserList(pageable,userSearchDto);
        Map<String,Object> map = new HashMap<>();
        map.put("slice",adminUserListDto);
        map.put("enumValues", getDisplayableDtoList(UserStatus.values()));
        return ResponseEntity.ok(map);
    }

    //  관리자 페이지 장소글 전체 조회 및 페이징 처리(무한 스크롤)
    @GetMapping("/place/list")
    public ResponseEntity<Map<String, Object>> adminPlaceList(
            @PageableDefault(page = 0,
                    size = 12, sort = "id",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        Slice<AdminPlaceListDto> adminPlaceListDto = adminService.findAdminPlaceList(pageable);
        Map<String,Object> map = new HashMap<>();
        map.put("slice",adminPlaceListDto);
        map.put("enumValues", getDisplayableDtoList(PostStatus.values()));
        return ResponseEntity.ok(map);
    }

    //  관리자 페이지 장소글 이메일 조회 및 페이징 처리(무한 스크롤)
    @GetMapping("/place/email")
    public void adminPlaceEmailList() {
//        adminPlaceService.
    }

    @PostMapping("/user/status/change")
    public ResponseEntity<Void> adminChangeUserStatus(@RequestBody List<UserStatusDto> statusDtos){
            adminService.updateUsersStatus(statusDtos);
        return ResponseEntity.ok().build();
    }


}

