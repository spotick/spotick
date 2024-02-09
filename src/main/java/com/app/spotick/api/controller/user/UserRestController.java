package com.app.spotick.api.controller.user;

import com.app.spotick.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
















