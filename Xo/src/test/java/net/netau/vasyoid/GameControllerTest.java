package net.netau.vasyoid;

import org.junit.Test;

import static net.netau.vasyoid.GameController.CellState.CROSS;
import static net.netau.vasyoid.GameController.CellState.EMPTY;
import static net.netau.vasyoid.GameController.CellState.NOUGHT;
import static net.netau.vasyoid.GameController.GameState.*;
import static org.junit.Assert.*;

public class GameControllerTest {

    @Test
    public void testDraw() throws Exception {
        GameController controller = new GameController();
        controller.init();
        assertFalse(controller.isGameOver());
        assertEquals(CROSSES_MOVE, controller.getCurrentGameState());
        assertEquals(CROSS, controller.move(0, 0));
        assertEquals(NOUGHTS_MOVE, controller.getCurrentGameState());
        assertEquals(NOUGHT, controller.move(1, 0));
        assertEquals(CROSSES_MOVE, controller.getCurrentGameState());
        assertEquals(CROSS, controller.move(0, 1));
        assertEquals(NOUGHTS_MOVE, controller.getCurrentGameState());
        assertEquals(NOUGHT, controller.move(1, 1));
        assertEquals(CROSSES_MOVE, controller.getCurrentGameState());
        assertFalse(controller.isGameOver());
        assertEquals(CROSS, controller.move(1, 2));
        assertEquals(NOUGHTS_MOVE, controller.getCurrentGameState());
        assertEquals(NOUGHT, controller.move(0, 2));
        assertEquals(CROSSES_MOVE, controller.getCurrentGameState());
        assertEquals(CROSS, controller.move(2, 0));
        assertEquals(NOUGHTS_MOVE, controller.getCurrentGameState());
        assertEquals(NOUGHT, controller.move(2, 1));
        assertEquals(CROSSES_MOVE, controller.getCurrentGameState());
        assertFalse(controller.isGameOver());
        assertEquals(CROSS, controller.move(2, 2));
        assertEquals(DRAW, controller.getCurrentGameState());
        assertTrue(controller.isGameOver());
    }

    @Test
    public void testCrossesWin() throws Exception {
        GameController controller = new GameController();
        controller.init();
        controller.move(0, 0);
        controller.move(1, 0);
        controller.move(0, 1);
        controller.move(1, 1);
        controller.move(0, 2);
        assertEquals(CROSSES_WIN, controller.getCurrentGameState());
        assertTrue(controller.isGameOver());
    }

    @Test
    public void testNoughtsWin() throws Exception {
        GameController controller = new GameController();
        controller.init();
        controller.move(0, 0);
        controller.move(1, 0);
        controller.move(0, 1);
        controller.move(1, 1);
        controller.move(2, 0);
        controller.move(1, 2);
        assertEquals(NOUGHTS_WIN, controller.getCurrentGameState());
        assertTrue(controller.isGameOver());
    }

    @Test
    public void testMoveToOccupied() throws Exception {
        GameController controller = new GameController();
        controller.init();
        controller.move(0, 0);
        controller.turn();
        assertEquals(CROSS, controller.move(0, 0));
        assertEquals(NOUGHTS_MOVE, controller.getCurrentGameState());
        assertEquals(CROSS, controller.move(0, 0));
        assertEquals(NOUGHTS_MOVE, controller.getCurrentGameState());
    }

    @Test
    public void testMoveAfterGameOver() throws Exception {
        GameController controller = new GameController();
        controller.init();
        controller.move(0, 0);
        controller.move(1, 0);
        controller.move(0, 1);
        controller.move(1, 1);
        controller.move(0, 2);
        assertEquals(CROSS, controller.move(0, 0));
        assertEquals(CROSSES_WIN, controller.getCurrentGameState());
        assertTrue(controller.isGameOver());
        assertEquals(NOUGHT, controller.move(1, 0));
        assertEquals(CROSSES_WIN, controller.getCurrentGameState());
        assertTrue(controller.isGameOver());
        assertEquals(EMPTY, controller.move(2, 2));
        assertEquals(CROSSES_WIN, controller.getCurrentGameState());
        assertTrue(controller.isGameOver());
    }

    @Test
    public void testGameStateToString() throws Exception {
        assertEquals("Ходят X", CROSSES_MOVE.toString());
        assertEquals("Ходят O", NOUGHTS_MOVE.toString());
        assertEquals("Победили X!", CROSSES_WIN.toString());
        assertEquals("Победили O!", NOUGHTS_WIN.toString());
        assertEquals("Ничья", DRAW.toString());
    }

    @Test
    public void testCellStateToString() throws Exception {
        assertEquals("", EMPTY.toString());
        assertEquals("X", CROSS.toString());
        assertEquals("O", NOUGHT.toString());
    }

    @Test
    public void testCellStateGetValue() throws Exception {
        assertEquals(0, EMPTY.getValue());
        assertEquals(1, CROSS.getValue());
        assertEquals(2, NOUGHT.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCellStateParseValue() throws Exception {
        assertEquals(EMPTY, GameController.CellState.parseValue(0));
        assertEquals(CROSS, GameController.CellState.parseValue(1));
        assertEquals(NOUGHT, GameController.CellState.parseValue(2));
        //noinspection ResultOfMethodCallIgnored
        GameController.CellState.parseValue(-1);
    }

    @Test
    public void testCellStateGetOpposite() throws Exception {
        assertEquals(EMPTY, EMPTY.getOpposite());
        assertEquals(NOUGHT, CROSS.getOpposite());
        assertEquals(CROSS, NOUGHT.getOpposite());
    }
}