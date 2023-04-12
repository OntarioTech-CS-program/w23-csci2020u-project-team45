package com.game.gameserver;

// constants class created to hold all the static variables to be used in the application code
public final class Constants {
    private Constants(){} // default constructor
    public enum Status { WAITING, START, END } // game status
    public enum Items {COIN, JEWEL, DIAMOND, SNOWFLAKE, ENEMY, FREEZE} // game pieces
    public enum Choice {POINTS, LIVES, FREEZE} // game score type
    public static int START_SCORE = 0; // start score
    public static int START_LIVES = 7; // number of max lives
    public static int MAX_PLAYERS = 2; // number of max players that can play in the game
    public static int MAX_NUM_HIGH_SCORE = 5; // maximum number of players high scores to display
    public static String HIGH_SCORE_DIR = "/gameFiles"; // high score directory name
    public static String HIGH_SCORE_FILE = "highscores.json"; // high score file name
    public static String[][] GAME_PIECES = {
            {"coin", Choice.POINTS.name(), "10"},
            {"jewel", Choice.POINTS.name(), "25"},
            {"diamond", Choice.POINTS.name(), "50"},
            {"snowflake", Choice.LIVES.name(),"1"},
            {"enemy", Choice.LIVES.name(), "-1"},
            {"freeze", Choice.FREEZE.name(), "3"}
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
