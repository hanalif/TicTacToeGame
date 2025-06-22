package hana.lipschutz.math_exercises;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Model {

    public static final int SIZE = 3;
    public static final String PLAYER = "X";
    public static final String COMPUTER = "O";

    private String[][] board;
    private boolean gameOver;

    public Model() {
        board = new String[SIZE][SIZE];
        resetGame();
    }

    public boolean makePlayerMove(int row, int col) {
        if (!gameOver && board[row][col].isEmpty()) {
            board[row][col] = PLAYER;
            return true;
        }
        return false;
    }

    public boolean makeComputerMove() {
        if (gameOver) return false;

        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j].isEmpty()) {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }

        if (!emptyCells.isEmpty()) {
            int[] move = emptyCells.get(new Random().nextInt(emptyCells.size()));
            board[move[0]][move[1]] = COMPUTER;
            return true;
        }

        return false;
    }

    public boolean checkWinner(String symbol) {
        // שורות ועמודות
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0].equals(symbol) && board[i][1].equals(symbol) && board[i][2].equals(symbol)) return true;
            if (board[0][i].equals(symbol) && board[1][i].equals(symbol) && board[2][i].equals(symbol)) return true;
        }

        // אלכסונים
        if (board[0][0].equals(symbol) && board[1][1].equals(symbol) && board[2][2].equals(symbol)) return true;
        if (board[0][2].equals(symbol) && board[1][1].equals(symbol) && board[2][0].equals(symbol)) return true;

        return false;
    }

    public boolean isFull() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (board[i][j].isEmpty())
                    return false;
        return true;
    }

    public void resetGame() {
        gameOver = false;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board[i][j] = "";
    }

    public String getSymbolAt(int row, int col) {
        return board[row][col];
    }

    public void setGameOver(boolean value) {
        gameOver = value;
    }

    public boolean isGameOver() {
        return gameOver;
    }

}
