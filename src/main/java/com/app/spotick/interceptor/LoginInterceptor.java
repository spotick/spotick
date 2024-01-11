package com.app.spotick.interceptor;

import com.app.spotick.domain.entity.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("user");

//        세션에서 user 정보를 찾지 못할 시 로그인 페이지로
        if (user == null) {
            request.getSession().setAttribute("message", "로그인이 필요한 서비스입니다.");
            response.sendRedirect("/user/login");

            return false;
        }

//        아닐 시 그대로 진행
        return true;
    }
}
