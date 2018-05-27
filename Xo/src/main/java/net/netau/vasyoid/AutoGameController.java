package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Game controller class.
 * Controls game state and handles players' moves.
 * Simulates a user play.
 * Has two difficulty levels.
 */
public abstract class AutoGameController extends GameController {

    protected final Random randomGenerator = new Random();
    private GameState autoState;
    protected final List<Integer> freeCells = new LinkedList<>();
    protected final MyView view;
    protected final Map<Integer, BoardState> isWinningPosition  = new HashMap<>();

    public AutoGameController(@NotNull MyView view, @NotNull CellState autoUnit) {
        this.view = view;
        this.autoState = autoUnit.equals(CellState.CROSS) ?
                GameState.CROSSES_MOVE : GameState.NOUGHTS_MOVE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        super.init();
        freeCells.clear();
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            freeCells.add(i);
        }
        autoState = autoState.equals(GameState.CROSSES_MOVE) ?
                GameState.NOUGHTS_MOVE : GameState.CROSSES_MOVE;
        if (autoState.equals(GameState.CROSSES_MOVE)) {
            autoMove();
        }
        calculateMoves(board);
    }

    protected int boardToMask(@NotNull CellState[][] board) {
        int mask = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                mask = mask * 3 + board[i][j].getValue();
            }
        }
        return mask;
    }

    @NotNull
    protected CellState[][] maskToBoard(int mask) {
        CellState[][] board = new CellState[BOARD_SIZE][BOARD_SIZE];
        for (int i = BOARD_SIZE - 1; i >= 0; i--) {
            for (int j = BOARD_SIZE - 1; j >= 0; j--) {
                board[i][j] = CellState.parseValue(mask % 3);
                mask /= 3;
            }
        }
        return board;
    }

    @NotNull
    protected CellState whoMovesNext(@NotNull CellState[][] board) {
        int crosses = 0, noughts = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].equals(CellState.CROSS)) {
                    crosses++;
                } else if (board[i][j].equals(CellState.NOUGHT)) {
                    noughts++;
                }
            }
        }
        switch (crosses - noughts) {
            case 1:
                return CellState.NOUGHT;
            case 0:
                return CellState.CROSS;
            default:
                throw new IllegalArgumentException("X: " + crosses + "; O: " + noughts);
        }
    }

    protected void calculateMoves(@NotNull CellState[][] board) {
        int mask = boardToMask(board);
        if (isWinningPosition.containsKey(mask)) {
            return;
        }
        CellState next = whoMovesNext(board);
        for (int i = 1; i <= 2; i++) {
            BoardState alreadyKnown = isWin(CellState.parseValue(i), board);
            if (alreadyKnown.equals(BoardState.WIN)) {
                isWinningPosition.put(mask, next.getValue() == i ? BoardState.WIN : BoardState.LOSE);
                return;
            } else if (alreadyKnown.equals(BoardState.DRAW)) {
                isWinningPosition.put(mask, BoardState.DRAW);
                return;
            }
        }
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].equals(CellState.EMPTY)) {
                    board[i][j] = next;
                    calculateMoves(board);
                    BoardState nextLabel = isWinningPosition.get(boardToMask(board));
                    board[i][j] = CellState.EMPTY;
                    switch (nextLabel) {
                        case LOSE:
                            isWinningPosition.put(mask, BoardState.WIN);
                            return;
                        case DRAW:
                            isWinningPosition.put(mask, BoardState.DRAW);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        if (!isWinningPosition.containsKey(mask)) {
            isWinningPosition.put(mask, BoardState.LOSE);
        }
    }

    protected abstract void autoMove();

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public CellState move(int row, int col) {
        CellState result = super.move(row, col);
        Integer cellId = row * BOARD_SIZE + col;
        freeCells.remove(cellId);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void turn() {
        if (currentGameState.equals(autoState)) {
            autoMove();
        }
    }

    /**
     * Difficulty levels.
     */
    public enum Level {
        EASY, HARD
    }
}
