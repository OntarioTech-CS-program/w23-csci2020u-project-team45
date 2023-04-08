package com.game.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.gameserver.Constants;
import com.game.gameserver.HighScore;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Path("/game")
public class GameResource {
    public static Set<String> games = new HashSet<>();

    @GET
    @Path("/highscore")
    @Produces("application/json")
    public Response gameHighScore() {
        String val = "";
        //need to create object to build
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            val = getHighScores(HighScore.getInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Response myResp = Response.status(200).header("Access-Control-Allow-Origin", "http://localhost:8448")
                .header("Content-Type", "application/json")
                .entity(val)
                .build();
        return myResp;
    }

    private String getHighScores(HighScore highScore) {
        List<Map.Entry<String, Long>> values = highScore.getSortedByScore();
        String data = "{\"type\": \"scores\",\"scores\": [";
        boolean blnDelimiter = false;
        int count = 0;
        for (Map.Entry<String, Long> record:values) {
            count++;
            if (count <= Constants.MAX_NUM_HIGH_SCORE) {
                String name = record.getKey();
                Long score = record.getValue();
                if (blnDelimiter) {
                    data += ",";
                } else {
                    blnDelimiter = true;
                }
                data += "{";
                data += "\"name\": \"" + name + "\",";
                data += "\"score\": \"" + score.longValue() + "\"";
                data += "}";
            }
        }
        data += "]}";
        return data;
    }
}