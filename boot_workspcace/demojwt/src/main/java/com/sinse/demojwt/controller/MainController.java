package com.sinse.demojwt.controller;

import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/auth")
    public String authPage() {
        return "auth.html";
    }
}
