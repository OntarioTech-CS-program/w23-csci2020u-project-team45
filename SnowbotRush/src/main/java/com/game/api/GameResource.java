package com.game.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.gameserver.Constants;
import com.game.gameserver.HighScore;
import com.game.util.FileReaderWriter;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Path("/game")
public class GameResource {

    @GET
    @Path("/highscore")
    @Produces("application/json")
    public Response gameHighScore() {
        String val = "";
        //need to create object to build
        try {
            HighScore hs = HighScore.getInstance();
            if(hs.isEmpty()) {
                getHighScoresFromFile(hs);
            }
            val = getHighScores(hs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Response myResp = Response.status(200).header("Content-Type", "application/json")
                .entity(val)
                .build();
        return myResp;
    }

    public void getHighScoresFromFile(HighScore hs) throws JsonProcessingException {
        URL url = this.getClass().getClassLoader().getResource(Constants.HIGH_SCORE_DIR);
        String highScores = "";
        File mainDir = null;
        try {
            mainDir = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try {
            highScores = FileReaderWriter.readHighScoresFile(mainDir,Constants.HIGH_SCORE_FILE);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(highScores);
        Iterator<JsonNode> iterator = jsonNode.elements();
        while(iterator.hasNext()) {
            JsonNode js = iterator.next();
            String name = js.findValue("name").asText();
            String score = js.findValue("score").asText();
            hs.setHighScore(name, Long.parseLong(score));
        }
    }

    private String getHighScores(HighScore highScore) {
        List<Map.Entry<String, Long>> values = highScore.getSortedByScore();
        //String data = "{\"type\": \"scores\",\"scores\": [";
        String data = "{\"scores\": [";
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
                data += "\"rank\": \"" + count + "\",";
                data += "\"name\": \"" + name + "\",";
                data += "\"score\": \"" + score.longValue() + "\"";
                data += "}";
            }
        }
        data += "]}";
        return data;
    }
}