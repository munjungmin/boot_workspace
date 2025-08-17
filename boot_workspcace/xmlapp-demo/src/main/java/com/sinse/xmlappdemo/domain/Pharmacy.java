package com.sinse.xmlappdemo.domain;

import lombok.Data;

@Data
public class Pharmacy {
    private String name;  //사업장명
    private String state;  // 영업상태
    private String telno;  //번호
    private String road_addr;  //도로명 주소
    private double gpsx;
    private double gpsy;
}
