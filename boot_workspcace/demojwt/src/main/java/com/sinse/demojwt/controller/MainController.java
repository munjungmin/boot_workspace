package com.sinse.demojwt.controller;

import com.sinse.demojwt.model.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class MainController {

    @GetMapping("/main")
    public String getMain() {
        log.debug("MainController 진입!!!!");
        return "tempmain.html";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login.html";
    }

    /**
     * UserDetails(사용자 정보가 들어있는 객체) - Authentication - SecurityContext(Session에 저장됨) - SecurityContextHolder
     * 스프링 시큐리티를 쓰기 전에는 직접 세션에 담고, 세션에서 사용자 정보를 꺼내서 사용했었음
     * 스프링 시큐리티를 사용하면 인증이 성공했을때 세션에 SecurityContext를 담아준다.
     *
     * @return
     */
    @GetMapping("/auth")
    public String authPage(HttpSession session, Authentication auth, @AuthenticationPrincipal CustomUserDetails customDetails) {
        // 로그인에 성공했다면 이 페이지를 보여줌

        // 1. session에서 직접 사용자 정보를 꺼내기
        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        CustomUserDetails customUserDetails = (CustomUserDetails) securityContext.getAuthentication().getPrincipal();
        log.debug("session의 securityContext에서 꺼낸 username=" + customUserDetails.getUsername());


        // 2. Authentication에서 직접 꺼내기
        String username = ((CustomUserDetails) auth.getPrincipal()).getUsername();
        log.debug("authentication에서 꺼낸 username=" +  username);

        //3. SecurityContextHolder에서 직접 꺼내기
        CustomUserDetails details = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("SecurityContextHolder에서 꺼낸 username=" + username);

        // 4. @AuthenticationPrincipal 사용
        String name = customDetails.getUsername();
        log.debug("@Authentication에서 꺼낸 username=" + name);

        return "auth.html";
    }
}
