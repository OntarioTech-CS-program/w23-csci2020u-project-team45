package com.game.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.gameserver.GameRoom;
import com.game.gameserver.HighScore;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
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
            val = objectMapper.writeValueAsString(HighScore.getInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Response myResp = Response.status(200).header("Access-Control-Allow-Origin", "http://localhost:8448")
                .header("Content-Type", "application/json")
                .entity(val)
                .build();
        return myResp;
    }
}