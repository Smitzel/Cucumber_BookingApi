package utils;

import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestData {
    public static final Faker faker = new Faker();

    public static String getInvalidToken() {
        return faker.internet().uuid();
    }

    public static String getFirstName() {
        return faker.name().firstName();
    }

    public static String getLastName() {
        return faker.name().lastName();
    }

    public static int getPrice(int min, int max) {
        return faker.number().numberBetween(min, max);
    }

    public static boolean getRandomBoolEan() {
        return faker.bool().bool();
    }

    public static String getCheckInDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int randomDays = faker.number().numberBetween(1, 25);
        return LocalDate.now().minusDays(randomDays).format(formatter);
    }

    public static String getCheckOutDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int randomDays = faker.number().numberBetween(1, 51);
        return LocalDate.now().plusDays(randomDays).format(formatter);
    }

    public static String getRandomNeeds() {
        return faker.food().dish();
    }
}

