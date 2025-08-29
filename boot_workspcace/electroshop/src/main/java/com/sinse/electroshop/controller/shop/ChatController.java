package com.sinse.electroshop.controller.shop;

import com.sinse.electroshop.domain.Member;
import com.sinse.electroshop.domain.Store;
import com.sinse.electroshop.websocket.dto.ChatMessage;
import com.sinse.electroshop.websocket.dto.ChatRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
    Spring의 STOMP를 이용하면 웹소켓을 일반 컨트롤러로 제어할 수 있다.
    마치 웹 요청을 처리하듯..
*/

@Slf4j
@Controller
public class ChatController {
    //서버에 접속한 모든 유저 목록
    private Set<String> connectedUsers = ConcurrentHashMap.newKeySet();

    //서버에 존재하는 모든 방목록(상품의 수와 일치)
    private Map<String, ChatRoom> rooms = new ConcurrentHashMap<>();

    //접속 요청 처리
    @MessageMapping("/connect")  //localhost:9999/app/connect
    @SendTo("/topic/users")
    public Set<String> connect(ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        //SimpMessageHeaderAccessor 객체를 이용하면 WebSocket의 Session에 들어있는 정보를 추출

        log.debug("chatMessage content=" + chatMessage.getContent());

        int product_id = Integer.parseInt(chatMessage.getContent());
        ChatRoom chatRoom = null;

        if (headerAccessor.getSessionAttributes().get("member") != null) {
            Member member = (Member) headerAccessor.getSessionAttributes().get("member");
            //HttpSession에서 사용자 로그인 정보인 Member를 꺼내보자
            //STOMP 기반으로 HttpSession을 꺼내려면 인터셉터 객체를 구현 및 등록해야 함
            log.debug("웹소켓 세션에서 꺼낸 정보는 " + member.getName());
            log.debug("클라이언트 접속과 동시에 보낸 메시지 " + chatMessage.getContent());

            //일반회원은 개설된 방에 참여하면 됨.. 하지만 어떤 방을 들어갈 지 알아야 함
            for (ChatRoom room : rooms.values()) {
                if (room.getProduct_id() == product_id) {
                    chatRoom = room;
                    break;
                }
            }
            chatRoom.getCustomers().add(member.getId());

        } else if (headerAccessor.getSessionAttributes().get("store") != null) {
            Store store = (Store) headerAccessor.getSessionAttributes().get("store");
            log.debug("웹소켓 세션에서 꺼낸 정보는 " + store.getStoreName());
            log.debug("클라이언트 접속과 동시에 보낸 메시지 " + chatMessage.getContent());


            // 채팅방을 추가하기 전에 중복 여부 판단하기

            boolean exist = false;
            for (ChatRoom room : rooms.values()) {
                if (room.getProduct_id() == product_id) {
                    exist = true;
                    chatRoom = room;
                    break;
                }
            }

            if (!exist) {
                chatRoom = new ChatRoom();
                chatRoom.setRoomId(UUID.randomUUID().toString());
                chatRoom.setProduct_id(product_id);  //어떤 상품에 대한 채팅방인지
                chatRoom.getCustomers().add(store.getBusinessId());

                rooms.put(chatRoom.getRoomId(), chatRoom);
            }

            // 방 참여하기
            chatRoom.getCustomers().add(store.getBusinessId());

            log.debug("chatRoom size=" + rooms.size());
        }

        return chatRoom.getCustomers();
    }

    //메시지 요청 처리
    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")  // /topic/messages를 구독한 사람들에게 전송
    public ChatMessage send(ChatMessage message) {
        log.debug("클라이언트가 전송한 메시지 " + message.getContent());
        return message;
    }
}
