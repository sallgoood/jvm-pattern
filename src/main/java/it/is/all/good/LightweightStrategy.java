package it.is.all.good;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static java.lang.System.out;

public class LightweightStrategy {
    public static int totalValues(List<Integer> numbers) {
        int total = 0;

        for (int number : numbers) {
            total += number;
        }

        return total;
    }

    public static int totalEvenValues(List<Integer> numbers) {
        int total = 0;

        for (int number : numbers) {
            if (number % 2 == 0) total += number;
        }

        return total;
    }

    public static int totalOddValues(List<Integer> numbers) {
        int total = 0;

        for (int number : numbers) {
            if (number % 2 != 0) total += number;
        }

        return total;
    }

    public static int totalValues(List<Integer> numbers, Predicate<Integer> selector) {
        return numbers.stream()
                .filter(selector)
                .mapToInt(value -> value)
                .sum();
    }

    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(1, 2, 3, 4, 5, 6);
        out.println(totalValues(values));
        out.println(totalEvenValues(values));
        out.println(totalOddValues(values));

        out.println(totalValues(values, value -> true));
        out.println(totalValues(values, value -> value % 2 == 0));
        out.println(totalValues(values, value -> value % 2 != 0));
    }
}
