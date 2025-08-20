package com.sinse.chatroomapp.model.member;

import com.sinse.chatroomapp.domain.Member;

import java.util.List;

public interface MemberDAO {
    public List<Member> selectAll();
    public Member select(int member_id);
    public Member login(Member member);
}
