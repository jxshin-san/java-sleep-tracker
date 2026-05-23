package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.LongStream;

public class SleeplessNightsFunction implements SleepAnalyzerFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0L);
        }

        SleepingSession first = sessions.getFirst();
        LocalDate startNight = first.getStartTime().toLocalTime().isBefore(LocalTime.NOON)
                ? first.getStartTime().toLocalDate().minusDays(1)
                : first.getStartTime().toLocalDate();

        SleepingSession last = sessions.getLast();
        LocalDate endNight = last.getEndTime().toLocalTime().isBefore(LocalTime.NOON)
                ? last.getEndTime().toLocalDate().minusDays(1)
                : last.getEndTime().toLocalDate();

        long totalNights = ChronoUnit.DAYS.between(startNight, endNight) + 1;

        long sleeplessCount = LongStream.range(0, totalNights)
                .mapToObj(startNight::plusDays)
                .filter(nightDate -> !isNightSlept(nightDate, sessions))
                .count();
        return new SleepAnalysisResult("Количество бессонных ночей", sleeplessCount);
    }

    private boolean isNightSlept(LocalDate nightDate, List<SleepingSession> sessions) {

        LocalDateTime criticalStart = nightDate.plusDays(1).atStartOfDay();
        LocalDateTime criticalEnd = criticalStart.plusHours(6);
        return sessions.stream().anyMatch(s ->
                s.getStartTime().isBefore(criticalEnd) && s.getEndTime().isAfter(criticalStart)
        );
    }
}