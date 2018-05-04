package net.netau.vasyoid;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import javafx.util.Duration;

public class GameView extends Application {

    private static int boardSize;
    private Button[][] buttons;
    private GameController controller;

    private void flipButton(int row, int col) {
        switch (controller.handleButton(row, col)) {
            case HIDE:
                buttons[row][col].setText("");
                break;
            case HIDE_BOTH:
                buttons[row][col].setText(String.valueOf(controller.getValue(row, col)));
                new Timeline(new KeyFrame(Duration.seconds(1), ae -> {
                    for (Pair<Integer, Integer> p : controller.getShown()) {
                        buttons[p.getKey()][p.getValue()].setText("");
                    }
                    controller.clearShown();
                })).play();
                break;
            case WIN:
                for (int i = 0; i < boardSize; ++i) {
                    for (int j = 0; j < boardSize; ++j) {
                        buttons[i][j].setText("You win!");
                    }
                }
            case DISABLE:
                for (Pair<Integer, Integer> p : controller.getShown()) {
                    buttons[p.getKey()][p.getValue()].setDisable(true);
                }
                controller.clearShown();
            case SHOW:
                if (!buttons[row][col].getText().equals("You win!")) {
                    buttons[row][col].setText(String.valueOf(controller.getValue(row, col)));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void start(@NotNull Stage primaryStage) {
        controller = new GameController(boardSize);
        primaryStage.setTitle("Найди пару");
        primaryStage.setResizable(false);
        GridPane pane = new GridPane();
        pane.setHgap(4);
        pane.setVgap(4);
        buttons = new Button[boardSize][boardSize];
        float buttonSize = 604f / boardSize - 4;
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                buttons[row][col] = new Button();
                buttons[row][col].setMaxSize(buttonSize, buttonSize);
                buttons[row][col].setMinSize(buttonSize, buttonSize);
                int finalRow = row, finalCol = col;
                buttons[row][col].setOnAction(action -> flipButton(finalRow, finalCol));
                pane.add(buttons[row][col], col, row);
            }
        }
        Scene scene = new Scene(pane, 600, 600);
        primaryStage.setScene(scene);
        scene.setOnKeyPressed(t -> {
            KeyCode key = t.getCode();
            if (key == KeyCode.ESCAPE){
                primaryStage.close();
            }
        });
        primaryStage.show();
    }

    public static void main(@NotNull String[] args) {
        if (args.length != 1) {
            System.out.println("usage: FindPair <board size>");
        }
        boardSize = Integer.parseInt(args[0]);
        if (boardSize == 0 || boardSize % 2 != 0 || boardSize > 10) {
            System.out.println("board size must be even, < 11 and > 0");
            System.exit(-1);
        }
        Application.launch(args);
    }

}
