package com.sinse.chatroomapp.dto;

import lombok.Data;

@Data
public class ExitRoomResponse {

    private String responseType;
    private String sender;
    private Room room;
}
