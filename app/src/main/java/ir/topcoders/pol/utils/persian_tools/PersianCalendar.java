package ir.topcoders.pol.utils.persian_tools;

import java.util.GregorianCalendar;

import ir.topcoders.pol.utils.StringUtils;

public class PersianCalendar extends GregorianCalendar {
    /**
     *
     */
    private static final long serialVersionUID = 9060514555123543302L;

    public static final long PERSIAN_EPOCH = 1948321;

    public static final int FARVARDIN = 0;

    public static final int ORDIBEHESHT = 1;

    public static final int KHORDAD = 2;

    public static final int TIR = 3;

    public static final int MORDAD = 4;

    public static final int SHAHRIVAR = 5;

    public static final int MEHR = 6;

    public static final int ABAN = 7;

    public static final int AZAR = 8;

    public static final int DEY = 9;

    public static final int BAHMAN = 10;

    public static final int ESFAND = 11;

    public static final String[] persianMonths = {"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر",
            "آبان", "آذر", "دی", "بهمن", "اسفند"};

    public static final String[] persianWeekDays = {"شنبه", "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنجشنبه", "جمعه"};

    public void setDateFields(int year, int month, int day) {
        setDateFields(new DateFields(year, month, day));
    }

    public void setDateFields(DateFields dateFields) {
        DateFields t = convertJdnToGregorian(convertPersianToJdn(dateFields));
        set(YEAR, t.getYear());
        set(MONTH, t.getMonth());
        set(DAY_OF_MONTH, t.getDay());
    }

    public DateFields getDateFields() {
        if (isSet(YEAR) && isSet(MONTH) && isSet(DAY_OF_MONTH)) {
            return convertJdnToPersian(convertGregorianToJdn(get(YEAR), get(MONTH), get(DAY_OF_MONTH)));
        } else {
            return new DateFields();
        }
    }

    public static String getPersianMonthName(int month) {
        return persianMonths[month];
    }

    public String getPersianMonthName() {
        return getPersianMonthName(getDateFields().getMonth());
    }

    public static String getPersianWeekDayName(int weekDay) {
        switch (weekDay) {
            case SATURDAY:
                return persianWeekDays[0];
            case SUNDAY:
                return persianWeekDays[1];
            case MONDAY:
                return persianWeekDays[2];
            case TUESDAY:
                return persianWeekDays[3];
            case WEDNESDAY:
                return persianWeekDays[4];
            case THURSDAY:
                return persianWeekDays[5];
            case FRIDAY:
                return persianWeekDays[6];
        }
        return "";
    }

    public String getPersianWeekDayName() {
        return getPersianWeekDayName(get(DAY_OF_WEEK));
    }

    private static long mysign(double x) {
        return x < 0 ? -1 : (x > 0 ? 1 : 0);
    }

    private static long myfloor(double x) {
        double y = Math.abs(x);
        double f = Math.floor(y);
        return (long) (mysign(x) * f);
    }

    private static long myceil(double x) {
        double y = Math.abs(x) * -1.0;
        double f = Math.floor(y);
        return (long) (-mysign(x) * f);
    }

    @SuppressWarnings("unused")
    private static boolean leapYear(long year) {
        long lYear = year;

        if (lYear > 0)
            return ((((((lYear - (474)) % 2820) + 474) + 38) * 682) % 2816) < 682;
        else
            return ((((((lYear - (473)) % 2820) + 474) + 38) * 682) % 2816) < 682;
    }

    private static long convertPersianToJdn(DateFields t) {
        return convertPersianToJdn(t.getYear(), t.getMonth(), t.getDay());
    }

    private static long convertPersianToJdn(int iYear, int iMonth, int iDay) {
        long lYear = iYear;
        long lMonth = iMonth + 1; // Calculations assume 1-based months
        long lDay = iDay;

        long epbase, epyear, mdays;
        if (lYear >= 0)
            epbase = lYear - 474;
        else
            epbase = lYear - 473;
        epyear = 474 + (epbase % 2820);
        if (lMonth <= 7)
            mdays = (lMonth - 1) * 31;
        else
            mdays = (lMonth - 1) * 30 + 6;

        long jdn = lDay + mdays + myfloor(((epyear * 682) - 110) /* double */ / 2816.0) + (epyear - 1) * 365
                + myfloor(epbase /* double */ / 2820.0) * 1029983 + (PERSIAN_EPOCH - 1);
        return jdn;
    }

    private static DateFields convertJdnToPersian(long jdn) {
        long lYear;
        long lMonth;
        long lDay;

        long depoch, cycle, cyear, ycycle, aux1, aux2, yday;
        depoch = jdn - convertPersianToJdn(475, FARVARDIN, 1);
        cycle = myfloor(depoch /* double */ / 1029983.0);
        cyear = depoch % 1029983;
        if (cyear == 1029982)
            ycycle = 2820;
        else {
            aux1 = myfloor(cyear /* double */ / 366.0);
            aux2 = cyear % 366;
            ycycle = (long) Math.floor(((2134 * aux1) + (2816 * aux2) + 2815) /* double */ / 1028522.0) + aux1 + 1;
        }
        lYear = ycycle + (2820 * cycle) + 474;
        if (lYear <= 0)
            lYear = lYear - 1;
        yday = (jdn - convertPersianToJdn((int) lYear, FARVARDIN, 1)) + 1;
        if (yday <= 186)
            lMonth = myceil(yday /* double */ / 31.0);
        else
            lMonth = myceil((yday - 6) /* double */ / 30.0);
        lDay = (jdn - convertPersianToJdn((int) lYear, (int) lMonth - 1, 1)) + 1;

        DateFields t = new DateFields();
        t.setYear((int) lYear);
        t.setMonth((int) lMonth - 1); // Calculations assume 1-based months
        t.setDay((int) lDay);
        return t;
    }

