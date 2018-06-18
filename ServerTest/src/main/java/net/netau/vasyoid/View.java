package net.netau.vasyoid;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.netau.vasyoid.servers.Server;
import net.netau.vasyoid.servers.ServerManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class View extends Application {

    private static InetAddress clientAddress;

    public static void main(String[] args) {
        try {
            clientAddress = InetAddress.getByName(args[0]);
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + e.getMessage());
        }
        Application.launch(args);
    }

    private static void createPlotWindow(List<Server.TestResult> data,
                                  int minParameter, int deltaParameter) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Параметр");
        XYChart.Series<Number, Number> clientSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> handleSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> sortSeries = new XYChart.Series<>();
        clientSeries.setName("Время на клиенте");
        handleSeries.setName("Время на сервере");
        sortSeries.setName("Время сортировки");
        for (int i = 0; i < data.size(); ++i) {
            clientSeries.getData().add(new XYChart.Data<>(minParameter + i * deltaParameter,
                    data.get(i).getAverageClientTime()));
            handleSeries.getData().add(new XYChart.Data<>(minParameter + i * deltaParameter,
                    data.get(i).getAverageHandleTime()));
            sortSeries.getData().add(new XYChart.Data<>(minParameter + i * deltaParameter,
                    data.get(i).getAverageSortTime()));
        }
        LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.getData().add(clientSeries);
        lineChart.getData().add(handleSeries);
        lineChart.getData().add(sortSeries);
        Stage stage = new Stage();
        stage.setScene(new Scene(lineChart, 800, 600));
        stage.setTitle("Результат тестирования");
        stage.show();
    }

    private static void writeResults(ServerManager.Config config, List<Server.TestResult> results) {
        try (PrintWriter out = new PrintWriter(config.getServerType().toString() + ".txt")) {
            out.println(config);
            out.printf("Sort time:    Handle time:    Client time:\n");
            for (Server.TestResult result : results) {
                out.printf("%10d    %10d    %10d\n",
                        result.getAverageSortTime(),
                        result.getAverageHandleTime(),
                        result.getAverageClientTime());
            }
        } catch (IOException e) {
            System.out.println("Could not write to file: " + e.getMessage());
        }
    }

    private static void createElements(VBox pane) {
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
        Spinner<Integer> xChooser = new Spinner<>(10, 100, 10, 10);
        xChooser.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);


        int[][] bounds = new int[][] {
            {1000, 10000, 100, 1000},
            {10, 100, 10, 10},
            {20, 400, 20, 20}
        };

        Label nLabel = new Label("Постоянное значение N");
        Spinner<Integer> nChooser = new Spinner<>(
                bounds[0][0], bounds[0][1], bounds[0][2], bounds[0][3]);
        nChooser.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        Label mLabel = new Label("Постоянное значение M");
        Spinner<Integer> mChooser = new Spinner<>(
                bounds[1][0], bounds[1][1], bounds[1][2], bounds[1][3]);
        mChooser.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        Label dLabel = new Label("Постоянное значение Δ (мс)");
        Spinner<Integer> dChooser = new Spinner<>(
                bounds[2][0], bounds[2][1], bounds[2][2], bounds[2][3]);
        dChooser.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);

        Spinner[] choosers = new  Spinner[]{nChooser, mChooser, dChooser};

        Label minValueLabel = new Label("Минимальное значение");
        Spinner<Integer> minValueChooser = new Spinner<>(1000, 10000, 100, 100);
        minValueChooser.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        Label maxValueLabel = new Label("Максимальное значение");
        Spinner<Integer> maxValueChooser = new Spinner<>(1000, 10000, 100, 100);
        maxValueChooser.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        Label strideLabel = new Label("Шаг");
        Spinner<Integer> strideChooser = new Spinner<>(1000, 10000, 100, 100);
        strideChooser.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);

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
                    int ind = newValue.intValue();
                    choosers[ind].setDisable(true);
                    minValueChooser.setValueFactory(
                            new SpinnerValueFactory.IntegerSpinnerValueFactory(
                                    bounds[ind][0], bounds[ind][1], bounds[ind][2], bounds[ind][3]
                            ));
                    maxValueChooser.setValueFactory(
                            new SpinnerValueFactory.IntegerSpinnerValueFactory(
                                    bounds[ind][0], bounds[ind][1], bounds[ind][2], bounds[ind][3]
                            ));
                    strideChooser.setValueFactory(
                            new SpinnerValueFactory.IntegerSpinnerValueFactory(
                                    bounds[ind][0], bounds[ind][1], bounds[ind][2], bounds[ind][3]
                            ));
                }
        );
        variableParameterChooser.getSelectionModel().select(0);

        Button startButton = new Button("Старт");
        startButton.setOnAction(event -> {
            int architectureIndex = architectureChooser.getSelectionModel().getSelectedIndex();
            ServerManager.Config config =
                    new ServerManager.Config(Server.ServerType.values()[architectureIndex]);
            config.setRequestsCount(xChooser.getValue());
            config.setElementsCount(nChooser.getValue());
            config.setClientsCount(mChooser.getValue());
            config.setDelta(dChooser.getValue());
            switch (variableParameterChooser.getSelectionModel().getSelectedIndex()) {
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
            List<Server.TestResult> results = ServerManager.run(config, clientAddress);
            writeResults(config, results);
            createPlotWindow(results, minValueChooser.getValue(), strideChooser.getValue());
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
