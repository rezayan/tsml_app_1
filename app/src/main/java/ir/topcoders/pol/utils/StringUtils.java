package ir.topcoders.pol.utils;

import java.util.Locale;

public class StringUtils {
    public static String format(String format, Object... args) {
        return String.format(Locale.US, format, args);
    }
}
