package ir.topcoders.pol.utils;

public class NumberUtils {

    public static double parseDouble(String value, double defaultValue) {
        try {
            if (value == null || value.length() == 0)
                return defaultValue;
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static float parseFloat(String value, float defaultValue) {
        try {
            if (value == null || value.length() == 0)
                return defaultValue;
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }
}
