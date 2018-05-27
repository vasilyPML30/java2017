package net.netau.vasyoid;

import org.junit.Test;

import static net.netau.vasyoid.AutoGameController.Level.EASY;
import static net.netau.vasyoid.AutoGameController.Level.HARD;
import static net.netau.vasyoid.GameController.CellState.CROSS;
import static net.netau.vasyoid.GameController.CellState.EMPTY;
import static net.netau.vasyoid.GameController.CellState.NOUGHT;
import static net.netau.vasyoid.GameController.GameState.CROSSES_MOVE;
import static net.netau.vasyoid.GameController.GameState.CROSSES_WIN;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class AutoGameControllerTest {

    private final GameController.CellState[][] board = new GameController.CellState[3][3];
    private MyView view = mock(MyView.class);
    private AutoGameController controllerEasy, controllerHard;

    private void initializeControllers() {
        controllerHard = new HardGameController(view, CROSS);
        controllerEasy = new EasyGameController(view, NOUGHT);
        controllerHard.init();
        controllerEasy.init();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    private void initializeView(AutoGameController controller) {
        view = mock(MyView.class);
        doAnswer(invocation -> {
            int row = invocation.getArgument(0);
            int col = invocation.getArgument(1);
            assertEquals(EMPTY, board[row][col]);
            board[row][col] = NOUGHT;
            controller.move(row, col);
            return null;
        }).when(view).pressButton(any(Integer.class), any(Integer.class));
    }


    @Test
    public void testEasyBotMovesToEmptyCells() throws Exception {
        initializeControllers();
        initializeView(controllerEasy);
        for (int i = 0; i < 9; i++) {
            controllerEasy.turn();
        }
    }

    @Test
    public void testHardBotMovesToEmptyCells() throws Exception {
        initializeControllers();
        initializeView(controllerHard);
        for (int i = 0; i < 9; i++) {
            controllerHard.turn();
        }
    }

    @Test
    public void testHardBotPlaysBetterThanEasy() throws Exception {
        doAnswer(invocation -> {
            int row = invocation.getArgument(0);
            int col = invocation.getArgument(1);
            controllerHard.move(row, col);
            controllerEasy.move(row, col);
            assertNotEquals(CROSSES_WIN, controllerHard.getCurrentGameState());
            assertEquals(controllerEasy.getCurrentGameState(), controllerHard.getCurrentGameState());
            if (controllerEasy.getCurrentGameState().equals(CROSSES_MOVE)) {
                controllerEasy.turn();
            } else {
                controllerHard.turn();
            }
            return null;
        }).when(view).pressButton(any(Integer.class), any(Integer.class));
        initializeControllers();
    }

    @Test
    public void testHardBotCanLoose() throws Exception {
        doAnswer(invocation -> {
            int row = invocation.getArgument(0);
            int col = invocation.getArgument(1);
            controllerHard.move(row, col);
            if (row == 2 || col == 2) {
                controllerHard.move(0, 2);
            } else {
                controllerHard.move(2, 2);
            }
            assertEquals(CROSSES_WIN, controllerHard.getCurrentGameState());
            return null;
        }).when(view).pressButton(any(Integer.class), any(Integer.class));
        controllerHard = new HardGameController(view, CROSS);
        controllerHard.init();
        controllerHard.move(0, 0);
        controllerHard.move(0, 1);
        controllerHard.move(1, 1);
        controllerHard.move(2, 1);
        controllerHard.move(2, 0);
        controllerHard.turn();
    }
}