package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.functions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SleepTrackerApp {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
    private final List<SleepAnalyzerFunction> analyticalFunctions = new ArrayList<>();

    public SleepTrackerApp() {
        analyticalFunctions.add(new TotalSessionsFunction());
        analyticalFunctions.add(new MinDurationFunction());
        analyticalFunctions.add(new MaxDurationFunction());
        analyticalFunctions.add(new AvgDurationFunction());
        analyticalFunctions.add(new BadQualityCountFunction());
        analyticalFunctions.add(new SleeplessNightsFunction());
        analyticalFunctions.add(new ChronotypeFunction());
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Укажите путь к файлу с логом сна в аргументах запуска.");
            return;
        }

        String filePath = args[0];
        SleepTrackerApp app = new SleepTrackerApp();

        try {
            List<SleepingSession> sessions = app.loadLogs(filePath);

            System.out.println("=== РЕЗУЛЬТАТЫ АНАЛИЗА СНА ===");
            app.analyticalFunctions.stream()
                    .map(function -> function.apply(sessions))
                    .forEach(System.out::println);

        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла логов: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }

    private List<SleepingSession> loadLogs(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path) || Files.isDirectory(path)) {
            throw new IOException("Файл по указанному пути не найден: " + filePath);
        }
        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .filter(line -> !line.isBlank())
                    .map(line -> {
                        String[] parts = line.split(";");
                        LocalDateTime startTime = LocalDateTime.parse(parts[0].trim(), FORMATTER);
                        LocalDateTime endTime = LocalDateTime.parse(parts[1].trim(), FORMATTER);
                        Quality quality = Quality.valueOf(parts[2].trim());
                        return new SleepingSession(startTime, endTime, quality);
                    })
                    .collect(Collectors.toList());
        }
    }
}