package com.app.spotick.api.controller.admin;

import com.app.spotick.api.dto.response.CommonResponse;
import com.app.spotick.domain.type.post.PostStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminRestController {

//    private final AdminUserService adminUserService;
//    private final AdminPlaceService adminPlaceService;

    //  관리자 페이지 회원관리 전체 조회 및 페이징 처리(무한 스크롤)
    @GetMapping("/user/list")
    public ResponseEntity<CommonResponse> adminUserList(String keyword, Pageable pageable){
//        Slice<AdminUserListDto> adminUserListDto = adminUserService.userListWithSlice(keyword, pageable);

//        return ResponseEntity.ok(new CommonResponse(true, "목록 조회성공", adminUserListDto));
        return null;
    }

    //  관리자 페이지 장소글 전체 조회 및 페이징 처리(무한 스크롤)
    @GetMapping("/place/list")
    public ResponseEntity<CommonResponse<Map<String, Object>>> adminPlaceList(Pageable pageable){
//        Slice<AdminPlaceListDto> adminPlaceListDto = adminPlaceService.placeListWithSlice(pageable);
//
//        PostStatus[] values = PostStatus.values();
//        Map<String, Object> map = new HashMap<>();
//
//        map.put("postStatus",values);
//        map.put("list",adminPlaceListDto);
//
//        return ResponseEntity.ok(new ApiResponse<>(true, "목록 조회성공", map));
        return null;

    }


    //  관리자 페이지 장소글 이메일 조회 및 페이징 처리(무한 스크롤)
    @GetMapping("/place/email")
    public void adminPlaceEmailList(){
//        adminPlaceService.
    }
}