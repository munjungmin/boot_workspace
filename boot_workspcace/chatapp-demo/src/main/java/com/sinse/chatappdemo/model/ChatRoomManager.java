package com.sinse.chatappdemo.model;

import com.sinse.chatappdemo.domain.ChatRoom;
import jakarta.websocket.Session;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatRoomManager {

    private static ChatRoomManager instance;
    private Map<Integer, ChatRoom> rooms = new ConcurrentHashMap<>();
    private AtomicInteger roomIdCounter = new AtomicInteger(1);

    private ChatRoomManager() {
    }

    public static ChatRoomManager getInstance() {
        if (instance == null) {
            instance = new ChatRoomManager();
        }
        return instance;
    }

    // 방 생성
    public void createRoom(ChatRoom room) {
        room.setRoomId(roomIdCounter.getAndIncrement());  //컨트롤러에서 조립할지 여기서 할지 우선 컨트롤러에서 조립
        room.setSessions(ConcurrentHashMap.newKeySet());
        //방을 만든사람은 여기 포함되어야 하는데, 방 입장은 웹소켓 연결을 해야함
        // 웹소켓은 무조건 클라이언트가 직접 연결해야 생긴다. 프론트에서 콜백으로 다시 ws요청해야됨

        rooms.put(room.getRoomId(), room);
    }

    // 방 조회
    public Collection<ChatRoom> getRooms() {
        return rooms.values();
    }

    // 방 입장

    // 방 퇴장

    // 방 삭제

}
