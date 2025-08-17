package com.sinse.chatappdemo.controller;

import com.sinse.chatappdemo.domain.ChatRoom;
import com.sinse.chatappdemo.model.ChatRoomManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
public class ChatRoomController {
    private ChatRoomManager chatRoomManager = ChatRoomManager.getInstance();

    @PostMapping("/rooms")
    public String createRoom(@RequestBody ChatRoom room) {
        log.debug("room.title=" + room.getTitle());
        log.debug("room.description=" + room.getDescription());
        chatRoomManager.createRoom(room);

        return "ok";
    }

    @GetMapping("/rooms")
    public List<ChatRoom> getRooms() {
        return chatRoomManager.getRooms().stream().toList();
    }

}
