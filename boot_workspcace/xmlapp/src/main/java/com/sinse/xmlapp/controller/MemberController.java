package com.sinse.xmlapp.controller;

import com.sinse.xmlapp.domain.Member;
import com.sinse.xmlapp.model.member.MemberService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemberController {

    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/test")
    public String test() {
        return "my app is successful";
    }

    @GetMapping("/members")
    public List<Member> getMemberList() throws Exception {
        return memberService.parse();
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e){
        return e.getMessage();
    }
}
