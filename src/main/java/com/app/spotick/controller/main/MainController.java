package com.app.spotick.controller.main;

import com.app.spotick.domain.entity.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

//    상시 메인페이지는 장소리스트페이지로 함
//    /place/list 요청시에도 같은 페이지가 나와야 함.
    @RequestMapping
    public String goToMain(HttpServletRequest request) {
//        회원가입 및 로그인 기능 추가 이전에 마이페이지에서 사용할 테스트 코드임 삭제하지 말 것
//        HttpSession session = request.getSession();
//
//        User user = User.builder()
//                .id(1L)
//                .email("cnh1234578@gmail.com")
//                .nickName("안녕하세요.")
//                .build();
//
//        // 세션에 사용자 정보 추가
//        session.setAttribute("user", user);

        return "place/list";
    }
}
