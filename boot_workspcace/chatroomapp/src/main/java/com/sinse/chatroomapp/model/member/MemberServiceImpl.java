package com.sinse.chatroomapp.model.member;

import com.sinse.chatroomapp.domain.Member;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService{

    private MemberDAO memberDAO;

    public MemberServiceImpl(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    @Override
    public List<Member> selectAll() {
        return memberDAO.selectAll();
    }

    @Override
    public Member select(int member_id) {
        return memberDAO.select(member_id);
    }

    @Override
    public Member login(Member member) {
        return memberDAO.login(member);
    }
}
