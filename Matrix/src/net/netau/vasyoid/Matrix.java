package net.netau.vasyoid;

public class Matrix {

    private int[][] data;
    private int size;

    public Matrix(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("matrix size must be positive");
        } else if (size % 2 == 0) {
            throw new IllegalArgumentException("matrix size must be odd");
        }
        this.size = size;
        data = new int[size][size];
    }

    public Matrix(int size, int[] elements) {
        this(size);
        if (size * size != elements.length) {
            throw new IllegalArgumentException("matrix size and number of elements do not match");
        }
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                data[row][col] = elements[col * size + row];
            }
        }
    }

    public int[] getArray() {
        int[] result = new int[size * size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                result[col * size + row] = data[row][col];
            }
        }
        return result;
    }

    public int[] getSpiralizedArray() {
        int[] result = new int[size * size];
        int curRow = size / 2;
        int curCol = size / 2;
        int curResPos = 0;
        int nextRow = -1;
        int nextCol = 0;
        int stride = 1;
        result[curResPos++] = data[curCol][curCol];
        while (curCol > 0 || curRow > 0) {
            for (int i = 0; i < stride && (curRow > 0 || curCol > 0); i++) {
                curRow += nextRow;
                curCol += nextCol;
                result[curResPos++] = data[curRow][curCol];
            }
            int tmp = -nextRow;
            nextRow = nextCol;
            nextCol = tmp;
            if (nextCol == 0) {
                stride++;
            }
        }
        return result;
    }

    public void sortColumns() {
        java.util.Arrays.sort(data, new java.util.Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return Integer.compare(a[0], b[0]);
            }
        });
    }
}
