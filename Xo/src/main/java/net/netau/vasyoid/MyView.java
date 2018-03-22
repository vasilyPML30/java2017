package net.netau.vasyoid;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import static net.netau.vasyoid.GameController.BOARD_SIZE;

/**
 * Game view class.
 * Creates graphical interface and handles user interactions.
 */
public class MyView extends Application {

    private static final int MAX_NAME_LENGTH = 10;

    private GameController controller;
    private Button[][] buttons = new Button[BOARD_SIZE][BOARD_SIZE];
    private Label titleText = new Label();
    private GameController.CellState firstPlayerItem = GameController.CellState.CROSS;
    private String firstName = "Алиса";
    private String secondName = "Боб";
    private TableView<GameResult> table = new TableView<>();

    /**
     * Simulate a click on one of the board buttons.
     * @param row button vertical position.
     * @param col button horizontal position.
     */
    public void pressButton(int row, int col) {
        buttons[row][col].fire();
    }

    /**
     * Restart current game.
     * The board is cleared and player flip their roles.
     */
    public void restart() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                buttons[row][col].setText(GameController.CellState.EMPTY.toString());
            }
        }
        firstPlayerItem = firstPlayerItem.getOpposite();
        controller.init();
        updateTitle();
    }

    private void updateTitle() {
        String title = firstName.toUpperCase()
                + " (" + firstPlayerItem + ")"
                + " против " + secondName.toUpperCase()
                + " (" + firstPlayerItem.getOpposite() + ")"
                + "\n" + controller.getCurrentGameState();
        titleText.setText(title);
    }

    @NotNull
    private Tab createGameTab() {
        Tab gameTab = new Tab();
        GridPane pane = new GridPane();
        gameTab.setText("Игра");
        gameTab.setContent(pane);
        RowConstraints titleRow = new RowConstraints();
        titleRow.setPercentHeight(30);
        pane.getRowConstraints().add(titleRow);
        RowConstraints restartRow = new RowConstraints();
        restartRow.setPercentHeight(30);
        pane.getRowConstraints().add(restartRow);
        titleText.setMaxWidth(Double.MAX_VALUE);
        titleText.setAlignment(Pos.CENTER);
        Button restartButton = new Button("Начать заново");
        restartButton.setMaxWidth(Double.MAX_VALUE);
        restartButton.setOnAction(event -> restart());
        for (int row = 0; row < BOARD_SIZE; row++) {
            RowConstraints rowCons = new RowConstraints();
            rowCons.setPercentHeight(100);
            pane.getRowConstraints().add(rowCons);
            ColumnConstraints colCons = new ColumnConstraints();
            colCons.setPercentWidth(100);
            pane.getColumnConstraints().add(colCons);
        }
        pane.add(titleText, 0, 0, 3, 1);
        pane.add(restartButton, 1, 1);
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                buttons[row][col] = new Button();
                buttons[row][col].setStyle("-fx-font: 50 serif");
                buttons[row][col].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                int finalRow = row, finalCol = col;
                buttons[row][col].setOnAction(actionEvent -> {
                    if (controller.isGameOver()) {
                        return;
                    }
                    buttons[finalRow][finalCol].setText(controller.move(finalRow, finalCol).toString());
                    if (controller.isGameOver()) {
                        table.getItems().add(new GameResult(firstName + "(" + firstPlayerItem + ")",
                                secondName + "(" + firstPlayerItem.getOpposite() + ")",
                                controller.getCurrentGameState().toString()));
                    }
                    updateTitle();
                    controller.turn();
                });
                pane.add(buttons[row][col], col, row + 2);
            }
        }
        return gameTab;
    }

    @NotNull
    private Tab createSettingsTab() {
        Tab scoreTab = new Tab();
        scoreTab.setText("Настройки");
        VBox pane = new VBox(5);
        pane.setPadding(new Insets(50, 50, 50, 50));
        scoreTab.setContent(pane);
        EventHandler<KeyEvent> nameInputHandler = event -> {
            if (!event.getCharacter().matches("[А-Яа-яA-Za-z ]")) {
                event.consume();
            }
        };
        Label firstPlayerNameLabel = new Label("Имя первого игрока:");
        TextField firstPlayerName = new TextField();
        firstPlayerName.setPromptText("Введите имя");
        firstPlayerName.setOnKeyTyped(nameInputHandler);
        firstPlayerName.textProperty().addListener(value -> {
            if (firstPlayerName.getLength() > MAX_NAME_LENGTH) {
                firstPlayerName.deleteText(MAX_NAME_LENGTH, firstPlayerName.getLength());
            }
            firstName = firstPlayerName.getText();
            if (firstName.equals("")) {
                firstName = "Боб";
            }
            updateTitle();
        });
        Label secondPlayerNameLabel = new Label("Имя второго игрока:");
        Label secondPlayerTitle = new Label("Тип второго игрока:");
        TextField secondPlayerName = new TextField();
        secondPlayerName.setPromptText("Введите имя");
        secondPlayerName.setOnKeyTyped(nameInputHandler);
        secondPlayerName.textProperty().addListener(value -> {
            if (secondPlayerName.getLength() > MAX_NAME_LENGTH) {
                secondPlayerName.deleteText(MAX_NAME_LENGTH, secondPlayerName.getLength());
            }
            secondName = secondPlayerName.getText();
            if (secondName.equals("")) {
                secondName = "Боб";
            }
            updateTitle();
        });
        ComboBox<String> secondPlayer = new ComboBox<>();
        secondPlayer.getItems().addAll("Пользователь", "Изи робот", "Хард робот");
        secondPlayer.getSelectionModel().selectFirst();
        secondPlayer.setMaxWidth(Double.MAX_VALUE);
        secondPlayer.valueProperty().addListener((observableValue, s, t1) -> {
            secondPlayerName.setVisible(t1.equals("Пользователь"));
            secondPlayerNameLabel.setVisible(t1.equals("Пользователь"));
            switch (t1) {
                case "Пользователь":
                    secondPlayerName.setText("");
                    controller = new GameController();
                    break;
                case "Изи робот":
                    secondPlayerName.setText(t1);
                    controller = new AutoGameController(this, AutoGameController.Level.EASY,
                            firstPlayerItem.getOpposite());
                    break;
                default:
                    secondPlayerName.setText(t1);
                    controller = new AutoGameController(this, AutoGameController.Level.HARD,
                            firstPlayerItem.getOpposite());
                    break;
            }
            restart();
        });
        pane.getChildren().add(firstPlayerNameLabel);
        pane.getChildren().add(firstPlayerName);
        pane.getChildren().add(secondPlayerTitle);
        pane.getChildren().add(secondPlayer);
        pane.getChildren().add(secondPlayerNameLabel);
        pane.getChildren().add(secondPlayerName);
        VBox.setMargin(secondPlayerTitle, new Insets(40, 0, 0, 0));
        VBox.setMargin(secondPlayerNameLabel, new Insets(20, 0, 0, 0));
        return scoreTab;
    }

    @NotNull
    private Tab createScoreTab() {
        Tab scoreTab = new Tab();
        scoreTab.setText("Результаты игр");
        table = new TableView<>();
        TableColumn<GameResult, String> firstPlayer = new TableColumn<>("Первый игрок");
        firstPlayer.setCellValueFactory(new PropertyValueFactory<>("firstPlayerName"));
        TableColumn<GameResult, String> secondPlayer = new TableColumn<>("Второй игрок");
        secondPlayer.setCellValueFactory(new PropertyValueFactory<>("secondPlayerName"));
        TableColumn<GameResult, String> result = new TableColumn<>("Результат");
        result.setCellValueFactory(new PropertyValueFactory<>("result"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().add(firstPlayer);
        table.getColumns().add(secondPlayer);
        table.getColumns().add(result);
        table.setPadding(new Insets(20));
        scoreTab.setContent(table);
        return scoreTab;
    }

    /**
     * Start the application.
     * Creates and initialises visual elements and a controller.
     * @param primaryStage layout to place the elements.
     */
    @Override
    public void start(@NotNull Stage primaryStage) {
        controller = new GameController();
        titleText.setTextAlignment(TextAlignment.CENTER);
        primaryStage.setTitle("Крестики-нолики");
        primaryStage.setResizable(false);
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().add(createGameTab());
        tabs.getTabs().add(createSettingsTab());
        tabs.getTabs().add(createScoreTab());
        Scene scene = new Scene(tabs, 600, 680);
        primaryStage.setScene(scene);
        primaryStage.show();
        restart();
    }

    /**
     * Auxiliary class for the table of results.
     * Represents table rows.
     */
    public static class GameResult {

        private final SimpleStringProperty firstPlayerName;
        private final SimpleStringProperty secondPlayerName;
        private final SimpleStringProperty result;

        private GameResult(String firstPlayerName, String secondPlayerName, String result) {
            this.firstPlayerName = new SimpleStringProperty(firstPlayerName);
            this.secondPlayerName = new SimpleStringProperty(secondPlayerName);
            this.result = new SimpleStringProperty(result);
        }

        @NotNull
        @SuppressWarnings("unused")
        public String getFirstPlayerName() {
            return firstPlayerName.get();
        }

        @NotNull
        @SuppressWarnings("unused")
        public String getSecondPlayerName() {
            return secondPlayerName.get();
        }

        @NotNull
        @SuppressWarnings("unused")
        public String getResult() {
            return result.get();
        }
    }
}
