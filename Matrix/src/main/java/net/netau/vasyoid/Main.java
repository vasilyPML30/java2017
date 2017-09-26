package net.netau.vasyoid;

import java.util.Random;

/**
 * The main class to show what Matrix method return.
 */
public class Main {

    private static void printMatrix(int size, int[] matrix) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                System.out.print(matrix[row * size + col] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void printArray(int[] array) {
        for (int element : array) {
            System.out.print(element + " ");
        }
        System.out.println("\n");
    }

    /**
     * The main function.
     * A random matrix is created, output in a normal and a spiral ways and with sorted columns.
     * @param args not used
     */
    public static void main(String[] args) {
        final int size = 5;
        int[] data = new int[size * size];
        Random random = new Random();
        for (int i = 0; i < size * size; i++) {
            data[i] = random.nextInt(size * size);
        }
        Matrix matrix = new Matrix(size, data);
        printMatrix(size, matrix.getArray());
        printArray(matrix.getSpiralizedArray());
        matrix.sortColumns();
        printMatrix(size, matrix.getArray());
    }
}
