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

    public GamePiece getGamePiece (int row, int column) {
        return gamePieces[row][column];
    }

    public boolean isGameOver() {
        boolean blnGameOver = true;
        for (int row=0; row<gamePieces.length && !blnGameOver; row++) {
            for (int column=0; column<gamePieces[row].length && !blnGameOver; column++) {
                if (!gamePieces[row][column].isClaimed()) {
                    blnGameOver = false;
                }
            }
        }
        return blnGameOver;
    }
}
