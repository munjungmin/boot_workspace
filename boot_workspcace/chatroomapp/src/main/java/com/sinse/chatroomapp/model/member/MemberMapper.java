package com.sinse.chatroomapp.model.member;

import com.sinse.chatroomapp.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    public List<Member> selectAll();
    public Member select(int member_id);
    public Member login(Member member);
}
