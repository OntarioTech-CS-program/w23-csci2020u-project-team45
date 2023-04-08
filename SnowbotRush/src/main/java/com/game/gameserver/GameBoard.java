package com.game.gameserver;

import java.util.Random;

public class GameBoard {
    GamePiece[][] gamePieces;

    public GameBoard(Constants.LEVEL level) {
        gamePieces = new GamePiece[level.getValue()][level.getValue()]; // create the game board size
        generateGameBoard();
    }

    // generating game board dynamically
    private void generateGameBoard() {
        int row = 0;
        for (GamePiece[] gprow:gamePieces) {
            int column = 0;
            for (GamePiece gp:gprow) {
                Random random = new Random();
                int itemNum = random.nextInt(Constants.Items.values().length);
                gp = new GamePiece(itemNum);
                gp.setPosition(row,column);
                column++;
            }
            row++;
        }
    }

    public GamePiece[][] getGamePieces() {
        return gamePieces;
    }
}
