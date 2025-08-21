package com.sinse.bootwebsocket.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ChatRoom {
    private String roomId;
    private String roomName;
    private Set<String> userList = new HashSet<>(); // 방 참여자 목록
}
