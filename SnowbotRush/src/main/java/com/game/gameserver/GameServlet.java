package com.game.gameserver;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
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
        while (games.contains(generatedString)) {
            generatedString = RandomStringUtils.randomAlphanumeric(length).toUpperCase();
        }
        games.add(generatedString);

        return generatedString;
    }
}
