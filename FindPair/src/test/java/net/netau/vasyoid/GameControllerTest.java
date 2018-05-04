package net.netau.vasyoid;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameControllerTest {

    private static int testSize = 10;
    private GameController controller;
    private int[][] data;

    @Before
    public void init() {
        data = new int[testSize][testSize];
        controller = new GameController(testSize);
        for (int i = 0; i < testSize; ++i) {
            for (int j = 0; j < testSize; ++j) {
                data[i][j] = controller.getValue(i, j);
            }
        }
    }

    @Test
    public void constructorCorrectnessTest() throws Exception {
        int[] count = new int[testSize * testSize / 2];
        for (int[] row : data) {
            for (int value : row) {
                assertTrue(0 <= value && value < count.length);
                count[value]++;
            }
        }
        for (int v : count) {
            assertTrue(v % 2 == 0);
        }
    }

    @Test
    public void singleButtonTest() throws Exception {
        assertEquals(GameController.MoveResult.SHOW, controller.handleButton(0, 0));
        assertEquals(GameController.MoveResult.HIDE, controller.handleButton(0, 0));
    }

    @Test
    public void twoButtonsEqualTest() throws Exception {
        controller.handleButton(0, 0);
        for (int i = 0; i < testSize; ++i) {
            for (int j = 0; j < testSize; ++j) {
                if ((i != 0 || j != 0) && data[0][0] == data[i][j]) {
                    assertEquals(GameController.MoveResult.DISABLE, controller.handleButton(i, j));
                    return;
                }
            }
        }
    }

    @Test
    public void twoButtonsDifferentTest() throws Exception {
        controller.handleButton(0, 0);
        for (int i = 0; i < testSize; ++i) {
            for (int j = 0; j < testSize; ++j) {
                if ((i != 0 || j != 0) && data[0][0] != data[i][j]) {
                    assertEquals(GameController.MoveResult.HIDE_BOTH, controller.handleButton(i, j));
                    return;
                }
            }
        }
    }

    @Test
    public void winTest() throws Exception {
        int opened = 0;
        for (int i = 0; i < testSize; ++i) {
            for (int j = 0; j < testSize; ++j) {
                for (int row = 0; row < testSize; ++row) {
                    for (int col = 0; col < testSize; ++col) {
                        if ((i != row || j != col) && data[row][col] == data[i][j] && data[i][j] != -1) {
                            controller.handleButton(i, j);
                            if (++opened == testSize * testSize / 2) {
                                assertEquals(GameController.MoveResult.WIN, controller.handleButton(row, col));
                                return;
                            } else {
                                data[i][j] = -1;
                                assertEquals(GameController.MoveResult.DISABLE, controller.handleButton(row, col));
                                controller.clearShown();
                            }
                        }
                    }
                }
            }
        }
    }

}