package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

/**
 * Difficult implementation of an automatic game controller.
 */
public class HardGameController extends AutoGameController {

    public HardGameController(@NotNull MyView view, @NotNull CellState autoUnit) {
        super(view, autoUnit);
    }

    /**
     * {@inheritDoc}
     * Always tries to win or at least not loose.
     */
    @Override
    protected void autoMove()  {
        int curMask = boardToMask(board);
        curMask = boardToMask(maskToBoard(curMask));
        calculateMoves(maskToBoard(curMask));
        BoardState curLabel = isWinningPosition.get(curMask);
        if (curLabel.equals(BoardState.LOSE)) {
            int cellId = freeCells.get(freeCells.get(0));
            view.pressButton(cellId / BOARD_SIZE, cellId % BOARD_SIZE);
        } else {
            for (int cellId : freeCells) {
                board[cellId / BOARD_SIZE][cellId % BOARD_SIZE] = whoMovesNext(board);
                BoardState nextLabel = isWinningPosition
                        .getOrDefault(boardToMask(board), BoardState.WIN);
                if ((curLabel.equals(BoardState.DRAW) && nextLabel.equals(BoardState.DRAW)) ||
                        (curLabel.equals(BoardState.WIN) && nextLabel.equals(BoardState.LOSE))) {
                    board[cellId / BOARD_SIZE][cellId % BOARD_SIZE] = CellState.EMPTY;
                    view.pressButton(cellId / BOARD_SIZE, cellId % BOARD_SIZE);
                    return;
                }
                board[cellId / BOARD_SIZE][cellId % BOARD_SIZE] = CellState.EMPTY;
            }
        }
    }
}
