package com.app.spotick.api.controller.mypage;

import com.app.spotick.api.response.CommonResponse;
import com.app.spotick.domain.dto.promotion.FileDto;
import com.app.spotick.domain.dto.user.UserDetailsDto;
import com.app.spotick.service.redis.RedisService;
import com.app.spotick.service.user.UserProfileFileService;
import com.app.spotick.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/mypage/api")
@RequiredArgsConstructor
@Slf4j
public class MypageRestController {
    private final UserService userService;
    private final UserProfileFileService profileFileService;
    private final RedisService redisService;

    @PatchMapping("/updateDefaultImg")
    public ResponseEntity<CommonResponse<?>> updateDefaultImg(@RequestParam("imgName") String imgName,
                                                              @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        try {
            profileFileService.updateDefaultImg(imgName, userDetailsDto.getId());
            userDetailsDto.updateProfileImage(imgName, null, null, true);

            return new ResponseEntity<>(CommonResponse.builder()
                    .success(true)
                    .message("프로필 사진이 수정되었습니다.")
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("프로필 사진 [Err_Msg]: {}", e.getMessage());

            return new ResponseEntity<>(CommonResponse.builder()
                    .success(false)
                    .message("프로필 사진이 수정되지 못했습니다.")
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/updatePersonalImg")
    public ResponseEntity<CommonResponse<?>> updatePersonalImg(@RequestBody MultipartFile uploadFile,
                                                               @AuthenticationPrincipal UserDetailsDto userDetailsDto) {

        System.out.println("uploadFile = " + uploadFile);

        if (uploadFile.getSize() > DataSize.ofMegabytes(1).toBytes()) {
            return ResponseEntity.badRequest()
                    .body(CommonResponse.builder()
                            .success(false)
                            .message("파일 크기는 1MB를 넘을 수 없습니다.")
                            .build());
        }

        try {
            FileDto fileDto = profileFileService.updatePersonalImg(uploadFile, userDetailsDto.getId());

            return new ResponseEntity<>(CommonResponse.builder()
                    .success(true)
                    .data(fileDto)
                    .message("프로필 사진이 수정되었습니다.")
                    .build(), HttpStatus.OK);
        } catch (IOException e) {
            log.error("프로필 사진 업로드 [Err_Msg]: {}", e.getMessage());

            return new ResponseEntity<>(CommonResponse.builder()
                    .success(false)
                    .message("프로필 사진 등록에 실패했습니다.")
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/updateNickName")
    public ResponseEntity<CommonResponse<?>> updateNickName(@RequestParam("nickname") String nickname,
                                                            @AuthenticationPrincipal UserDetailsDto userDetailsDto) {

        // 검증
        if (nickname == null || nickname.length() < 2 || nickname.length() > 10) {
            return new ResponseEntity<>(CommonResponse.builder()
                    .success(false)
                    .message("닉네임은 최소 2자에서 최대 10자까지 가능합니다.")
                    .build(), HttpStatus.BAD_REQUEST);
        }

        try {
            userService.updateNickName(userDetailsDto.getId(), nickname);
            userDetailsDto.updateNickName(nickname);

            return new ResponseEntity<>(CommonResponse.builder()
                    .success(true)
                    .data(nickname)
                    .message("닉네임이 수정되었습니다.")
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("닉네임 업데이트 [Err_Msg]: {}", e.getMessage());

            return new ResponseEntity<>(CommonResponse.builder()
                    .success(false)
                    .data(nickname)
                    .message("닉네임 수정에 실패했습니다.")
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/authenticateTelStart")
    public ResponseEntity<Void> sendAuthenticatingCode(@RequestParam("tel") String tel) {
        try {
            userService.sendAuthCodeToTel(tel);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("전화번호 인증 시작 [Err_Msg]: {}", e.getMessage());

            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/authenticateTel")
    public ResponseEntity<CommonResponse<?>> authenticateTel(@RequestParam("tel") String tel,
                                                             @RequestParam("code") String code,
                                                             @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        // 검증
        if (Objects.equals(redisService.getValues(tel), code)) {
            userService.updateTel(userDetailsDto.getId(), tel);

            redisService.deleteValues(tel);

            return new ResponseEntity<>(CommonResponse.builder()
                    .success(true)
                    .data(tel)
                    .message("전화번호가 수정되었습니다.")
                    .build(), HttpStatus.OK);
        }

        return new ResponseEntity<>(CommonResponse.builder()
                .success(false)
                .message("잘못된 코드입니다. 다시 확인해주세요.")
                .build(), HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<CommonResponse<?>> changePassword(@RequestParam("password") String password,
                                       @AuthenticationPrincipal UserDetailsDto userDetailsDto) {
        try {
            String passwordRegex = "^(?=.*[a-zA-Z])(?=.*[0-9!@#$%^&*()-+=<>]).{6,15}$";

            if (password.matches(passwordRegex)) {
                userService.updatePassword(userDetailsDto.getId(), password);

                return new ResponseEntity<>(CommonResponse.builder()
                        .success(true)
                        .message("비밀번호가 수정되었습니다.")
                        .build(), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("비밀번호 변경 [Err_Msg]: {}", e.getMessage());
        }

        return new ResponseEntity<>(CommonResponse.builder()
                .success(true)
                .message("비밀번호 수정에 실패 했습니다")
                .build(), HttpStatus.OK);
    }
}
