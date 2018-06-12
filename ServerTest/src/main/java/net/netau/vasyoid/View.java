package net.netau.vasyoid;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.netau.vasyoid.servers.Server;
import net.netau.vasyoid.servers.ServerManager;

import java.util.List;

public class View extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    private void createElements(VBox pane) {
        Separator line1 = new Separator();
        line1.setPadding(new Insets(10, 0, 20, 0));
        Separator line2 = new Separator();
        line2.setPadding(new Insets(10, 0, 20, 0));
        Separator line3 = new Separator();
        line3.setPadding(new Insets(10, 0, 20, 0));
        Separator line4 = new Separator();
        line4.setPadding(new Insets(10, 0, 10, 0));

        Label architectureLabel = new Label("Тестируемая архитектура");
        ChoiceBox<String> architectureChooser = new ChoiceBox<>(
                FXCollections.observableArrayList(
                    "Простой блокирующий сервер",
                    "Блокирующий сервер с пулом потоков",
                    "Неблокирующий сервер"));
        architectureChooser.getSelectionModel().select(0);

        Label xLabel = new Label("Количество запросов от каждого клиента (X)");
        Spinner<Integer> xChooser = new Spinner<>(100, 1000, 100, 100);
        xChooser.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);

        Label nLabel = new Label("Постоянное значение N");
        Spinner<Integer> nChooser = new Spinner<>(100, 1000, 100, 10);
        nChooser.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        Label mLabel = new Label("Постоянное значение M");
        Spinner<Integer> mChooser = new Spinner<>(100, 1000, 100, 10);
        mChooser.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        Label dLabel = new Label("Постоянное значение Δ (мс)");
        Spinner<Integer> dChooser = new Spinner<>(100, 1000, 100, 10);
        dChooser.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);

        Spinner[] choosers = new  Spinner[]{nChooser, mChooser, dChooser};

        Label variableParameterLabel = new Label("Параметр, который будет меняться");
        ChoiceBox<String> variableParameterChooser = new ChoiceBox<>(
                FXCollections.observableArrayList(
                        "Количество элементов в массиве (N)",
                        "Количество одновременно работающих клиентов (M)",
                        "Время между запросами (Δ, мс)"));
        variableParameterChooser.getSelectionModel().selectedIndexProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (oldValue.intValue() >= 0) {
                        choosers[oldValue.intValue()].setDisable(false);
                    }
                    choosers[newValue.intValue()].setDisable(true);
                }
        );
        variableParameterChooser.getSelectionModel().select(0);

        Label minValueLabel = new Label("Минимальное значение");
        Spinner<Integer> minValueChooser = new Spinner<>(100, 800, 100, 100);
        minValueChooser.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        Label maxValueLabel = new Label("Максимальное значение");
        Spinner<Integer> maxValueChooser = new Spinner<>(300, 1000, 300, 100);
        maxValueChooser.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        Label strideLabel = new Label("Шаг");
        Spinner<Integer> strideChooser = new Spinner<>(10, 100, 10, 10);
        strideChooser.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);

        Button startButton = new Button("Старт");
        startButton.setOnAction(event -> {
            int architectureIndex = architectureChooser.getSelectionModel().getSelectedIndex();
            ServerManager.Config config =
                    new ServerManager.Config(Server.ServerType.values()[architectureIndex]);
            config.setRequestsCount(xChooser.getValue());
            config.setElementsCount(nChooser.getValue());
            config.setClientsCount(mChooser.getValue());
            config.setDelta(dChooser.getValue());
            /*switch (variableParameterChooser.getSelectionModel().getSelectedIndex()) {
                case 0:
                    config.setElementsCount(minValueChooser.getValue(),
                            maxValueChooser.getValue(), strideChooser.getValue());
                    break;
                case 1:
                    config.setClientsCount(minValueChooser.getValue(),
                            maxValueChooser.getValue(), strideChooser.getValue());
                    break;
                default:
                    config.setDelta(minValueChooser.getValue(),
                            maxValueChooser.getValue(), strideChooser.getValue());
                    break;
            }
            */
            System.out.println(config);
            System.out.printf("Sort time:    Handle time:    Client time:\n");
            List<Server.TestResult> results = ServerManager.run(config);
            for (Server.TestResult result : results) {
                System.out.printf("%10d    %10d    %10d\n",
                        result.getAverageSortTime(),
                        result.getAverageHandleTime(),
                        result.getAverageClientTime());
            }

        });

        pane.getChildren().addAll(
                architectureLabel, architectureChooser,
                line1,
                xLabel, xChooser,
                line2,
                variableParameterLabel, variableParameterChooser,
                minValueLabel, minValueChooser,
                maxValueLabel, maxValueChooser,
                strideLabel, strideChooser,
                line3,
                nLabel, nChooser,
                mLabel, mChooser,
                dLabel, dChooser,
                line4,
                startButton
        );

    }

    @Override
    public void start(Stage stage) {
        VBox pane = new VBox();
        pane.setPadding(new Insets(30, 50, 50, 50));
        pane.setSpacing(5);
        createElements(pane);
        Scene scene = new Scene(pane, 600, 700);
        scene.setOnKeyPressed(t -> {
            KeyCode key = t.getCode();
            if (key == KeyCode.ESCAPE){
                stage.close();
            }
        });
        stage.setScene(scene);
        stage.setTitle("Server architectures test");
        stage.setResizable(false);
        stage.show();
    }
}
