package com.game.gameserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.game.util.FileReaderWriter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This is a class that has services
 * In our case, we are using this to generate unique room IDs**/
@WebServlet(name = "gameServlet", value = "/game-servlet")
public class GameServlet extends HttpServlet {
    private String message;

    //static so this set is unique
    public static Set<String> games = new HashSet<>();

    // method generates unique room codes
    public String generatingRandomUpperAlphanumericString(int length) {
        String generatedString = RandomStringUtils.randomAlphanumeric(length).toUpperCase();
        // generating unique room code
        while (games.contains(generatedString)){
            generatedString = RandomStringUtils.randomAlphanumeric(length).toUpperCase();
        }
        games.add(generatedString);

        return generatedString;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        // send the random code as the response's content
        PrintWriter out = response.getWriter();
        out.println(generatingRandomUpperAlphanumericString(5));
    }

    public void destroy() {
        try {
            HighScore hs = HighScore.getInstance();
            Map<String, Long> scores = hs.getAllScores();

            String data = "[";
            boolean blnDelimiter = false;
            for (Map.Entry<String, Long> record:scores.entrySet()) {
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
            data += "]";

            URL url = this.getClass().getClassLoader().getResource(Constants.HIGH_SCORE_DIR);
            File mainDir = new File(url.toURI());
            FileReaderWriter.saveNewFile(mainDir,Constants.HIGH_SCORE_FILE,data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}