    @SuppressWarnings("unused")
    private static long convertGregorianToJdn(DateFields t) {
        return convertGregorianToJdn(t.getYear(), t.getMonth(), t.getDay());
    }

    private static long convertGregorianToJdn(int iYear, int iMonth, int iDay) {
        long lYear = iYear;
        long lMonth = iMonth + 1; // Calculations assume 1-based months
        long lDay = iDay;

        if ((lYear > 1582) || ((lYear == 1582) && (lMonth > 10)) || ((lYear == 1582) && (lMonth == 10) && (lDay > 14))) {
            return ((1461 * (lYear + 4800 + ((lMonth - 14) /* long */ / 12))) /* long */ / 4)
                    + ((367 * (lMonth - 2 - 12 * (((lMonth - 14) /* long */ / 12)))) /* long */ / 12)
                    - ((3 * (((lYear + 4900 + ((lMonth - 14) /* long */ / 12)) /* long */ / 100))) /* long */ / 4) + lDay - 32075;
        } else {
            return convertJulianToJdn(iYear, iMonth, iDay);
        }
    }

    private static DateFields convertJdnToGregorian(long jdn) {
        long lYear;
        long lMonth;
        long lDay;

        long l, n, i, j;
        if (jdn > 2299160) {
            l = jdn + 68569;
            n = ((4 * l) /* long */ / 146097);
            l = l - ((146097 * n + 3) /* long */ / 4);
            i = ((4000 * (l + 1)) /* long */ / 1461001);
            l = l - ((1461 * i) /* long */ / 4) + 31;
            j = ((80 * l) /* long */ / 2447);
            lDay = l - ((2447 * j) /* long */ / 80);
            l = (j /* long */ / 11);
            lMonth = j + 2 - 12 * l;
            lYear = 100 * (n - 49) + i + l;

            DateFields t = new DateFields();
            t.setYear((int) lYear);
            t.setMonth((int) lMonth - 1); // Calculations assume 1-based months
            t.setDay((int) lDay);
            return t;
        } else {
            return convertJdnToJulian(jdn);
        }
    }

    @SuppressWarnings("unused")
    private static long convertJulianToJdn(DateFields t) {
        return convertJulianToJdn(t.getYear(), t.getMonth(), t.getDay());
    }

    private static long convertJulianToJdn(int iYear, int iMonth, int iDay) {
        long lYear = iYear;
        long lMonth = iMonth + 1; // Calculations assume 1-based months
        long lDay = iDay;

        return 367 * lYear - ((7 * (lYear + 5001 + ((lMonth - 9) /* long */ / 7))) /* long */ / 4) + ((275 * lMonth) /* long */ / 9)
                + lDay + 1729777;
    }

    private static DateFields convertJdnToJulian(long jdn) {
        long lYear;
        long lMonth;
        long lDay;

        long l, k, n, i, j;
        j = jdn + 1402;
        k = ((j - 1) /* long */ / 1461);
        l = j - 1461 * k;
        n = ((l - 1) /* long */ / 365) - (l /* long */ / 1461);
        i = l - 365 * n + 30;
        j = ((80 * i) /* long */ / 2447);
        lDay = i - ((2447 * j) /* long */ / 80);
        i = (j /* long */ / 11);
        lMonth = j + 2 - 12 * i;
        lYear = 4 * k + n + i - 4716;

        DateFields t = new DateFields();
        t.setYear((int) lYear);
        t.setMonth((int) lMonth - 1); // Calculations assume 1-based months
        t.setDay((int) lDay);
        return t;
    }

    public String getFullDateAsString() {
        try {
            String sCurrentDate = "";
            DateFields df = getDateFields();
            sCurrentDate = getPersianWeekDayName();
            sCurrentDate += "  ";
            sCurrentDate += df.getDay();
            sCurrentDate += " ";
            sCurrentDate += getPersianMonthName(df.getMonth());
            sCurrentDate += " ";
            sCurrentDate += df.getYear();
            return sCurrentDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getSimpleDateAsString() {
        try {
            return getSimpleDateAsString("/");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getSimpleDateAsString(String separator) {
        try {
            String sCurrentDate = "";
            DateFields df = getDateFields();
            sCurrentDate += StringUtils.format("%04d", df.getYear());
            sCurrentDate += separator;
            sCurrentDate += StringUtils.format("%02d", (df.getMonth() + 1));
            sCurrentDate += separator;
            sCurrentDate += StringUtils.format("%02d", df.getDay());
            return sCurrentDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
