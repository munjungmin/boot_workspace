package com.sinse.customlogindb.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PasswordCreator {
    public String getCryptPassword(String pwd) {
        //salt를 적용하여 비밀번호를 암호화 시켜주는 객체
        // 내부적으로 salt를 사용하므로, 같은 문자열일지라도, 매번 생성할 때마다 암호화 결과물은 매번 바뀐다.
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String result = encoder.encode(pwd);
        return result;
    }
}
