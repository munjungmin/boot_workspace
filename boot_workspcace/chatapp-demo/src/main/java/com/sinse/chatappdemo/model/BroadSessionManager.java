package com.sinse.chatappdemo.model;

import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class BroadSessionManager {
    private static BroadSessionManager instance;

    // List보다 Set이 성능 좋다. 중복 방지 및 삭제/조회 빠름
    // ConcurrentHashMap.newKeySet() 스레드 안전한 동시성 Set
    private Set<Session> sessionSet = ConcurrentHashMap.newKeySet();

    private BroadSessionManager() {
    }

    public static BroadSessionManager getInstance() {
        if (instance == null) {
            instance = new BroadSessionManager();
        }
        return instance;
    }

    public void addClient(Session session){
        sessionSet.add(session);
        log.debug("add() sessionList.size()=" + sessionSet.size());
    }

    public void broadcast(Session session, String message) throws IOException {
        for (Session s : sessionSet) {
            s.getBasicRemote().sendText(session.getUserProperties().get("nickname") + ": " + message);
        }
    }

    public void removeClient(Session session) {
        sessionSet.remove(session);
        log.debug("remove() sessionList.size()=" + sessionSet.size());
    }

}
