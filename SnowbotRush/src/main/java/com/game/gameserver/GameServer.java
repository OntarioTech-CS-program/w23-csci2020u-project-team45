package com.game.gameserver;

import com.game.api.GameResource;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint(value="/ws/game/")
public class GameServer {

    private Map<String, String> players = new HashMap<String, String>();
    private GameRoom gameRoom = GameRoom.getInstance();

    @OnOpen
    public void open(String comm, Session session) throws IOException, EncodeException {
        int numOfPlayers = gameRoom.getNumOfPlayers();
        if (numOfPlayers < Constants.MAX_PLAYERS) {
            JSONObject jsonmsg = new JSONObject(comm);
            String type = (String) jsonmsg.get("action");
            String id = (String) jsonmsg.get("id");
            String name = (String) jsonmsg.get("name");
            if (Constants.Actions.JOIN.equals(Constants.Actions.valueOf(type))) {
                Player player = new Player(id, name);
                gameRoom.addPlayer(player);
                numOfPlayers = gameRoom.getNumOfPlayers();
                if (numOfPlayers == Constants.MAX_PLAYERS) {
                    for (Session peer : session.getOpenSessions()) {
                        peer.getBasicRemote().sendText("{\"type\": \"info\", \"message\":\"Get Ready, game is about to start.\"}");
                    }
                    // TO DO: create the board game and broadcast to both players
                } else {
                    session.getBasicRemote().sendText("{\"type\": \"info\", \"message\":\"Waiting for player 2 to connect.\"}");
                }
            }
        } else {
            session.getBasicRemote().sendText("{\"type\": \"error\", \"message\":\"Game Room is full, please wait your turn.\"}");
        }
    }

    @OnClose
    public void close(Session session) throws IOException, EncodeException {
        String playerId = session.getId();
        if (players.containsKey(playerId)) {
            String username = players.get(playerId);
            players.remove(playerId);
            if (!"".equalsIgnoreCase(username)) {
                for (Session peer : session.getOpenSessions()) { //broadcast this person left the server
                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): " + username + " left the chat room.\"}");
                }
            }
        }
    }

    @OnMessage
    public void handleMessage(String comm, Session session) throws IOException, EncodeException {
        String userID = session.getId(); // my id
        JSONObject jsonmsg = new JSONObject(comm);
        String type = (String) jsonmsg.get("type");
        String message = (String) jsonmsg.get("msg");

        if (!"".equalsIgnoreCase(message)) {
            if (players.containsKey(userID)) { // not their first message
                String username = players.get(userID);
                for (Session peer : session.getOpenSessions()) {
                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(" + username + "): " + message + "\"}");
                }
            } else { //first message is their username
                players.put(userID, message);
                session.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server ): Welcome, " + message + "!\"}");
                for (Session peer : session.getOpenSessions()) {
                    // only announce to those in the same room as me, excluding myself
                    if (!peer.getId().equals(userID)) {
                        peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): " + message + " joined the chat room.\"}");
                    }
                }
            }
        }
    }
}