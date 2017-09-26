package net.netau.vasyoid;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Random;

/**
 * The class provides unit tests for the Matrix class public methods
 * and some exceptional situations.
 */
class MatrixTest {

    /**
     * Tests Matrix.getArray() method.
     * Cases: matrices of odd sizes between 1 and 9.
     */
    @Test
    void testGetArray() {
        for (int size = 1; size < 10; size += 2) {
            int[] elements = new int[size * size];
            for (int i = 0; i < size * size; i++) {
                elements[i] = i;
            }
            Matrix matrix = new Matrix(size, elements);
            assertArrayEquals(elements, matrix.getArray());
        }
    }

    /**
     * Tests Matrix.getSpiralizedArray() method.
     * Cases: Matrices 3x3 and 5x5.
     */
    @Test
    void testGetSpiralizedArray() {
        int[] elements = {1, 2, 3,
                          4, 5, 6,
                          7, 8, 9};
        Matrix matrix = new Matrix(3, elements);
        elements = new int[] {5, 4, 7, 8, 9, 6, 3, 2, 1};
        assertArrayEquals(elements, matrix.getSpiralizedArray());
        elements = new int[] {1,  2,  3,  4,  5,
                6,  7,  8,  9,  10,
                11, 12, 13, 14, 15,
                16, 17, 18, 19, 20,
                21, 22, 23, 24, 25};
        matrix = new Matrix(5, elements);
        elements = new int[] {13, 12, 17, 18, 19, 14, 9, 8, 7, 6, 11, 16,
                21, 22, 23, 24, 25, 20, 15, 10, 5, 4, 3, 2, 1};
        assertArrayEquals(elements, matrix.getSpiralizedArray());
    }

    /**
     * Tests Matrix.sortColumns() method.
     * Cases: columns in reverse order and in random order.
     */
    @Test
    void testSortColumns() {
        int[] elements = new int[25];
        for (int i = 0; i < 25; i++) {
            elements[i] = 25 - i;
        }
        Matrix matrix = new Matrix(5, elements);
        matrix.sortColumns();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                elements[5 * i + j] = 5 * (4 - i) + j + 1;
            }
        }
        assertArrayEquals(elements, matrix.getArray());
        java.util.Arrays.fill(elements, 0);
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            elements[i] = random.nextInt();
        }
        matrix = new Matrix(5, elements);
        matrix.sortColumns();
        elements = matrix.getArray();
        for (int i = 0; i < 4; i++) {
            assertTrue(elements[i] <= elements[i + 1]);
        }
    }

    /**
     * Checks what exceptions are thrown when an illegal argument is passed into constructors.
     * Cases: negative size, even size, different size and number of elements.
     */
    @Test
    void testExceptions() {
        assertThrows(IllegalArgumentException.class, () -> new Matrix(-3), "matrix size must be positive");
        assertThrows(IllegalArgumentException.class, () -> new Matrix(4), "matrix size must be odd");
        int[] elements = new int[5];
        assertThrows(IllegalArgumentException.class, () ->
                new Matrix(5, elements), "matrix size and number of elements do not match");
    }

}