package com.sinse.chatroomapp.model.chat;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.chatroomapp.domain.Member;
import com.sinse.chatroomapp.dto.*;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint(value="/chat/multi", configurator = HttpSessionConfigurator.class)
public class ChatEndpoint {

    //프로그램 접속한 유저 목록 (서버용, 클라이언트용)
    private static Set<Member> memberList = new HashSet<>();

    //브로드캐스팅용: Room 객체에는 Set<Member>가 담겨있음. 근데 메시지를 전송하려면 해당 member의 Session 객체가 필요함
    private static Map<String, Session> memberSessionMap = new ConcurrentHashMap<>();  //멤버의 id -> session
    private static Set<Room> roomList = new HashSet<>();

    private static ObjectMapper objectMapper = new ObjectMapper();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws JsonProcessingException {
        // 웹소켓 연결할 때 로그인 유저의 정보를 저장하기 위해 HttpSession 정보를 웹소켓 Session에 저장
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        log.debug("onOpen()");
        if(httpSession != null) {
            Member member = (Member) httpSession.getAttribute("member");
            session.getUserProperties().put("member", member); //웹소켓 세션에 저장
            memberSessionMap.put(member.getId(), session);

            //응답정보 만들기
            ConnectResponse response = new ConnectResponse();
            response.setResponseType("connect");

            Member obj = new Member();
            obj.setId(member.getId());
            obj.setName(member.getName());
            obj.setEmail(member.getEmail());
            memberList.add(obj);    //클라이언트용 접속자 명단에 저장

            response.setMemberList(memberList);
            response.setRoomList(roomList);

            //java를 문자열로 변환 후 웹소켓으로 전송
            String jsonStr = objectMapper.writeValueAsString(response);
            log.debug("connect response=" + jsonStr);

            session.getAsyncRemote().sendText(jsonStr);
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws JsonProcessingException {

        log.debug("onMessage() message=" + message);

        // 요청타입 먼저 분석
        JsonNode jsonNode = objectMapper.readTree(message);
        String requestType = jsonNode.get("requestType").asText();

        if(requestType.equals("createRoom")){
            Room room = new Room();
            String uuid = UUID.randomUUID().toString();
            room.setUUID(uuid);
            room.setMaster(jsonNode.get("userId").asText());
            room.setRoomName(jsonNode.get("roomName").asText());
            room.setMemberList(new HashSet<>());
            //방을 만든 방장을 채팅방 참여자로 추가

            Member member = (Member)session.getUserProperties().get("member");
            Member participant = new Member();
            participant.setId(member.getId());
            participant.setName(member.getName());
            participant.setEmail(member.getEmail());
            room.getMemberList().add(participant);

            //방 목록에 추가
            roomList.add(room);

            //응답 객체 생성
            CreateRoomResponse response = new CreateRoomResponse();
            response.setResponseType("createRoom");
            response.setRoom(room);

            String jsonStr = objectMapper.writeValueAsString(response);
            log.debug("createRoomResponse=" + jsonStr);
            session.getAsyncRemote().sendText(jsonStr);
        } else if (requestType.equals("enterRoom")) {
            String uuid = jsonNode.get("uuid").asText();

            //이 유저를 해당 방에 분류
            Room room = roomList.stream()
                    .filter(r -> r.getUUID().equals(uuid))
                    .findAny()
                    .orElse(null);

            // 참여자 목록을 보내야되는데 보안상 중요한 비밀번호는 포함하면 안되니까 따로 멤버를 또 생섣해야될듯
            // 또 새로 멤버를 만들어서 넣으면, 이미 방 참여자인지 여부를 파악해야함
            //찾아낸 Room 안에 채팅 참여자로 등록(등록되어 있지 않은 사람만)
            Member member = (Member) session.getUserProperties().get("member");
            //룸에 들어있는 user정보와, 비요하여 같지 않은 경우에만 유저를 방에 추가
            boolean exists = false;
            for (Member obj : room.getMemberList()) {
                if (member.getId().equals(obj.getId())) {
                    exists = true;
                    break;
                }
            }

            if (exists == false) {
                Member participant = new Member();  // 클라이언트에게 전송될 예정이므로, 보안상 중요한 부분은 제외시키기 위해 별도의 멤버를 선언
                participant.setId(member.getId());
                participant.setName(member.getName());
                participant.setEmail(member.getEmail());
                room.getMemberList().add(participant);
            }

            //응답객체 생성
            EnterRoomResponse response = new EnterRoomResponse();
            response.setResponseType("enterRoom");
            response.setRoom(room);

            String jsonStr = objectMapper.writeValueAsString(response);
            log.debug("enterRoom response=" + jsonStr);
            session.getAsyncRemote().sendText(jsonStr);
        } else if (requestType.equals("chat")) {
            log.debug("chat request=" + jsonNode.toPrettyString());

            String sender = jsonNode.get("sender").asText();
            String uuid = jsonNode.get("uuid").asText();
            String data = jsonNode.get("data").asText();

            // uuid로 방을 얻어와서 그 안의 참여자 목록을 얻어오기
            Room room = roomList.stream()
                    .filter(r -> r.getUUID().equals(uuid))
                    .findAny()
                    .get();

            ChatResponse response = new ChatResponse();
            response.setResponseType("chat");
            response.setData(data);
            response.setSender(sender);
            response.setUuid(uuid);

            String jsonStr = objectMapper.writeValueAsString(response);
            log.debug("chat response=" + jsonStr);
            // 참여자들의 세션을 얻어와 broadcast
            for(Member m : room.getMemberList()){
                 memberSessionMap.get(m.getId()).getAsyncRemote().sendText(jsonStr);
            }
        }
    }
}
