package com.sinse.chatappdemo.model;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// serverendpoint는 스프링이 아니라 톰캣이 관리함
// 연결마다 새 인스턴스가 만들어짐!! 그래서 세션?들을 관리하는 클래스를 따로 두어야 한다.
@Slf4j
@Component
@ServerEndpoint("/ws/broad")
public class BroadChatEndpoint {
    private BroadSessionManager broadSessionManager = BroadSessionManager.getInstance();

    @OnOpen
    public void onOpen(Session session) {
        log.debug("broad.onOpen() sessionId=" + session.getId());
        broadSessionManager.addClient(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        log.debug("broad.onMessage() sessionId=" + session.getId() + " message=" + message);

        // 서버에 접속한 모든 세션에 메시지 전송
        broadSessionManager.broadcast(session, message);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        log.debug("broad.onMessage() sessionId=" + session.getId() + " closeReason=" + closeReason);
        broadSessionManager.removeClient(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }
}