package com.sinse.jwtredis.domain;

import lombok.Data;

@Data
public class MemberDTO {
    private int member_id;
    private String id;
    private String pwd;
    private String name;
    private String email;
    private String code;  //6자리 랜덤값
}
