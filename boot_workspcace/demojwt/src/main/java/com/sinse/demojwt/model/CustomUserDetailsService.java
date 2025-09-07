package com.sinse.demojwt.model;

import com.sinse.demojwt.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// user 정보를 가져올 저장소를 직접 지정하기 위해 구현
// 늘 하던 방식처럼 repository에서 username을 가진 member를 찾고 그걸 UserDetails 객체로 감싸서 반환하면 된다.


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByLoginId(username);

        if(member == null){
            throw new UsernameNotFoundException(username);
        }

        return new CustomUserDetails(member);
    }
}
