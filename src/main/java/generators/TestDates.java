package generators;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

// Даты для запросов. Стенд работает в часовом поясе UTC.
// OpenMRS принимает два формата:
// дата рождения — 1990-05-10
// дата и время — 2026-11-24T10:00:00.000+0000
public class TestDates {

    private static final DateTimeFormatter DATE =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter DATE_TIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private TestDates() {
    }

    public static ZonedDateTime now() {
        return ZonedDateTime.now(ZoneOffset.UTC);
    }

    // Дата рождения человека, которому сейчас age лет
    public static String birthdateForAge(int age) {
        return now().toLocalDate().minusYears(age).format(DATE);
    }

    // Завтра в указанный час
    public static ZonedDateTime tomorrowAt(int hour) {
        return now().toLocalDate().plusDays(1)
                .atTime(hour, 0).atZone(ZoneOffset.UTC);
    }

    public static String format(ZonedDateTime dateTime) {
        return dateTime.format(DATE_TIME);
    }

    // Преобразование даты OpenMRS в миллисекунды Unix epoch
    public static long toEpochMillis(String dateTime) {
        return OffsetDateTime.parse(dateTime, DATE_TIME)
                .toInstant()
                .toEpochMilli();
    }
}