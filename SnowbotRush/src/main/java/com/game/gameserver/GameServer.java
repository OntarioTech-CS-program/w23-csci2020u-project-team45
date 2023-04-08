package com.game.gameserver;

import com.game.api.GameResource;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@ServerEndpoint(value="/ws/game/")
public class GameServer {

    private Map<String, String> players = new HashMap<String, String>(); // list of all players and the game room they have joined
    private List<GameRoom> gameRooms = new ArrayList<>(); // list of all the game rooms

    @OnOpen
    public void open(String comm, Session session) throws IOException, EncodeException {
        String id = session.getId(); // unique id of the user
        JSONObject jsonmsg = new JSONObject(comm);
        String name = (String) jsonmsg.get("name"); // name user has entered for their player
        if (name != null) {
            String gameID = players.get(id); // to fetch if the user belongs to any game
            if (gameID != null) {
                removeUserFromGame(id,gameID,session);
            }
            String level = (String) jsonmsg.get("level");
            GameRoom gameRoom = findAGameRoomForPlayer(id,name,level);
            players.put(id, gameRoom.getGameID());
            if (gameRoom.getNumOfPlayers() == Constants.MAX_PLAYERS) {
                session.getBasicRemote().sendText("{\"type\": \"info\", \"message\":\"Get Ready, Game Is About To Start!\"}");
                Set<GameServerHandler> allPlayers = new HashSet<>();
                for (Session peer : session.getOpenSessions()) {
                    if (gameRoom.inRoom(peer.getId())) {
                        allPlayers.add(new GameServerHandler(gameRoom,peer));
                    }
                }
                Iterator iterator = allPlayers.iterator();
                while (iterator.hasNext()) {
                    ((GameServerHandler)iterator.next()).run();
                }
            } else {
                session.getBasicRemote().sendText("{\"type\": \"info\", \"message\":\"Waiting For Player 2 To Connect...\"}");
            }
        } else {
            session.getBasicRemote().sendText("{\"type\": \"error\", \"message\":\"Please provide your player name to play the game.\"}");
        }
    }

    @OnClose
    public void close(Session session) throws IOException, EncodeException {
        String playerId = session.getId();
        if (players.containsKey(playerId)) {
            String gameID = players.get(playerId);
            removeUserFromGame(playerId,gameID,session);
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

    public GameRoom getGameRoom(String gameID) {
        for (GameRoom gr:gameRooms) {
            if (gr.getGameID().equals(gameID)) {
                return gr;
            }
        }
        return null;
    }

    public void removeUserFromGame(String userID, String gameID, Session session) throws IOException {
        // remove user from player hash map
        // remove user from the game room
        // check if game room has no players left then remove the game room
        GameRoom gameRoom = getGameRoom(gameID);
        if (gameRoom != null) {
            players.remove(userID);
            Player player = gameRoom.getPlayer(userID);
            String playerName = player.getName();
            gameRoom.removePlayer(userID);
            if (gameRoom.getNumOfPlayers() == 0) {
                gameRooms.remove(gameRoom);
            } else {
                Map<String, Player> players = gameRoom.getPlayers();
                Set keys = players.keySet();
                for (Session peer : session.getOpenSessions()) { //broadcast this person left the server
                    if (keys.contains(peer.getId())) {
                        peer.getBasicRemote().sendText("{\"type\": \"info\", \"message\":\"" + playerName + " has left the game.\"}");
                    }
                }
            }
        }
    }

    public GameRoom findAGameRoomForPlayer(String userID, String playerName, String gameLevel) throws IOException {
        // loop through array list and see if there is any game available for user to join
        // if there is no game room then generate a new game id by calling the game servlet
        // create a new game room and add the user
        for (GameRoom gr:gameRooms) {
            if (gr.getNumOfPlayers() < Constants.MAX_PLAYERS) {
                if (gr.isLevel(gameLevel)) {
                    gr.addPlayer(userID,playerName);
                    return gr;
                }
            }
        }
        String gameID = getGameRoomID();
        GameRoom gameRoom = new GameRoom(gameID,gameLevel);
        gameRoom.addPlayer(userID,playerName);
        gameRooms.add(gameRoom);
        return gameRoom;
    }

    public String getGameRoomID() throws IOException {
        String uriAPI = "http://localhost:8080/SnowbotRush-1.0-SNAPSHOT/api/game-servlet";
        URL url = new URL(uriAPI);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "text/plain");
        con.setRequestProperty("Accept", "text/plain");
        // allows us to write content to the outputStream
        con.setDoOutput(false);

        //reading and printing response
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}