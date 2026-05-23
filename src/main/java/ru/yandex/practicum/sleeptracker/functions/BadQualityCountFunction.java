package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.Quality;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class BadQualityCountFunction implements SleepAnalyzerFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long count = sessions.stream()
                .filter(s -> s.getQuality() == Quality.BAD)
                .count();
        return new SleepAnalysisResult("Количество сессий с плохим качеством сна", count);
    }
}