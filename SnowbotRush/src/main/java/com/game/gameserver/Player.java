package com.game.gameserver;

/**
 * This class represents the data you may need to store about a Chat room
 * You may add more method or attributes as needed
 * **/
public class Player {
    private String id; // created id for player
    private String name; // player name
    private long score; // current score of player
    private int lives; // number of lives player has

    // constructor
    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        this.score = Constants.START_SCORE;
        this.lives = Constants.START_LIVES;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score += score;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        if (this.lives < Constants.START_LIVES) {
            this.lives += lives;
        }
    }

    public boolean hasLives() {
        return (lives>0);
    }
}