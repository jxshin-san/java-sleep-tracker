package ru.yandex.practicum.sleeptracker;

    import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты трекера сна")
public class SleepTrackerAppTest {
    //сессии

    @Test
    @DisplayName("Подсчёт сессий на заполненном логе")
    public void testTotalSessionsWithData() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 7, 0), Quality.NORMAL)
        );
        assertEquals(2L, new TotalSessionsFunction().apply(sessions).getValue());
    }

    @Test
    @DisplayName("Подсчёт сессий на пустом логе")
    public void testTotalSessionsEmpty() {
        assertEquals(0L, new TotalSessionsFunction().apply(Collections.emptyList()).getValue());
    }

    @Test
    @DisplayName("Минимальная длительность на заполненном логе")
    public void testMinDurationFunction() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 1, 23, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 22, 0),
                        LocalDateTime.of(2025, 10, 3, 0, 0), Quality.GOOD)
        );
        assertEquals(60L, new MinDurationFunction().apply(sessions).getValue());
    }

    @Test
    @DisplayName("Минимальная длительность на пустом логе")
    public void testMinDurationEmpty() {
        assertEquals(0L, new MinDurationFunction().apply(Collections.emptyList()).getValue());
    }

    @Test
    @DisplayName("Максимальная длительность на заполненном логе")
    public void testMaxDurationFunction() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 1, 23, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 22, 0),
                        LocalDateTime.of(2025, 10, 3, 1, 0), Quality.GOOD)
        );
        assertEquals(180L, new MaxDurationFunction().apply(sessions).getValue());
    }

    @Test
    @DisplayName("Максимальная длительность на пустом логе")
    public void testMaxDurationEmpty() {
        assertEquals(0L, new MaxDurationFunction().apply(Collections.emptyList()).getValue());
    }

    @Test
    @DisplayName("Средняя длительность на заполненном логе")
    public void testAvgDurationFunction() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 1, 23, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 22, 0),
                        LocalDateTime.of(2025, 10, 3, 0, 0), Quality.GOOD)
        );
        assertEquals(90L, new AvgDurationFunction().apply(sessions).getValue());
    }

    @Test
    @DisplayName("Средняя длительность на пустом логе")
    public void testAvgDurationEmpty() {
        assertEquals(0L, new AvgDurationFunction().apply(Collections.emptyList()).getValue());
    }

    @Test
    @DisplayName("Подсчёт сессий с плохим качеством")
    public void testBadQualityCountFunction() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0), Quality.BAD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 22, 0),
                        LocalDateTime.of(2025, 10, 3, 6, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 22, 0),
                        LocalDateTime.of(2025, 10, 4, 6, 0), Quality.BAD)
        );
        assertEquals(2L, new BadQualityCountFunction().apply(sessions).getValue());
    }

    @Test
    @DisplayName("Подсчёт плохих сессий при их отсутствии")
    public void testBadQualityCountZero() {
        List<SleepingSession> sessions = Collections.singletonList(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0), Quality.GOOD)
        );
        assertEquals(0L, new BadQualityCountFunction().apply(sessions).getValue());
    }

    @Test
    @DisplayName("Подсчёт плохих сессий на пустом логе")
    public void testBadQualityCountEmpty() {
        assertEquals(0L, new BadQualityCountFunction().apply(Collections.emptyList()).getValue());
    }

    //бессонные ночи

    @Test
    @DisplayName("Бессонные ночи при регулярном сне")
    public void testSleeplessNightsAllSlept() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 7, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 7, 0), Quality.GOOD)
        );
        assertEquals(0L, new SleeplessNightsFunction().apply(sessions).getValue());
    }

    @Test
    @DisplayName("Обнаружение одной бессонной ночи")
    public void testSleeplessNightsOneSleepless() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 7, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 23, 0),
                        LocalDateTime.of(2025, 10, 4, 6, 30), Quality.GOOD)
        );
        assertEquals(1L, new SleeplessNightsFunction().apply(sessions).getValue());
    }

    @Test
    @DisplayName("Учёт правила 12:00 дня для дневного сна")
    public void testSleeplessNightsEdgeCaseRule12() {
        List<SleepingSession> sessions = Collections.singletonList(
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 11, 0),
                        LocalDateTime.of(2025, 10, 2, 11, 45), Quality.GOOD)
        );
        assertEquals(1L, new SleeplessNightsFunction().apply(sessions).getValue());
    }

    @Test
    @DisplayName("Бессонные ночи на пустом логе")
    public void testSleeplessNightsEmptyList() {
        assertEquals(0L, new SleeplessNightsFunction().apply(Collections.emptyList()).getValue());
    }

    //хронотипы

    @Test
    @DisplayName("Определение хронотипа Сова")
    public void testChronotypeOwlDominant() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 9, 30), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 40),
                        LocalDateTime.of(2025, 10, 3, 10, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 21, 0),
                        LocalDateTime.of(2025, 10, 4, 6, 30), Quality.GOOD)
        );
        assertEquals("Сова", new ChronotypeFunction().apply(sessions).getValue());
    }

    @Test
    @DisplayName("Определение хронотипа Жаворонок")
    public void testChronotypeLarkDominant() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 21, 30),
                        LocalDateTime.of(2025, 10, 2, 5, 30), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 21, 45),
                        LocalDateTime.of(2025, 10, 3, 6, 0), Quality.GOOD)
        );
        assertEquals("Жаворонок", new ChronotypeFunction().apply(sessions).getValue());
    }

    @Test
    @DisplayName("Определение хронотипа Голубь при ничьей")
    public void testChronotypeTieReturnsDove() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 10, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 20, 0),
                        LocalDateTime.of(2025, 10, 3, 6, 0), Quality.GOOD)
        );
        assertEquals("Голубь", new ChronotypeFunction().apply(sessions).getValue());
    }

    @Test
    @DisplayName("Определение хронотипа на пустом логе")
    public void testChronotypeEmptyList() {
        assertEquals("Голубь", new ChronotypeFunction().apply(Collections.emptyList()).getValue());
    }
}