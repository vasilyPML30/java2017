package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base game controller class.
 * Controls game state and handles players' moves.
 */
public class GameController {

    public static int BOARD_SIZE = 3;

    protected CellState[][] board = new CellState[3][3];
    protected GameState currentGameState = GameState.CROSSES_MOVE;
    private boolean gameOver;

    /**
     * Clear the board and game state.
     */
    public void init() {
        gameOver = false;
        currentGameState = GameState.CROSSES_MOVE;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = CellState.EMPTY;
            }
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    @NotNull
    protected BoardState isWin(@NotNull CellState potentialWinner, @NotNull CellState[][] board) {
        boolean someoneWon = false;
        boolean draw = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            boolean rowWin = true, colWin = true;
            for (int j = 0; j < BOARD_SIZE; j++) {
                rowWin &= board[i][j].equals(potentialWinner);
                colWin &= board[j][i].equals(potentialWinner);
                draw &= !board[i][j].equals(CellState.EMPTY);
            }
            someoneWon |= rowWin | colWin;
        }
        boolean firstDiagWin = true, secondDiagWin = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            firstDiagWin &= board[i][i].equals(potentialWinner);
            secondDiagWin &= board[i][BOARD_SIZE - i - 1].equals(potentialWinner);
        }
        someoneWon |= firstDiagWin | secondDiagWin;
        if (someoneWon) {
            return BoardState.WIN;
        } else if (draw) {
            return BoardState.DRAW;
        } else {
            return BoardState.NOTHING;
        }
    }

    @NotNull
    public GameState getCurrentGameState() {
        return currentGameState;
    }

    private void updateGameState() {
        CellState potentialWinner = currentGameState.equals(GameState.CROSSES_MOVE) ?
                CellState.CROSS : CellState.NOUGHT;
        switch (isWin(potentialWinner, board)) {
            case WIN:
                gameOver = true;
                currentGameState = currentGameState.equals(GameState.CROSSES_MOVE) ?
                        GameState.CROSSES_WIN : GameState.NOUGHTS_WIN;
                break;
            case DRAW:
                gameOver = true;
                currentGameState = GameState.DRAW;
                break;
            case NOTHING:
                currentGameState = currentGameState.equals(GameState.CROSSES_MOVE) ?
                        GameState.NOUGHTS_MOVE : GameState.CROSSES_MOVE;
                break;
            default:
                break;
        }
    }

    /**
     * Try to set a sign to a specified cell.
     * @param row cell vertical position.
     * @param col cell horizontal position.
     * @return new state of the specified cell.
     */
    public CellState move(int row, int col) {
        if (board[row][col] != CellState.EMPTY) {
            return board[row][col];
        }
        switch (currentGameState) {
            case CROSSES_MOVE:
                board[row][col] = CellState.CROSS;
                updateGameState();
                break;
            case NOUGHTS_MOVE:
                board[row][col] = CellState.NOUGHT;
                updateGameState();
                break;
            default:
                break;
        }
        return board[row][col];
    }

    /**
     * Report that a player finished their move.
     */
    public void turn() {}

    /**
     * Types of states the game can have.
     */
    public enum GameState {
        CROSSES_MOVE("Ходят X"),
        NOUGHTS_MOVE("Ходят O"),
        CROSSES_WIN("Победили X!"),
        NOUGHTS_WIN("Победили O!"),
        DRAW("Ничья");

        private String text;

        GameState(@NotNull String text) {
            this.text = text;
        }

        @NotNull
        @Override
        public String toString() {
            return text;
        }
    }

    /**
     * Types of values a cell can have.
     */
    public enum CellState {
        EMPTY("", 0),
        CROSS("X", 1),
        NOUGHT("O", 2);

        private String text;
        private int value;

        CellState(@NotNull String text, int value) {
            this.text = text;
            this.value = value;
        }

        @NotNull
        @Override
        public String toString() {
            return text;
        }

        public int getValue() {
            return value;
        }

        @NotNull
        public static CellState parseValue(int value) {
            switch (value) {
                case 0:
                    return EMPTY;
                case 1:
                    return CROSS;
                case 2:
                    return NOUGHT;
                default:
                    throw new IllegalArgumentException();
            }
        }

        @NotNull
        public CellState getOpposite() {
            switch (this) {
                case CROSS:
                    return NOUGHT;
                case NOUGHT:
                    return CROSS;
                default:
                    return EMPTY;
            }
        }
    }

    /**
     * Types of values the board can have.
     */
    public enum BoardState {
        WIN, LOSE, DRAW, NOTHING
    }
}
