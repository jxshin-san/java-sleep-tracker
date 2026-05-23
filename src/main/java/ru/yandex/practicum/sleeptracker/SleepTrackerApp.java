package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.functions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SleepTrackerApp {

    private static final String FILE_NAME = "sleep_log.txt";
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
        SleepTrackerApp app = new SleepTrackerApp();

        try {
            List<SleepingSession> sessions = app.loadLogsFromResources();

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

    private List<SleepingSession> loadLogsFromResources() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FILE_NAME);

        if (inputStream == null) {
            throw new IOException("Файл '" + FILE_NAME + "' не найден в папке ресурсов (src/main/resources)!");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines()
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