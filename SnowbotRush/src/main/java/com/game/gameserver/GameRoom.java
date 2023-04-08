package com.game.gameserver;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the data you may need to store about a Chat room
 * You may add more method or attributes as needed
 * **/
public class GameRoom {
    private String gameID;
    private GameBoard gameBoard;
    private Map<String, Player> players = new HashMap<String, Player>(); // list of players playing the game
    private String winner; // for keeping track of who won the game
    private long timer; // for the game duration
    private Constants.Status gameStatus = Constants.Status.WAITING;
    private Constants.LEVEL level;

    // constructor to create game room
    public GameRoom(String gameID, String level) {
        this.gameID = gameID;
        this.level = Constants.LEVEL.valueOf(level);
        this.gameBoard = new GameBoard(this.level);
    }

    public String getGameID() {
        return gameID;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }

    public Constants.Status getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(Constants.Status gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void setPlayer(Player player) {
        String playerID = player.getId();
        removePlayer(playerID);
        players.put(playerID, player);
    }

    public Player getPlayer(String playerID) {
        return players.get(playerID);
    }

    public int getNumOfPlayers() {
        return players.size();
    }

    public void addPlayer(String playerID, String playerName) {
        if(!inRoom(playerID)) {
            Player player = new Player(playerID, playerName);
            this.players.put(playerID, player);
        }
    }

    public void removePlayer(String playerID){
        if(inRoom(playerID)){
            players.remove(playerID);
        }
    }

    public boolean inRoom(String playerID){
        return (players.containsKey(playerID));
    }

    public boolean isLevel(String level) {
        boolean blnMatch = false;
        if (this.level.name().equalsIgnoreCase(level)) {
            blnMatch = true;
        }
        return blnMatch;
    }
}