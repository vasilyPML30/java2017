package net.netau.vasyoid.utils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import net.netau.vasyoid.servers.Server;
import net.netau.vasyoid.servers.ServerManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CompareView extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    private List<Server.TestResult> readResults(ServerManager.Config config) {
        List<Server.TestResult> result = new ArrayList<>();
        File file = new File(config.getServerType().toString() + ".txt");
        try (Scanner in = new Scanner(file)) {
            config.read(in);
            in.nextLine();
            in.nextLine();
            while (in.hasNextInt()) {
                Server.TestResult testResult = new Server.TestResult();
                testResult.addTest();
                testResult.addSortTime(in.nextInt());
                testResult.addHandleTime(in.nextInt());
                testResult.setClientTime(in.nextInt());
                result.add(testResult);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not open a file: " + e.getMessage());
        }
        return result;
    }

    private XYChart.Series<Number, Number>[] createSeries(List<Server.TestResult> data,
                                           int minParameter, int deltaParameter) {
        //noinspection unchecked
        XYChart.Series<Number, Number>[] result = new XYChart.Series[] {
                new XYChart.Series<>(),
                new XYChart.Series<>(),
                new XYChart.Series<>()
        };
        for (int i = 0; i < data.size(); ++i) {
            result[0].getData().add(new XYChart.Data<>(minParameter + i * deltaParameter,
                    data.get(i).getAverageSortTime()));
            result[1].getData().add(new XYChart.Data<>(minParameter + i * deltaParameter,
                    data.get(i).getAverageHandleTime()));
            result[2].getData().add(new XYChart.Data<>(minParameter + i * deltaParameter,
                    data.get(i).getAverageClientTime()));
        }
        return result;
    }

    @Override
    public void start(Stage stage) {
        XYChart.Series<Number, Number> simpleSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> threadPoolSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> nonBlockingSeries = new XYChart.Series<>();
        simpleSeries.setName("Простой блокирующий сервер");
        threadPoolSeries.setName("Сервер с пулом потоков");
        nonBlockingSeries.setName("Неблокирующий сервер");
        ServerManager.Config simpleConfig = new ServerManager.Config(Server.ServerType.SIMPLE);
        ServerManager.Config threadPoolConfig =
                new ServerManager.Config(Server.ServerType.THREAD_POOL);
        ServerManager.Config nonBlockingConfig =
                new ServerManager.Config(Server.ServerType.NON_BLOCKING);
        List<Server.TestResult> simpleResults = readResults(simpleConfig);
        List<Server.TestResult> threadPoolResults = readResults(threadPoolConfig);
        List<Server.TestResult> nonBlockingResults = readResults(nonBlockingConfig);
        //noinspection unchecked
        XYChart.Series<Number, Number>[][] series = new XYChart.Series[3][3];
        series[0] = createSeries(simpleResults,
                simpleConfig.getParameterMin(), simpleConfig.getParameterStride());
        series[1] = createSeries(threadPoolResults,
                threadPoolConfig.getParameterMin(), threadPoolConfig.getParameterStride());
        series[2] = createSeries(nonBlockingResults,
                nonBlockingConfig.getParameterMin(), nonBlockingConfig.getParameterStride());
        TabPane tabPane = new TabPane();
        for (int i = 0; i < 3; ++i) {
            series[0][i].setName("Простой блокирующий сервер");
            series[1][i].setName("Блокирующий сервер с пулом потоков");
            series[2][i].setName("Неблокирующий сервер");
        }
        String[] tabName = {"Время сортировки", "Время обработки", "Время клиента"};
        for (int i = 0; i < 3; ++i) {
            NumberAxis xAxis = new NumberAxis();
            NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Задержка");
            yAxis.setLabel("Время");
            Tab tab = new Tab(tabName[i]);
            LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
            //noinspection unchecked
            lineChart.getData().addAll(series[0][i], series[1][i], series[2][i]);
            tab.setContent(lineChart);
            tabPane.getTabs().add(tab);
        }
        stage.setScene(new Scene(tabPane, 800, 600));
        stage.setTitle("Результат тестирования");
        stage.show();
    }
}
