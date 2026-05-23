package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class AvgDurationFunction implements SleepAnalyzerFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        double avg = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .average()
                .orElse(0.0);
        return new SleepAnalysisResult("Средняя продолжительность сессии (в минутах)", Math.round(avg));
    }
}