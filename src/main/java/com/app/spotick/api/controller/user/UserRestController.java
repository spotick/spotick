package com.app.spotick.api.controller.user;

import com.app.spotick.api.dto.response.CommonResponse;
import com.app.spotick.api.dto.user.FindEmailDto;
import com.app.spotick.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/api")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    @GetMapping("/valid/email/{email}")
    public ResponseEntity<Boolean> isValidEmail(@PathVariable("email")String email){
        return ResponseEntity.ok(userService.isValidEmail(email));
    }

    @GetMapping("/valid/nickname/{nickname}")
    public ResponseEntity<Boolean> isValidNickname(@PathVariable("nickname")String nickname){
        return ResponseEntity.ok(userService.isValidNickname(nickname));
    }

    @PostMapping("/email/cert/code")
    public ResponseEntity<CommonResponse<Boolean>> sendEmailCertCode(@RequestBody FindEmailDto findEmailDto){
        CommonResponse<Boolean> resp = null;
        if(!userService.checkUserByNicknameAndTel(findEmailDto.getNickname(),findEmailDto.getTel())) {
             resp = new CommonResponse<>(true,"닉네임과 전화번호를 확인해주세요",false);
            return ResponseEntity.badRequest().body(resp);
        }
        resp = new CommonResponse<>(true,"인증번호를 전송했습니다.",true);
        return ResponseEntity.ok().body(resp);
    }



}
















