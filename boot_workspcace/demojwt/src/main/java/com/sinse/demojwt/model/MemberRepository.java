package com.sinse.demojwt.model;

import com.sinse.demojwt.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Integer> {
    public Member findByLoginId(String loginId);
}
