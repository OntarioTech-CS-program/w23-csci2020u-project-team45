package com.game.gameserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.Session;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameServerHandler implements Runnable {
    private GameRoom gameRoom;
    private Session session;
    public GameServerHandler(GameRoom gameRoom, Session session) throws IOException {
        this.gameRoom = gameRoom;
        this.session = session;
    }

    public void run() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String val = getBoard(gameRoom.getGameBoard());
            session.getBasicRemote().sendText(val);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getBoard(GameBoard gameBoard) {
        String data = "";
        boolean blnDelimiter = false;
        if (gameBoard != null) {
            GamePiece[][] gamePieces = gameBoard.getGamePieces();
            data = "{\"type\": \"game\",\"pieces\": [";
            for (GamePiece[] gprow:gamePieces) {
                for (GamePiece gp:gprow) {
                    String piece = gp.getItem();
                    int x = gp.getxPos();
                    int y = gp.getyPos();
                    if (blnDelimiter) {
                        data += ",";
                    } else {
                        blnDelimiter = true;
                    }
                    data += "{";
                    data += "\"piece\": \"" + piece + "\",";
                    data += "\"row\": \"" + x + "\",";
                    data += "\"column\": \"" + y + "\"";
                    data += "}";
                }
            }
            data += "]}";
        }
        return data;
    }

}
