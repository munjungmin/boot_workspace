package com.sinse.jwtredis.controller;

import com.sinse.jwtredis.domain.Member;
import com.sinse.jwtredis.model.RegistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MemberController {

    private RegistService registService;



    @PostMapping("/member/regist")
    public ResponseEntity<?> regist(@RequestBody Member member){  //클라이언트가 json으로 보내면 자동매핑 하겠다.
        log.debug("regist member" + member);
        return ResponseEntity.ok(null);
    }
}
