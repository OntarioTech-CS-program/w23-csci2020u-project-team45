package com.game.gameserver;

import java.util.Random;

public class GameBoard {
    GamePiece[][] gamePieces; // array to store game pieces
    int size; // declaring size of game board

    // method to create the game board
    public GameBoard(Constants.LEVEL level) {
        this.size = level.getValue(); // get the size of the game piece
        this.gamePieces = new GamePiece[this.size][this.size]; // create the game board size
        generateGameBoard();
    }

    // generating game board dynamically
    private void generateGameBoard() {
        Random random = new Random(); // randomly generating game board pieces
        for (int row=0; row<gamePieces.length; row++) {
            for (int column=0; column<gamePieces[row].length;column++) {
                int itemNum = random.nextInt(Constants.Items.values().length);
                gamePieces[row][column] = new GamePiece(itemNum);
                gamePieces[row][column].setPosition(row,column);
            }
        }
    }

    // method to get all game pieces for game board
    public GamePiece[][] getGamePieces() {
        return this.gamePieces;
    }

    // method to get a game piece
    public GamePiece getGamePiece (int row, int column) {
        return this.gamePieces[row][column];
    }

    // method to get the size of the game board
    public int getSize() {
        return this.size;
    }

    // method called when game ends
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
