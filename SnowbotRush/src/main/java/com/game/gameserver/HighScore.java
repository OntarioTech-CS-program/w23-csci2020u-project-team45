package com.game.gameserver;

import java.util.*;
import java.util.stream.Stream;

public class HighScore {
    private Map<String, Long> scores = new HashMap<String, Long>();
    private static HighScore highScore = null;
    private HighScore() {}

    // static method to create instance of Singleton class
    public static synchronized HighScore getInstance()
    {
        if (highScore == null) {
            highScore = new HighScore();
        }
        highScore.setHighScore("Kate", 200);
        highScore.setHighScore("Jack", 100);
        highScore.setHighScore("Sally", 300);
        return highScore;
    }

    public void setHighScore(String playerName, long score) {
        this.scores.put(playerName.toUpperCase(), Long.valueOf(score));
    }

    public Map<String, Long> getAllScores() {
        return scores;
    }

    public long getScore(String playerName) {
        Long score = Long.valueOf(0);
        if (playerName != null) {
            score = scores.get(playerName.toUpperCase());
        }
        return score.longValue();
    }

    public List<Map.Entry<String, Long>> getSortedByScore() {
        Stream<Map.Entry<String,Long>> sorted = scores.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return sorted.toList();
    }
}