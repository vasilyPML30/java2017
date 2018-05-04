package net.netau.vasyoid;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameController {

    private int boardSize;
    private List<Pair<Integer, Integer>> shown;
    private int matchedPairs = 0;
    private int numbersRange;

    private int[][] data;

    public GameController(int boardSize) {
        shown = new ArrayList<>();
        this.boardSize = boardSize;
        data = new int[boardSize][boardSize];
        List<Integer> numbers = new ArrayList<>();
        Random random = new Random();
        numbersRange = boardSize * boardSize / 2;
        for (int i = 0; i < numbersRange; ++i) {
            int number = random.nextInt(numbersRange);
            numbers.add(number);
            numbers.add(number);
        }
        Collections.shuffle(numbers);
        for (int i = 0; i < boardSize; ++i) {
            for (int j = 0; j < boardSize; ++j) {
                data[i][j] = numbers.get(i * boardSize + j);
            }
        }
    }

    MoveResult handleButton(int row, int col) {
        Pair<Integer, Integer> current = new Pair<>(row, col);
        if (shown.size() == 2) {
            return MoveResult.NOTHING;
        } else if (shown.size() == 0) {
            shown.add(current);
            return MoveResult.SHOW;
        } else if (shown.contains(current)) {
            shown.remove(0);
            return MoveResult.HIDE;
        } else {
            Pair<Integer, Integer> previous = shown.get(0);
            shown.add(current);
            if (data[previous.getKey()][previous.getValue()] !=
                    data[current.getKey()][current.getValue()]) {
                return MoveResult.HIDE_BOTH;
            } else {
                return ++matchedPairs == numbersRange ? MoveResult.WIN : MoveResult.DISABLE;
            }
        }
    }

    int getValue(int row, int col) {
        return data[row][col];
    }

    public List<Pair<Integer, Integer>> getShown() {
        return shown;
    }

    public void clearShown() {
        shown.clear();
    }

    public enum MoveResult {
        SHOW, HIDE, HIDE_BOTH, WIN, DISABLE, NOTHING
    }

}
