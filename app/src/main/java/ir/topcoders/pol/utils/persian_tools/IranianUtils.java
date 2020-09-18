package ir.topcoders.pol.utils.persian_tools;

/**
 * Created by eftekhari on 8/5/2015.
 */
public class IranianUtils {

    public static boolean isValidIranianNationalCode(String input) {
        if (!input.matches("^\\d{10}$"))
            return false;

        int iCheck = Integer.parseInt(input.substring(9, 10));

        int iSum = 0;
        for (int iPos = 0; iPos < 9; iPos++) {
            iSum += (Integer.parseInt(input.substring(iPos, iPos + 1)) * (10 - iPos));
        }
        iSum %= 11;

        return iSum < 2 && iCheck == iSum || iSum >= 2 && iCheck + iSum == 11;
    }

    public static CharSequence[] generateYears(int count) {
        count+=2;
        CharSequence[] result = new CharSequence[count];
        PersianCalendar cal = new PersianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        int currentYear = cal.getDateFields().getYear();
        result[0] = "انتخاب کنید";
        result[1] = "اطلاعاتی وجود ندارد";
        for (int i = 2; i < count; i++)
            result[i] = String.valueOf(currentYear - i + 1);
        return result;
    }
}
