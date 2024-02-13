package com.app.spotick.api.controller.user;

import com.app.spotick.api.dto.response.CommonResponse;
import com.app.spotick.api.dto.user.FindIdCertDto;
import com.app.spotick.api.dto.user.UserFindEmailDto;
import com.app.spotick.service.redis.RedisService;
import com.app.spotick.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/api")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;
    private final RedisService redisService;

    @GetMapping("/valid/email/{email}")
    public ResponseEntity<Boolean> isValidEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(userService.isValidEmail(email));
    }

    @GetMapping("/valid/nickname/{nickname}")
    public ResponseEntity<Boolean> isValidNickname(@PathVariable("nickname") String nickname) {
        return ResponseEntity.ok(userService.isValidNickname(nickname));
    }

    @PostMapping("/email/cert/code")
    public ResponseEntity<CommonResponse<Boolean>> sendEmailCertCode(@RequestBody FindIdCertDto findIdCertDto) {
        CommonResponse<Boolean> resp = null;
        String nickname = findIdCertDto.getNickname();
        String tel = findIdCertDto.getTel();

        if (!userService.checkUserByNicknameAndTel(nickname, tel)) {
            resp = new CommonResponse<>(false, "닉네임과 전화번호를 확인해주세요", false);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(resp);
        }
        userService.sendAuthCodeToTel(tel);
        resp = new CommonResponse<>(true, "인증번호를 전송했습니다.", true);
        return ResponseEntity.ok().body(resp);
    }

    @PostMapping("/find/email")
    public ResponseEntity<CommonResponse<UserFindEmailDto.Response>> findEmail(@RequestBody UserFindEmailDto.Request findEmailReq) {
        String tel = findEmailReq.getTel();
        String nickname = findEmailReq.getNickname();
        UserFindEmailDto.Response userFindEmailDto = null;
        CommonResponse<UserFindEmailDto.Response> findEmailResp = new CommonResponse<>(false, "", null);

        if (!userService.isValidCertCode(findEmailReq.getCertCode(), tel)) {
            findEmailResp.setMessage("인증번호를 다시 입력해 주세요");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(findEmailResp);
        }

        try {
            userFindEmailDto = userService.findUserFindEmailDto(nickname, tel);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            findEmailResp.setMessage("전화번호와 닉네임을 확인해 주세요");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(findEmailResp);
        }
        findEmailResp.setSuccess(true);
        findEmailResp.setData(userFindEmailDto);
        return ResponseEntity.ok(findEmailResp);
    }


}
















