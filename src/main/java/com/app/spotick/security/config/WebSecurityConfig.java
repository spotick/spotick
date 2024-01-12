package com.app.spotick.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public static PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//               spring security csrf 방지 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests((requests) -> requests
//                        해당  url로 들어온 요청은 인증을 해야한다
                                .requestMatchers("/mypage/*").authenticated()
//                        해당  url로 들어온 요청은 인증과 권한확인을 해야한다
//                                .requestMatchers("/admin")
//                                .hasAnyRole("USER", "ADMIN")
//                                Role로 회원의 권한을 검사할 경우 db에 ROLE_접두어를 붙여 저장한다.( ex. ROLE_USER, ROLE_ADMIN )
//                                .hasAnyAuthority("USER", "ADMIN")
                                // hasAnyAuthority()메소드로 검사하는 경우 해당 문자열 그대로 검사한다.

//                        그 밖의 요청은 허용한다.
                                .anyRequest().permitAll()

                )
                .formLogin((form) -> form
//                        로그인 페이지의 url설정
                                .loginPage("/user/login")
                                .loginProcessingUrl("/user/login")
                                .defaultSuccessUrl("/")
//                                .successHandler(new CustomLoginSuccessHandler("/"))

                )

                .logout((logout) -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true)
                )
                .exceptionHandling(exceptionHandling->
                        exceptionHandling.accessDeniedPage("/")
                );

        return http.build();
    }


}