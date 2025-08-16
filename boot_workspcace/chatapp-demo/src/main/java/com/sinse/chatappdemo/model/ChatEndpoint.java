package com.sinse.chatappdemo.model;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@ServerEndpoint("/ws/echo")   // java 표준, client가 웹소켓으로 접속할 수 있는 접속 지점(url 경로)를 선언
@Component
public class ChatEndpoint {

    @OnOpen
    public void onOpen(Session session) {
        log.debug("onOpen() sessionId=" + session.getId());
    }

    @OnMessage
    public void onMessage(Session session, String message) throws Exception{
        log.debug("onMessage() sessionId=" + session.getId() + " message=" + message);

        // 에코 서버이므로 다시 클라이언트에게 전송
        // 세션이 remote endpoint 객체를 보유하고 있음
        session.getBasicRemote().sendText("server: " + message);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        log.debug("onClose() sessionId=" + session.getId() + " closeReason=" + closeReason);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }
}
