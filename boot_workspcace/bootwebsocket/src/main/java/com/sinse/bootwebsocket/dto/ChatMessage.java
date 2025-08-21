package com.sinse.bootwebsocket.dto;

import lombok.Data;

//서버와 클라이언트가 대화를 위한 메시지 전달 객체
@Data
public class ChatMessage {

    // Type에 올 수 있는 것들
    /*
        CONNECT: 접속
        DISCONNECT: 접속해제
        MESSAGE: 채팅
        ROOM_CREATE: 생성
        ROOM_LIST: 조회
        ROOM_ENTER: 입장
        ROOM_LEAVE: 퇴장
    */
    private String type;
    private String sender;   // 요청을 보낸 사람
    private String content;  //메시지 내용
    private String roomId;   // 방 ID
}
