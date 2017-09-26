package net.netau.vasyoid;

/**
 * A class of matrices.
 * Stores a 2-dimensional array of integer values.
 * A matrix can be presented as an array (successively or by a spiral).
 * Columns can be sorted by their first values.
 */
public class Matrix {

    private int[][] data;
    private int size;

    /**
     * Creates a matrix of wanted size filled with zeros.
     * @param size size of the matrix.
     */
    public Matrix(int size) throws IllegalArgumentException {
        if (size <= 0) {
            throw new IllegalArgumentException("matrix size must be positive");
        } else if (size % 2 == 0) {
            throw new IllegalArgumentException("matrix size must be odd");
        }
        this.size = size;
        data = new int[size][size];
    }

    /**
     * Creates a matrix filled with values from an array.
     * @param size size of the matrix.
     * @param elements array of values to fill tje matrix with.
     */
    public Matrix(int size, int[] elements) throws IllegalArgumentException {
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

    /**
     * Presents the matrix as an array of successive elements.
     * @return array of elements.
     */
    public int[] getArray() {
        int[] result = new int[size * size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                result[col * size + row] = data[row][col];
            }
        }
        return result;
    }

    /**
     * Presents the matrix as an array of elements ordered by spiral
     * with the beginning at the central cell.
     * @return array of elements.
     */
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

    /**
     * Sorts the columns of the matrix by their first elements.
     */
    public void sortColumns() {
        java.util.Arrays.sort(data, new java.util.Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return Integer.compare(a[0], b[0]);
            }
        });
    }
}
