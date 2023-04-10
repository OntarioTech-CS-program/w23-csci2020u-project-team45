package com.game.gameserver;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@ServerEndpoint(value="/ws/game/")
public class GameServer {

    private static Map<String, String> players = new HashMap<String, String>(); // list of all players and the game room they have joined
    private static List<GameRoom> gameRooms = new ArrayList<>(); // list of all the game rooms

    @OnOpen
    public void open(Session session) throws IOException, EncodeException {
        String id = session.getId(); // unique id of the user
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
        String error = null;
        if ("enter".equalsIgnoreCase(type)) {
            enterGameRoom(comm,session);
        } else if ("select".equalsIgnoreCase(type)) {
            String gameID = players.get(userID);
            if (gameID != null) {
                GameRoom gameRoom = getGameRoom(gameID);
                if (gameRoom != null && gameRoom.getPlayer(userID)!=null) {
                    if (Constants.Status.START.equals(gameRoom.getGameStatus())) {
                        String row = (String) jsonmsg.get("row");
                        String column = (String) jsonmsg.get("column");
                        int[] selection = new int[2];
                        selection[0] = Integer.parseInt(row);
                        selection[1] = Integer.parseInt(column);
                        PlayerServerHandler psh = new PlayerServerHandler(gameRoom, selection, session);
                        psh.run();
                    } else if (Constants.Status.WAITING.equals(gameRoom.getGameStatus())) {
                        error = "{\"type\": \"error\", \"message\":\"Please wait for player to connect.\"}";
                    } else {
                        error = "{\"type\": \"error\", \"message\":\"Please join a game to play.\"}";
                    }
                } else {
                    error = "{\"type\": \"error\", \"message\":\"Please join a game to play.\"}";
                }
            } else {
                error = "{\"type\": \"error\", \"message\":\"Please join a game to play.\"}";
            }
        } else {
            error = "{\"type\": \"error\", \"message\":\"Command not recognized.\"}";
        }
        if (error != null) {
            session.getBasicRemote().sendText(error);
        }
    }

    public void enterGameRoom(String comm,Session session) throws IOException, EncodeException {
        String id = session.getId(); // unique id of the user
        JSONObject jsonmsg = new JSONObject(comm);
        String name = (String) jsonmsg.get("name"); // name user has entered for their player
        if (name != null) {
            session.getBasicRemote().sendText("{\"type\": \"info\", \"message\":\"Welcome " + name.toUpperCase() +"!\"}");
            String gameID = players.get(id); // to fetch if the user belongs to any game
            if (gameID != null) {
                removeUserFromGame(id,gameID,session);
            }
            String level = (String) jsonmsg.get("level");
            GameRoom gameRoom = findAGameRoomForPlayer(id,name,level);
            players.put(id, gameRoom.getGameID());
            if (gameRoom.getNumOfPlayers() == Constants.MAX_PLAYERS) {
                session.getBasicRemote().sendText("{\"type\": \"info\", \"message\":\"Get Ready " + name.toUpperCase() +", Game Is About To Start!\"}");
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
                gameRoom.setGameStatus(Constants.Status.START);
            } else {
                session.getBasicRemote().sendText("{\"type\": \"info\", \"message\":\"Waiting For Other Players To Connect...\"}");
            }
        } else {
            session.getBasicRemote().sendText("{\"type\": \"error\", \"message\":\"Please provide your player name to play the game.\"}");
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
        String uriAPI = "http://localhost:8080/SnowbotRush-1.0-SNAPSHOT/game-servlet";
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