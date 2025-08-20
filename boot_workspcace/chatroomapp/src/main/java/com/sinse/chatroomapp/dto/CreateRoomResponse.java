package com.sinse.chatroomapp.dto;

import lombok.Data;

@Data
public class CreateRoomResponse {
    private String responseType;
    private Room room;
}
