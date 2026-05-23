package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChronotypeFunction implements SleepAnalyzerFunction {

    private enum Type { OWL, LARK, DOVE }

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        Map<Type, Long> counts = sessions.stream()
                .map(this::determineType)
                .filter(type -> type != null)
                .collect(Collectors.groupingBy(type -> type, Collectors.counting()));

        long owls = counts.getOrDefault(Type.OWL, 0L);
        long larks = counts.getOrDefault(Type.LARK, 0L);
        long doves = counts.getOrDefault(Type.DOVE, 0L);

        String finalChronotype = "Голубь";
        if (owls > larks && owls > doves) {
            finalChronotype = "Сова";
        } else if (larks > owls && larks > doves) {
            finalChronotype = "Жаворонок";
        }

        return new SleepAnalysisResult("Хронотип пользователя", finalChronotype);
    }

    private Type determineType(SleepingSession s) {
        LocalTime bedTime = s.getStartTime().toLocalTime();
        LocalTime wakeTime = s.getEndTime().toLocalTime();

        if (s.getStartTime().toLocalDate().equals(s.getEndTime().toLocalDate())
                && bedTime.isAfter(LocalTime.of(11, 0))
                && wakeTime.isBefore(LocalTime.of(21, 0))) {
            return null;
        }

        boolean lateBed = bedTime.isAfter(LocalTime.of(23, 0)) || bedTime.isBefore(LocalTime.of(5, 0));
        boolean lateWake = wakeTime.isAfter(LocalTime.of(9, 0));
        if (lateBed && lateWake) {
            return Type.OWL;
        }

        boolean earlyBed = bedTime.isBefore(LocalTime.of(22, 0)) && bedTime.isAfter(LocalTime.of(18, 0));
        boolean earlyWake = wakeTime.isBefore(LocalTime.of(7, 0));
        if (earlyBed && earlyWake) {
            return Type.LARK;
        }

        return Type.DOVE;
    }
}