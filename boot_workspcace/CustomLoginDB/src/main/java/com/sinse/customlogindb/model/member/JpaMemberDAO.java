package com.sinse.customlogindb.model.member;

import com.sinse.customlogindb.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class JpaMemberDAO implements MemberDAO {

    private final JpaMemberRepository memberRepository;

    @Override
    public List<Member> selectAll() {
        return List.of();
    }

    @Override
    public Member select(int member_id) {
        return null;
    }

    @Override
    public void insert(Member member) {

    }

    @Override
    public void delete(Member member) {

    }

    @Override
    public void update(Member member) {

    }

    @Override
    public Member getMemberById(String id) {
        return memberRepository.findById(id);
    }
}
