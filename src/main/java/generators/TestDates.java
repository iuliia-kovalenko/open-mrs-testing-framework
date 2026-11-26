package generators;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

// Даты для запросов. Стенд работает в часовом поясе UTC, поэтому «сейчас» тоже берём в UTC.
// OpenMRS принимает два формата:
//   дата рождения — 1990-05-10
//   дата и время  — 2026-11-24T10:00:00.000+0000
public class TestDates {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private TestDates() {
    }

    public static ZonedDateTime now() {
        return ZonedDateTime.now(ZoneOffset.UTC);
    }

    // Дата рождения человека, которому сейчас age лет
    public static String birthdateForAge(int age) {
        return now().toLocalDate().minusYears(age).format(DATE);
    }

    // Завтра в указанный час: встречи создаются на завтра (раздел А.4 тест-плана)
    public static ZonedDateTime tomorrowAt(int hour) {
        return now().toLocalDate().plusDays(1).atTime(hour, 0).atZone(ZoneOffset.UTC);
    }

    public static String format(ZonedDateTime dateTime) {
        return dateTime.format(DATE_TIME);
    }
}
