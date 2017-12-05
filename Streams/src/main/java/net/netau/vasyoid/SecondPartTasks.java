package net.netau.vasyoid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Random;
import java.awt.geom.Point2D;

public final class SecondPartTasks {

    private static final int SHOTS_COUNT = 5000000;

    private SecondPartTasks() {}

    /** Найти строки из переданных файлов, в которых встречается указанная подстрока.
     */
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
            return paths.stream()
                    .map(Paths::get)
                    .flatMap(path -> {
                        try {
                            return Files.lines(path);
                        } catch (IOException e) {
                            System.out.println("Could not read file: " + path.getFileName());
                            return Stream.empty();
                        }
                    })
                    .filter(line -> line.contains(sequence))
                    .collect(Collectors.toList());
    }

    /** В квадрат с длиной стороны 1 вписана мишень.
     *  Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
     *  Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать, какова вероятность попасть в мишень.
     */
    public static double piDividedBy4() {
        return Stream.generate(() -> {
                Point2D.Double point = new Point2D.Double();
                Random random = new Random();
                point.setLocation(random.nextDouble() - 0.5, random.nextDouble() - 0.5);
                return point;
            })
            .limit(SHOTS_COUNT)
            .map(point -> Math.sqrt(point.getX() * point.getX() + point.getY() * point.getY()))
            .mapToInt(x -> (x <= 0.5 ? 1 : 0))
            .average().orElse(0);
    }

    /** Дано отображение из имени автора в список с содержанием его произведений.
     * Надо вычислить, чья общая длина произведений наибольшая.
     */
    public static String findPrinter(Map<String, List<String>> compositions)
                                                        throws IllegalArgumentException{
        return compositions.entrySet().stream()
            .collect(
                    Collectors.groupingBy(
                            Map.Entry::getKey,
                            Collectors.summingInt(
                                    entry -> entry.getValue().stream()
                                            .mapToInt(String::length).sum()
                            )
                    )
            )
            .entrySet().stream()
            .max(Comparator.comparingInt(Map.Entry::getValue))
            .orElseThrow(() -> new IllegalArgumentException("Empty compositions list"))
            .getKey();
    }

    /** Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
     * Необходимо вычислить, какой товар и в каком количестве надо поставить.
     */
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream().flatMap(order -> order.entrySet().stream())
            .collect(
                    Collectors.groupingBy(
                            Map.Entry::getKey,
                            Collectors.summingInt(Map.Entry::getValue)
                    )
            );
    }
}
