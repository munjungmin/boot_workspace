package com.sinse.bootwebsocket.model.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.bootwebsocket.dto.ChatMessage;
import com.sinse.bootwebsocket.dto.ChatRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/*
스프링의 웹소켓 Server Endpoint를 다루는 객체는 WebSocketHandler만 있는게 아니다.
 [ TextWebSocketHandler ]
 - 다루고자 하는 데이터가 Text일 경우 효율적
 - 인터페이스가 아니므로, 사용되지도 않는 메서드를 재정의할 필요 없다. 즉 필요한 것만 골라서 재정의
*/
@Slf4j
@Component
public class ChatTextWebSocketHandler extends TextWebSocketHandler {

    //java - json 문자열과의 변환을 자동으로 처리해주는 객체
    private ObjectMapper objectMapper = new ObjectMapper();

    //현재 서버에 연결되어 있는 모든 클라이언트 세션집합 (클라이언트 전송용 아님)
    private Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    //현재 서버에 접속되어 있는 모든 클라이언트 아이디 집합 (클라이언트 전송용)
    private Set<String> connectedUsers = ConcurrentHashMap.newKeySet();

    //전체 방목록 집합
    private Map<String, ChatRoom> rooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //클라이언트는 java가 이해할 수 없는 json 문자열 형태로 메시지를 전송하므로, 서버측에서는 해석이 필요하다
        ChatMessage chatMessage = objectMapper.readValue(message.getPayload(), ChatMessage.class);

        //클라이언트의 요청 분기
        switch (chatMessage.getType()){
            case "CONNECT" -> {

            }
            case "DISCONNECT" -> {

            }
            case "MESSAGE" -> {

            }
            case "ROOM_CREATE" -> {

            }
            case "ROOM_LIST" -> {

            }
            case "ROOM_ENTER" -> {

            }
            case "ROOM_LEAVE" -> {

            }
        }


    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }

    // 모든 클라이언트가 동시에 알아야할 정보를 전송할 브로드캐스트 메서드 정의
    // 매개변수가 Object인 이유는? ObjectMapper에게 json 문자열로 변환을 맡길 데이터 형식이 결정돼있지 않기 때문에
    private void broadcast(String dest, Object data) throws Exception{
        String payload = objectMapper.writeValueAsString(Map.of("destination", dest, "body", data));

        // 접속한 모든 클라이언트에게 전송
        for(WebSocketSession ws : sessions) {
            if (ws.isOpen()) {
                ws.sendMessage(new TextMessage(payload));
            }
        }
    }

}
