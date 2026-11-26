package generators;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

// Как в nbank-tests: случайные значения для тестовых данных.
public class RandomData {
    private static final Random random = new Random();

    private RandomData() {
    }

    // Добавляется к фамилии, чтобы поиск по фамилии не находил пациентов других тестов.
    // Только строчные буквы — цифры в имени могли бы повлиять на проверки поиска.
    public static String getUniqueSuffix() {
        return RandomStringUtils.insecure().nextAlphabetic(8).toLowerCase();
    }

    public static String getGender() {
        return random.nextBoolean() ? "M" : "F";
    }

    public static int getAge() {
        return 18 + random.nextInt(60);
    }
}
