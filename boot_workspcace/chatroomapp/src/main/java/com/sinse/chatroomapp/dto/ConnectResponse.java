package com.sinse.chatroomapp.dto;

import com.sinse.chatroomapp.domain.Member;
import lombok.Data;

import java.util.Map;
import java.util.Set;

/*
     {
         responseType : "createRoom",
         memberList: [
             {
                 id: "mario",
                 name: "마리오",
                 email: "mario@naver.com"
             }
         ],
         roomList: []
     }
*/
@Data
public class ConnectResponse {
    private String responseType;
    private Set<Member> memberList;
    private Set<Room> roomList;
}
