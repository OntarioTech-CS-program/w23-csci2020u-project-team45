package com.game.gameserver;


import jakarta.websocket.Session;
import java.io.IOException;

public class PlayerServerHandler implements Runnable {
    private GameRoom gameRoom;
    private Session session;
    private int[] selection;
    public PlayerServerHandler(GameRoom gameRoom, int[] selection, Session session) throws IOException {
        this.gameRoom = gameRoom;
        this.selection = selection;
        this.session = session;
    }

    public void run() {
        try {
            GameBoard gameBoard = gameRoom.getGameBoard();
            Player player = gameRoom.getPlayer(session.getId());
            if (player.hasLives()) {
                GamePiece gamePiece = gameBoard.getGamePiece(selection[0], selection[1]);
                if (!gamePiece.isClaimed()) {
                    gamePiece.setClaimed();
                    Constants.Choice choice = gamePiece.getType();
                    int value = gamePiece.getValue();
                    if (Constants.Choice.POINTS.equals(choice)) {
                        player.setScore(value);
                        session.getBasicRemote().sendText("{\"type\": \"score\", \"message\":\""+ player.getScore() +"\"}");
                        HighScore.getInstance().setHighScore(player.getName(), player.getScore());
                    } else if (Constants.Choice.LIVES.equals(choice)) {
                        player.setLives(value);
                        session.getBasicRemote().sendText("{\"type\": \"lives\", \"message\":\""+ player.getLives() +"\"}");
                        if (!player.hasLives()) {
                            session.getBasicRemote().sendText("{\"type\": \"lost\", \"message\":\"Sorry, no more lives.\"}");
                        }
                    }
                    checkWinner();
                } else {
                    session.getBasicRemote().sendText("{\"type\": \"failed\", \"message\":\"Game Piece is already claimed by other player.\"}");
                }
            } else {
                session.getBasicRemote().sendText("{\"type\": \"lost\", \"message\":\"Sorry, no more lives.\"}");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void checkWinner() throws IOException {
        if (gameRoom.hasWinner()) {
            String winner = gameRoom.getWinner();
            for (Session peer : session.getOpenSessions()) { //broadcast this person left the server
                if (peer.getId().equals(winner)) {
                    peer.getBasicRemote().sendText("{\"type\": \"winner\", \"message\":\"Congratulations!!! you have won the game.\"}");
                } else {
                    peer.getBasicRemote().sendText("{\"type\": \"winner\", \"message\":\"" + gameRoom.getPlayer(winner).getName() + " has won the game.\"}");
                }
            }
        }
    }
}
