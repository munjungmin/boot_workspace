package com.sinse.chatappdemo.domain;

import jakarta.websocket.Session;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class ChatRoom {
    private int roomId;
    private String title;
    private String description;
    Set<Session> sessions;
}
