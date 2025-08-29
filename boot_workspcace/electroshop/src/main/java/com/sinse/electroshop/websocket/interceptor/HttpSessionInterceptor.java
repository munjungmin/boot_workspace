package com.sinse.electroshop.websocket.interceptor;

import com.sinse.electroshop.domain.Member;
import com.sinse.electroshop.domain.Store;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j

// 아래의 클래스는 WebSocket 연결과정(Handshake)에서 HttpSession 정보를 WebSocket 세션 속성으로 옮겨놓기 위한 객체이다
public class HttpSessionInterceptor implements HandshakeInterceptor {

    //WebSocket 핸드세이크가 시작되기 전에 호출되는 메서드
    //이 타이밍을 놓치지 말고, HttpSession에 들어있는 값을 WebSocket의 Session에 옮겨심기
    //주의) 4번째 매개변수인 attributes가 바로 WebSocket 세션의 attributes임
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        //HttpSession에서 Member 꺼내기
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        //만일 세션이 없을 경우, 새로 생성하지 말라는 매개변수 false
        HttpSession httpSession = servletRequest.getServletRequest().getSession(false);

        if(httpSession != null){
            if(httpSession.getAttribute("member") != null) {//일반 유저인 경우
                Member member = (Member) httpSession.getAttribute("member");
                attributes.put("member", member);  //WebSocket의 Session으로 옮겨심기
                log.debug("핸드셰이크 시점에 추출한 일반 회원의 이름은 " + member.getId());
            }else if(httpSession.getAttribute("store") != null){ //상점 관리자인 경우
                Store store = (Store) httpSession.getAttribute("store");
                attributes.put("store", store);  //WebSocket의 Session으로 옮겨심기
                log.debug("핸드셰이크 시점에 추출한 상점의 이름은 " + store.getStoreName());
            }
        }

        return true;
    }

    //핸드세이크가 끝난 후 호출되는 메서드
    //보통은 특별히 사용할 일이 없음 (로그 기록 정도..)
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
