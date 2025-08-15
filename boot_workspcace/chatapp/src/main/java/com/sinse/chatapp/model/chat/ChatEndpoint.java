package com.sinse.chatapp.model.chat;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.IOException;


@Slf4j
@ServerEndpoint("/ws/echo")
@Component
public class ChatEndpoint {

    //접속자 감지 즉 연결 감지 메서드
    //웹소켓에서는 더 이상 java SE 시절의 Socket 객체가 통신을 담당하지 않고 대신 Session 객체가 통신을 담당
    // 또한 접속자마다 1:1 대응하는 Thread를 별도로 제어할 필요 없음
    //cross origin 등록할 필요 없음. html 문서를 다운받는 구조가 아니어서?
    @OnOpen
    public void onOpen(Session session) {
        log.debug("onOpen() " + session.getId());
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        log.debug("onMessage() " + session.getId() + ", " + message);

        // echo가 목적이므로 클라이언트에게 다시 메시지 전송
        session.getBasicRemote().sendText("server : " + message);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) throws IOException{
        log.debug("onClose() " + session.getId() + ", " + closeReason);
    }

    @OnError
    public void onError(Session session, Throwable t) throws IOException{
        t.printStackTrace();
    }
}
