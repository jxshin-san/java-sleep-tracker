package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class MaxDurationFunction implements SleepAnalyzerFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long max = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .max()
                .orElse(0);
        return new SleepAnalysisResult("Максимальная продолжительность сессии (в минутах)", max);
    }
}