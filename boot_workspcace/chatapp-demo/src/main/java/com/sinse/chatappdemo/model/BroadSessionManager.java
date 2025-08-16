package com.sinse.chatappdemo.model;

import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

@Slf4j
public class BroadSessionManager {
    private static BroadSessionManager instance;
    private List<Session> sessionList = new Vector<>();

    private BroadSessionManager() {
    }

    public static BroadSessionManager getInstance() {
        if (instance == null) {
            instance = new BroadSessionManager();
        }
        return instance;
    }

    public void addClient(Session session){
        sessionList.add(session);
        log.debug("add() sessionList.size()=" + sessionList.size());
    }

    public void broadcast(Session session, String message) throws IOException {
        for (Session s : sessionList) {
            s.getBasicRemote().sendText("session" + session.getId() + ": " + message);
        }
    }

    public void removeClient(Session session) {
        sessionList.remove(session);
        log.debug("remove() sessionList.size()=" + sessionList.size());
    }

}
