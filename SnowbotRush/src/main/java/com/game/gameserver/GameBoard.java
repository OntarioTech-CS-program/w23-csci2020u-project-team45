package com.game.gameserver;

import java.util.Random;

public class GameBoard {
    GamePiece[][] gamePieces;
    int size;

    public GameBoard(Constants.LEVEL level) {
        this.size = level.getValue();
        this.gamePieces = new GamePiece[this.size][this.size]; // create the game board size
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
        return this.gamePieces;
    }

    public GamePiece getGamePiece (int row, int column) {
        return this.gamePieces[row][column];
    }

    public int getSize() {
        return this.size;
    }
    public boolean isGameOver() {
        boolean blnGameOver = true;
        for (int row=0; row<gamePieces.length && blnGameOver; row++) {
            for (int column=0; column<gamePieces[row].length && blnGameOver; column++) {
                if (!gamePieces[row][column].isClaimed()) {
                    blnGameOver = false;
                }
            }
        }
        return blnGameOver;
    }
}