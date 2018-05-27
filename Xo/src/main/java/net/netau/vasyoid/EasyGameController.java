package net.netau.vasyoid;

import org.jetbrains.annotations.NotNull;

/**
 * Easy implementation of an automatic game controller.
 */
public class EasyGameController extends AutoGameController {

    public EasyGameController(@NotNull MyView view, @NotNull CellState autoUnit) {
        super(view, autoUnit);
    }

    /**
     * {@inheritDoc}
     * Simply puts units into random available cells.
     */
    @Override
    protected void autoMove()  {
        int position = randomGenerator.nextInt(freeCells.size());
        int cellId = freeCells.get(position);
        view.pressButton(cellId / BOARD_SIZE, cellId % BOARD_SIZE);
    }
}
