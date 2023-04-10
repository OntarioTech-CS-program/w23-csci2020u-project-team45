package com.game.gameserver;

public class GamePiece {
    private String item; // game piece type (coin/diamond...)
    private Constants.Choice type; // whether is a point or a life
    private int value;
    private int xPos;
    private int yPos;
    private boolean claimed;

    public GamePiece(int piece) {
        this.item = Constants.GAME_PIECES[piece][0];
        this.type = Constants.Choice.valueOf(Constants.GAME_PIECES[piece][1]);
        this.value = Integer.parseInt(Constants.GAME_PIECES[piece][2]);
        this.xPos = -1; // -1 means not initialized
        this.yPos = -1; // -1 means not initialized
        this.claimed = true;
    }

    public void setPosition(int x, int y) {
        this.xPos = x;
        this.yPos = y;
        this.claimed = false;
    }

    public String getItem() {
        return item;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public boolean isClaimed() {
        return claimed;
    }

    public void setClaimed() {
        this.claimed = true;
    }
    public int getValue() {
        return value;
    }

    public Constants.Choice getType() {
        return type;
    }
}