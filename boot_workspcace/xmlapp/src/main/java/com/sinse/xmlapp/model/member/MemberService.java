package com.sinse.xmlapp.model.member;

import com.sinse.xmlapp.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class MemberService {

    private MemberHandler memberHandler;

    public MemberService(MemberHandler memberHandler) {
        this.memberHandler = memberHandler;
    }

    // xml 읽고 member handler의 memberList 가져다 쓰기
    //파싱 시도하기
    public List<Member> parse() throws IOException, ParserConfigurationException, SAXException {
        //스프링 부트 프로젝트의 정적 자원을 먼저 접근
        //ClassPathResource 객체는 프로젝트 내의 resources 디렉토리를 가리킴
        ClassPathResource resource = new ClassPathResource("static/member.xml");
        File file = resource.getFile();

        //SAXParserFactory 생성
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        //파일을 대상으로 파싱 시작
        saxParser.parse(file, memberHandler);  //동기 방식으로 파싱

        log.debug("파싱완료");

        return memberHandler.getMemberList();
    }
}
