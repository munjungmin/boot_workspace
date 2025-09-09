package com.sinse.jwtredis.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.jwtredis.domain.Member;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

//Redis 관련 로직: 임시가입 정보를 저장하고, 검증
@Service
public class RegistRedisService {

    // redis에게 데이터를 넣을때 문자열화 시켜서 넣는 작업을 쉽게 처리해주는 라이브러리
    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SecureRandom random = new SecureRandom();

    // TTL (Time To Live) 설정
    private static final Duration PENDING_TTL = Duration.ofMinutes(3);  // 임시가입 인증 만료시간

    // 키설계 (redis에 사용할 키 규칙 - 개발자가 정하는 것이다.)
    private String pendingKey(String email) {
        return "pending:" + email;   // pending:zino1187@naver.com
    }

    //이메일을 조회하기 위한 키 설계
    private String codeKey(String code){
        return "code:" + code;
    }

    public RegistRedisService(StringRedisTemplate stringRedisTemplate) {
        this.redis = stringRedisTemplate;
    }

    // 이메일 인증코드 생성, 6자리
    public String generateCode6(){
        return String.format("%06d", random.nextInt(1_000_000));  //6자리 랜덤값
    }

    // 임시가입 (redis)에 쓰기
    //별도의 DTO가 필요하지만 지금은 예시니까 그냥 엔티티를 받는다.
    public void savePending(Member member) {
        // Member라는 java객체가 redis로 insert 되려면, 문자열화되어야 함
        try {
            String jsonStr = objectMapper.writeValueAsString(member);

            //회원 임시 정보
            redis.opsForValue().set(pendingKey(member.getEmail()), jsonStr, PENDING_TTL);

            //이메일을 찾을 수 있도록 인덱스를 생성하자
            redis.opsForValue().set(codeKey(member.getEmail()), member.getEmail(), PENDING_TTL);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }




}
