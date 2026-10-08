import java.math.BigDecimal;
import java.math.RoundingMode;

public class Money {

    public static long parseCents(String text) {

        String cleaned = text
                .replace("$", "")
                .replace(",", "")
                .trim();

        BigDecimal amount = new BigDecimal(cleaned);

        amount = amount.setScale(2, RoundingMode.HALF_UP);

        return amount
                .movePointRight(2)
                .longValueExact();
    }

    public static String format(long cents) {

        boolean negative = cents < 0;

        long absolute = Math.abs(cents);

        long dollars = absolute / 100;
        long remainingCents = absolute % 100;

        String amount = String.format(
                "$%d.%02d",
                dollars,
                remainingCents);

        if (negative) {
            return "-" + amount;
        }

        return amount;
    }

    public static String formatSigned(long cents) {

        if (cents > 0) {
            return "+" + format(cents);
        }

        return format(cents);
    }
}
