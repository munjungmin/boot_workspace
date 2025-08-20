package com.sinse.chatroomapp.model.member;

import com.sinse.chatroomapp.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("mybatisMemberDAO")
public class MybatisMemberDAO implements MemberDAO{

    private MemberMapper memberMapper;

    public MybatisMemberDAO(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @Override
    public List<Member> selectAll() {
        return memberMapper.selectAll();
    }

    @Override
    public Member select(int member_id) {
        return memberMapper.select(member_id);
    }

    @Override
    public Member login(Member member) {
        return memberMapper.login(member);
    }
}
