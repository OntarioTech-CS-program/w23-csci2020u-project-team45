package com.game.gameserver;

public interface Constants {
    public enum Status { WAITING, START, END } // game status
    public enum Actions { JOIN, PLAY, RESTART, QUIT } // game status
    public enum Items {COIN, DIAMOND, SNOWFLAKE, ENEMY}
    public enum Choice {POINTS, LIVES}
    public static int START_SCORE = 0;
    public static int START_LIVES = 10;
    public static int MAX_PLAYERS = 2;
    public static int BOARD_SIZE = 10;
    public static int MAX_NUM_HIGH_SCORE = 5;

    public static String[][] GAME_PIECES = {
            {"coin", Choice.POINTS.name(), "10"},
            {"diamond", Choice.POINTS.name(), "50"},
            {"snowflake", Choice.LIVES.name(),"1"},
            {"enemy", Choice.LIVES.name(), "-1"}
    };

    public enum LEVEL{
        EASY(10),
        MEDIUM(12),
        HARD(14);

        private final int value;

        LEVEL(final int newValue) {
            value = newValue;
        }
        public int getValue() { return value; }
    }
}