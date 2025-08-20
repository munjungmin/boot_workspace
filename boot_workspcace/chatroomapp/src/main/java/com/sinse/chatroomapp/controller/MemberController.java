package com.sinse.chatroomapp.controller;

import com.sinse.chatroomapp.domain.Member;
import com.sinse.chatroomapp.model.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/member/loginform")
    public String loginform() {
        return "member/login";
    }

    //동기방식
    @PostMapping("/member/login")
    public String login(HttpSession session, Model model, Member member) {
        log.debug("member id=" + member.getId());
        log.debug("member password=" + member.getPassword());

        Member obj = memberService.login(member);

        if (obj == null) {
            model.addAttribute("loginError", "로그인 정보가 올바르지 않습니다.");
            return "member/login";
        }

        session.setAttribute("member", obj);
        return "redirect:/chat/main";
    }
    //비동기 방식
//    @PostMapping("/member/login")
//    @ResponseBody
//    public ResponseEntity<String> login(HttpSession session, Member member) {
//        log.debug("member id=" + member.getId());
//        log.debug("member password=" + member.getPassword());
//
//        Member obj = memberService.login(member);
//
//        if (obj == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 혹은 비밀번호가 올바르지 않습니다.");
//        }
//
//        session.setAttribute("member", obj);
//        return ResponseEntity.ok("success");
//    }


    @GetMapping("/chat/main")
    public String main(HttpSession session) {
        String viewName = "chat/main";

        // 로그인 하지 않고 /chat/main 접근 시 로그인 폼으로 돌려보내기
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            viewName = "member/login";
        }

        return viewName;
    }

    @GetMapping("/chat/room")
    public String room(HttpSession session) {
        return "chat/room";
    }
}

