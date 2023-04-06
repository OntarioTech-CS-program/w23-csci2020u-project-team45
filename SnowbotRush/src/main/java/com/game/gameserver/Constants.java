package com.game.gameserver;

public interface Constants {
    public enum Status { WAITING, START, END } // game status
    public enum Actions { JOIN, PLAY, RESTART, QUIT } // game status
    public static int START_SCORE = 0;
    public static int START_LIVES = 3;
    public static int MAX_PLAYERS = 2;
}
