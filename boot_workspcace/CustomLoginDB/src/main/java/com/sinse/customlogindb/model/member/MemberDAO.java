package com.sinse.customlogindb.model.member;

import com.sinse.customlogindb.domain.Member;

import java.util.List;

public interface MemberDAO {
    public List<Member> selectAll();
    public Member select(int member_id);
    public void insert(Member member);
    public void delete(Member member);
    public void update(Member member);

    public Member getMemberById(String id);
}
