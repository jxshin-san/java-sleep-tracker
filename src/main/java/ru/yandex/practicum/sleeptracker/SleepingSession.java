package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.time.LocalDateTime;

public class SleepingSession {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Quality quality;

    public SleepingSession(LocalDateTime startTime, LocalDateTime endTime, Quality quality) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.quality = quality;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Quality getQuality() {
        return quality;
    }

    public long getDurationInMinutes() {
        return Duration.between(startTime, endTime).toMinutes();
    }
}