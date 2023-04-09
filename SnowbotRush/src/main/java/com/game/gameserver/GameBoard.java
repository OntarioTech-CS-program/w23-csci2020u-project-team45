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
        Random random = new Random();
        for (int row=0; row<gamePieces.length; row++) {
            for (int column=0; column<gamePieces[row].length;column++) {
                int itemNum = random.nextInt(Constants.Items.values().length);
                gamePieces[row][column] = new GamePiece(itemNum);
                gamePieces[row][column].setPosition(row,column);
            }
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
