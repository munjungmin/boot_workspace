package com.sinse.demojwt.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="member")
@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //mysql의 auto_increment가 적용
    private int member_id;

    @Column(name = "id")
    private String loginId;
    private String password;
    private String name;
}
