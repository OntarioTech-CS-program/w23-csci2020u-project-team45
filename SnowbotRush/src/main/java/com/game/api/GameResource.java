package com.game.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.gameserver.GameRoom;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/status")
public class GameResource {
    @GET
    @Produces("application/json")
    public Response status() {
        String val = "";
        //need to create object to build
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            val = objectMapper.writeValueAsString(GameRoom.getInstance());
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