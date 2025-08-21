package com.sinse.bootwebsocket.model.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.bootwebsocket.dto.ChatMessage;
import com.sinse.bootwebsocket.dto.ChatRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//javaee 순수 api로 ServerEndpoint를 구현했던 클래스와 같은 역할을 수행하는 클래스
// 단 스프링 기반 api로 구현해본다.
@Slf4j
@Component
public class ChatWebSocketHandler implements WebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, WebSocketSession> connectedUsers = new ConcurrentHashMap<>();
    private final Map<String, ChatRoom> rooms = new ConcurrentHashMap<>();

    //javaee api의 @OnOpen
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.debug("새 클라이언트가 연결됨", session.getId());
        //sessions.add(session);
    }

    //javaee api의 @OnMessage
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.debug("client가 보낸 메시지=" + message.getPayload().toString());
        //클라이언트가 보낸 메시지는 그냥 String일 뿐이므로, 분석을 위해서는 자바 객체화시켜야한다.
        // jackson databind 라이브러리 이용
        ChatMessage chatMessage = objectMapper.readValue(message.getPayload().toString(), ChatMessage.class);

        switch (chatMessage.getType()) {
            case "CONNECT" -> {
                // 새로운 유저가 접속했으므로, 총 접속자 목록에 추가
                connectedUsers.put(chatMessage.getSender(), session);

                //모든 접속된 유저들에게 접속자 목록에 대한 브로드캐스팅
                //브로드 캐스팅 시, 클라이언트가 서버가 보낸 메시지를 구분할 수 있는 구분 채널, 또는 구분 값을 포함해서 보내주자
                broadcast("/users", connectedUsers.keySet());
            }
            case "DISCONNECT" -> {
                connectedUsers.remove(chatMessage.getSender());
                broadcast("/users", connectedUsers.keySet());
            }
            case "MESSAGE" -> {
                broadcast("/messages", chatMessage);
            }
            case "ROOM_CREATE" -> {
                //방 생성
                ChatRoom room = new ChatRoom();
                String uuid = UUID.randomUUID().toString();
                room.setRoomId(uuid);
                room.setRoomName(chatMessage.getContent());

                rooms.put(uuid, room);
                broadcast("/rooms", rooms.values());
            }
            case "ROOM_ENTER" -> {
                ChatRoom room = rooms.get(chatMessage.getRoomId());
                if(room != null){
                    room.getUserList().add(chatMessage.getSender());
                }
                broadcast("/rooms", rooms.values());
            }
            case "ROOM_LIST" -> {
                broadcast("/rooms", rooms.values());
            }
            case "ROOM_LEAVE" -> {
                ChatRoom room = rooms.get(chatMessage.getRoomId());
                if(room != null){
                    room.getUserList().remove(chatMessage.getSender());
                }
                broadcast("/rooms", rooms.values());
            }
        }
    }

    //javaee api의 @OnError
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    //javaee api의 @OnClose
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {}

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    // 거의 모든 클라이언트의 요청마다, 서버는 접속한 클라이언트들을 대상으로 메시지를 전송해야 하므로 브로드캐스트가 수시로 일어나야 하므로 메서드로 정의
    // 또한 이 메서드는 이 클래스에서만 접근할 것이므로, 굳이 public 외부에 공개할 필요가 없다.
    private void broadcast(String destination, Object data) throws IOException {
        String jsonStr = objectMapper.writeValueAsString(
                Map.of("destination", destination, "body", data)
        );

        //서버에 현재 접속해있는 모든 클라이언트의 세션만큼 반복
        for (WebSocketSession ws : connectedUsers.values()) {
            if(ws.isOpen()) {
                ws.sendMessage(new TextMessage(jsonStr));
            }
        }
    }
}
