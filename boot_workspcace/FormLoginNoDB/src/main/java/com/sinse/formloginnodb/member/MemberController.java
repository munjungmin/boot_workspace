package com.sinse.formloginnodb.member;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    //스프링 시큐리티에 의해 인증이 성공되면 사용자정보는 UserDetails 객체에 들어있다
    //이때 UserDetails는 객체를 꺼내는 방법은 총 4가지 방법이 있다.
    /*------------------------------------------------------
    방법 1) 세션에서 직접 꺼내기
    ------------------------------------------------------*/
    @GetMapping("/")
    public String index1(HttpSession session, Model model) {
        SecurityContext context = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");

        Authentication auth = context.getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String id = userDetails.getUsername();
        model.addAttribute("id", "session에서 꺼냄 : " + id);

        return "member/index";
    }

    // 방법 2) Authentication에서 직접 꺼내기
    @GetMapping("/main")
    public String main(Authentication auth, Model model) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        model.addAttribute("id", "Authentication에서 꺼냄: " + userDetails.getUsername());

        return "member/index";
    }

    // 방법 3) SecurityContextHolder에서 직접 꺼내기
    @GetMapping("/security/v3")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String id = userDetails.getUsername();

        model.addAttribute("id", id);
        return "member/index";
    }

    // 방법 4) @AuthenticationPrincipal 사용
    @GetMapping("/security/v4")
    public String securityV4(@AuthenticationPrincipal UserDetails userDetails, Model model){
        model.addAttribute("id", "annotation 사용" + userDetails.getUsername());
        return "member/index";
    }
}