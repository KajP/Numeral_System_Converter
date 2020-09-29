package converter;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int sourceBase;
        int targetBase;
        String sourceNumber;
        try {
            sourceBase = scanner.nextInt();
            sourceNumber = scanner.next();
            targetBase = scanner.nextInt();

            if (isRadixOutOfRange(sourceBase, targetBase)) {
                throw new IllegalArgumentException();
            }
        } catch (NoSuchElementException | IllegalArgumentException e) {
            System.out.println("error");
            return;
        }

        String[] split = sourceNumber.split("\\.");
        int integerDecimal;
        if (sourceBase > 1) {
            integerDecimal = Integer.parseInt(split[0], sourceBase);
        } else {
            integerDecimal = fromUnary(split[0]);
        }
        double fractionalDecimal = 0.0;
        if (split.length > 1) {
            fractionalDecimal = fromFraction(split[1], sourceBase);
        }
        double decimal = integerDecimal + fractionalDecimal;

        integerDecimal = (int) decimal;
        fractionalDecimal = decimal - integerDecimal;

        String targetInteger;
        if (targetBase > 1) {
            targetInteger = Integer.toString(integerDecimal, targetBase);
        } else {
            targetInteger = toUnary(integerDecimal);
        }
        String targetFraction = toFraction(fractionalDecimal, targetBase);

        System.out.printf("%s.%s%n", targetInteger, targetFraction);
    }

    static boolean isRadixOutOfRange(int radix, int... radixes) {
        boolean result = isRadixOutOfRange(radix);
        for (int i :
                radixes) {
            result = result || isRadixOutOfRange(i);
        }
        return result;
    }

    static boolean isRadixOutOfRange(int radix) {
        return !(1 <= radix && radix <= Character.MAX_RADIX);
    }

    static String toFraction(double num, final int base) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            num *= base;
            int digit = (int) num;
            if (0 <= digit && digit <= 9) {
                builder.append(digit);
            } else {
                char outputDigit = (char) (digit - 10 + 'a');
                builder.append(outputDigit);
            }
            num -= digit;
        }
        return builder.toString();
    }

    static double fromFraction(String num, final int base) {
        double result = 0;
        int dividend = base;

        for (char c :
                num.toCharArray()) {
            double val;
            if ('0' <= c && c <= '9') {
                val = c - '0';
            } else {
                val = c - 'a' + 10;
            }
            result += val / dividend;
            dividend *= base;
        }
        return result;
    }

    static int fromUnary(String source) {
        return source.length();
    }

    static String toUnary(int source) {
        return "1".repeat(Math.max(0, source));
    }

    static String lastDigitOfOct(int num) {
        String s = Integer.toOctalString(num);
        return s.substring(s.length() - 1);
    }

    static String toBinary(int dec) {
        StringBuilder builder = new StringBuilder();
        while (dec != 0) {
            builder.append(dec % 2);
            dec /= 2;
        }
        builder.append("b0");
        return builder.reverse().toString();
    }
}
