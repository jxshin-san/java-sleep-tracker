package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class MinDurationFunction implements SleepAnalyzerFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long min = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .min()
                .orElse(0);
        return new SleepAnalysisResult("Минимальная продолжительность сессии (в минутах)", min);
    }
}