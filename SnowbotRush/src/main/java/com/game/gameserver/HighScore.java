package com.game.gameserver;

import java.util.HashMap;
import java.util.Map;

public class HighScore {
    private Map<String, String> players = new HashMap<String, String>();
    private static HighScore highScore = null;
    private HighScore() {}

    // static method to create instance of Singleton class
    public static synchronized HighScore getInstance()
    {
        if (highScore == null) {
            highScore = new HighScore();
        }
        return highScore;
    }

}
