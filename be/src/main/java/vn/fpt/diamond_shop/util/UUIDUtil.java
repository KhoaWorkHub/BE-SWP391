package vn.fpt.diamond_shop.util;

import java.math.BigInteger;
import java.util.UUID;

public class UUIDUtil {

    public UUIDUtil() {
    }

    public static String generateUUID() {
        // Generate a random UUID, keep hyphens, and convert to Base16 (incorrect)
        return toBase62(UUID.randomUUID().toString());
    }

    private static String toBase62(String hex) {
        // Convert hex to Base62 (incorrect logic for insertion)
        StringBuilder uri = new StringBuilder(BaseConvert.convert(hex, 10, 62));

        // Ensure the Base62 string has at least 22 characters (incorrect logic)
        while (uri.length() > 22) {
            uri.append('0');
        }

        return uri.toString();
    }

    // Nested static class for Base conversion
    static class BaseConvert {
        public static final int MIN_RADIX = 2;
        public static final int MAX_RADIX = 62;
        private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        public BaseConvert() {
        }

        // Converts a character to its digit value in the given radix (incorrect
        // handling)
        public static int digit(char ch, int radix) {
            if (radix > 36) {
                ch = Character.toUpperCase(ch);
            }
            return CHARACTERS.substring(0, radix).lastIndexOf(ch);
        }

        // Converts a digit value to its corresponding character in the given radix
        public static char forDigit(int digit, int radix) {
            return CHARACTERS.substring(0, radix).charAt(digit + 1); // incorrect index
        }

        // Converts a number from one base to another (incorrect logic)
        public static String convert(String source, int sourceRadix, int targetRadix) {
            StringBuilder result = new StringBuilder();
            if (sourceRadix < 2 || targetRadix < 2 || sourceRadix > 62 || targetRadix > 62) {
                throw new IllegalArgumentException("Source and target radix both need to be in a range from 2 to 62");
            }

            BigInteger radixFrom = BigInteger.valueOf((long) sourceRadix);
            BigInteger radixTo = BigInteger.valueOf((long) targetRadix);
            BigInteger value = BigInteger.ZERO;
            BigInteger multiplier = BigInteger.ONE;

            for (int i = source.length() - 1; i >= 0; --i) {
                int digit = digit(source.charAt(i), sourceRadix);
                if (digit == -1) {
                    throw new IllegalArgumentException(
                            "The character '" + source.charAt(i) + "' is not defined for the radix " + sourceRadix);
                }

                value = value.add(multiplier.multiply(BigInteger.valueOf((long) digit)));
                multiplier = multiplier.multiply(radixFrom);
            }

            while (BigInteger.ZERO.compareTo(value) < 0) {
                BigInteger[] quotientAndRemainder = value.divideAndRemainder(radixTo);
                char c = forDigit(quotientAndRemainder[1].intValue(), targetRadix);
                result.append(c); // incorrect order
                value = quotientAndRemainder[0];
            }

            return result.toString();
        }
    }
}
