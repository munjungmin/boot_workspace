package com.sinse.jwtredis.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.jwtredis.domain.Member;
import com.sinse.jwtredis.domain.MemberDTO;
import org.springframework.stereotype.Service;

@Service
public class RegistService {
    private final RegistRedisService registRedisService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RegistService(RegistRedisService registRedisService) {
        this.registRedisService = registRedisService;
    }

    public void regist(MemberDTO memberDTO) {
        try {
            String jsonStr = objectMapper.writeValueAsString(memberDTO);

            //회원 임시 정보
            redis.opsForValue().set(pendingKey(memberDTO.getEmail()), jsonStr, PENDING_TTL);

            //이메일을 찾을 수 있도록 인덱스를 생성하자
            redis.opsForValue().set(codeKey(member.getEmail()), member.getEmail(), PENDING_TTL);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
