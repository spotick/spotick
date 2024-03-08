package com.app.spotick.api.controller.admin;

import com.app.spotick.api.dto.response.CommonResponse;
import com.app.spotick.domain.dto.admin.AdminPlaceListDto;
import com.app.spotick.domain.dto.admin.AdminUserListDto;
import com.app.spotick.domain.type.post.PostStatus;
import com.app.spotick.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admins/*")
public class AdminRestController {
    private final AdminService adminService;

    //  관리자 페이지 회원관리 전체 조회 및 페이징 처리(무한 스크롤)
    @GetMapping("/user/list")
    public ResponseEntity<Slice<AdminUserListDto>> adminUserList(
            @PageableDefault(page = 0,
                    size = 12, sort = "id",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        Slice<AdminUserListDto> adminUserListDto = adminService.findAdminUserList(pageable);
        return ResponseEntity.ok(adminUserListDto);
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
        map.put("list",adminPlaceListDto);
        map.put("postStatus", PostStatus.values());
        return ResponseEntity.ok(map);
    }


    //  관리자 페이지 장소글 이메일 조회 및 페이징 처리(무한 스크롤)
    @GetMapping("/place/email")
    public void adminPlaceEmailList() {
//        adminPlaceService.
    }
}

