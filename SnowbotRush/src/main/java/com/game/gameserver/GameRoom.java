package com.game.gameserver;

import java.util.*;

/**
 * This class represents the data you may need to store about a Chat room
 * You may add more method or attributes as needed
 * **/
public class GameRoom {
    private String gameID;
    private GameBoard gameBoard;
    private Map<String, Player> players = new HashMap<String, Player>(); // list of players playing the game
    private String winner; // for keeping track of who won the game
    private Constants.Status gameStatus = Constants.Status.WAITING;
    private Constants.LEVEL level;

    // constructor to create game room
    public GameRoom(String gameID, String level) {
        this.gameID = gameID;
        this.level = Constants.LEVEL.valueOf(level);
        this.gameBoard = new GameBoard(this.level);
        this.winner = null;
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

    public boolean hasWinner() {
        boolean blnFound = false;
        if (winner!=null) {
            blnFound = true;
        } else {
            List<Player> users = new ArrayList<>();
            for (Player player : players.values()) {
                if (player.hasLives()) {
                    users.add(player);
                }
            }
            if (users.size() == 1) {
                blnFound = true;
                gameStatus = Constants.Status.END;
                Player player = users.get(0);
                winner = player.getId();
            } else if (gameBoard.isGameOver()) {
                blnFound = true;
                gameStatus = Constants.Status.END;
                long highscore = 0;
                Iterator playerIterator = players.values().iterator();
                while (playerIterator.hasNext()) {
                    Player player = (Player) playerIterator.next();
                    if (highscore < player.getScore()) {
                        winner = player.getId();
                        highscore = player.getScore();
                    }
                }
            }
        }
        return blnFound;
    }


}