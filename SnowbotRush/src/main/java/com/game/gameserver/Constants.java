package com.game.gameserver;

public interface Constants {
    public enum Status { WAITING, START, END } // game status
    public enum Actions { JOIN, PLAY, RESTART, QUIT } // game status
    public enum Items {COINS, DIAMONDS, SNOWFLAKES, ENEMY}
    public enum Choice {POINTS, LIVES}
    public static int START_SCORE = 0;
    public static int START_LIVES = 3;
    public static int MAX_PLAYERS = 2;
    public static int BOARD_SIZE = 10;
    public static String[][] GAME_PIECES = {
            {"coins", Choice.POINTS.name(), "10"},
            {"diamonds", Choice.POINTS.name(), "50"},
            {"snowflakes", Choice.LIVES.name(),"1"},
            {"enemy", Choice.LIVES.name(), "-1"}
    };

    public enum LEVEL{
        EASY(10),
        MEDIUM(20),
        HARD(30);

        private final int value;

        LEVEL(final int newValue) {
            value = newValue;
        }
        public int getValue() { return value; }
    }
}
