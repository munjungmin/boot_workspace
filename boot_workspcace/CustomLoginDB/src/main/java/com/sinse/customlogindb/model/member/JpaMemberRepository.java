package com.sinse.customlogindb.model.member;

import com.sinse.customlogindb.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMemberRepository extends JpaRepository<Member, Integer> {

    public Member findById(String id);
    public Member findByPassword(String password);
}
