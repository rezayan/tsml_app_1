package ir.topcoders.pol.utils;

import android.util.SparseArray;

import java.util.HashMap;

public class ProvinceUtils {

    private static SparseArray<String> PROVINCES = new SparseArray<>();
    private static SparseArray<SparseArray<String>> COUNTIES = new SparseArray<>();

    static {
        PROVINCES.put(0, "انتخاب کنید");
//        PROVINCES.put(1, "خوزستان"); this is old version
        PROVINCES.put(11, "آذربايجان شرقي");
        PROVINCES.put(12, "آذربايجان غربي");
        PROVINCES.put(13, "اردبيل");
        PROVINCES.put(14, "اصفهان");
        PROVINCES.put(15, "ايلام");
        PROVINCES.put(16, "بوشهر");
        PROVINCES.put(17, "تهران");
        PROVINCES.put(18, "چهارمحال و بختياري");
        PROVINCES.put(19, "خراسان جنوبي");
        PROVINCES.put(20, "خراسان رضوي");
        PROVINCES.put(21, "خراسان شمالي");
        PROVINCES.put(22, "خوزستان");
        PROVINCES.put(23, "زنجان");
        PROVINCES.put(24, "سمنان");
        PROVINCES.put(25, "سيستان و بلوچستان");
        PROVINCES.put(26, "فارس");
        PROVINCES.put(27, "قزوين");
        PROVINCES.put(28, "قم");
        PROVINCES.put(29, "کردستان");
        PROVINCES.put(30, "کرمان");
        PROVINCES.put(31, "کرمانشاه");
        PROVINCES.put(32, "کهگیلویه و بویراحمد");
        PROVINCES.put(33, "گلستان");
        PROVINCES.put(34, "گيلان");
        PROVINCES.put(35, "لرستان");
        PROVINCES.put(36, "مازندران");
        PROVINCES.put(37, "مرکزي");
        PROVINCES.put(38, "هرمزگان");
        PROVINCES.put(39, "همدان");
        PROVINCES.put(40, "يزد");
        PROVINCES.put(41, "البرز");
        PROVINCES.put(50, "ايرانشهر");
        PROVINCES.put(51, "کرمان جنوب");
        PROVINCES.put(52, "لارستان");

        SparseArray<String> default_province = new SparseArray<>(); //انتخاب کنید
        default_province.put(0, "انتخاب کنید");
        COUNTIES.put(0, default_province);

        SparseArray<String> provinces = new SparseArray<>(); //آذربايجان شرقي
        provinces.put(0, "انتخاب کنید");
        provinces.put(1121, "آذرشهر");
        provinces.put(1122, "اسکو");
        provinces.put(1123, "اهر");
        provinces.put(1124, "بستان آباد");
        provinces.put(1125, "بناب");
        provinces.put(1126, "تبريز");
        provinces.put(1127, "جلفا");
        provinces.put(1128, "چاراويماق");
        provinces.put(1129, "سراب");
        provinces.put(1130, "شبستر");
        provinces.put(1131, "عجب شير");
        provinces.put(1132, "مراغه");
        provinces.put(1133, "مرند");
        provinces.put(1134, "هريس");
        provinces.put(1135, "هشترود");
        provinces.put(1136, "کليبر");
        provinces.put(1137, "ملکان");
        provinces.put(1138, "ميانه");
        provinces.put(1139, "ورزقان");
        provinces.put(1140, "خداآفرين");
        COUNTIES.put(11, provinces);

        provinces = new SparseArray<>(); //آذربايجان غربي
        provinces.put(0, "انتخاب کنید");
        provinces.put(1221, "اروميه");
        provinces.put(1222, "اشنويه");
        provinces.put(1223, "بوکان");
        provinces.put(1224, "پلدشت");
        provinces.put(1225, "پيرانشهر");
        provinces.put(1226, "تکاب");
        provinces.put(1227, "چالدران");
        provinces.put(1228, "چايپاره");
        provinces.put(1229, "خوي");
        provinces.put(1230, "سردشت");
        provinces.put(1231, "سلماس");
        provinces.put(1232, "شاهين دژ");
        provinces.put(1233, "شوط");
        provinces.put(1234, "ماکو");
        provinces.put(1235, "مهاباد");
        provinces.put(1236, "مياندوآب");
        provinces.put(1237, "نقده");
        COUNTIES.put(12, provinces);

        provinces = new SparseArray<>(); //اردبيل
        provinces.put(0, "انتخاب کنید");
        provinces.put(1321, "اردبيل");
        provinces.put(1322, "بيله سوار");
        provinces.put(1323, "پارس آباد");
        provinces.put(1324, "خلخال");
        provinces.put(1325, "کوثر");
        provinces.put(1326, "گرمي");
        provinces.put(1327, "مشگين شهر");
        provinces.put(1328, "نمين");
        provinces.put(1329, "نير");
        provinces.put(1330, "سرعين");
        COUNTIES.put(13, provinces);

        provinces = new SparseArray<>(); //اصفهان
        provinces.put(0, "انتخاب کنید");
        provinces.put(1421, "آران وبيدگل");
        provinces.put(1422, "اردستان");
        provinces.put(1423, "اصفهان");
        provinces.put(1424, "برخوار");
        provinces.put(1425, "تيران وکرون");
        provinces.put(1426, "چادگان");
        provinces.put(1427, "خميني شهر");
        provinces.put(1428, "خوانسار");
        provinces.put(1429, "دهاقان");
        provinces.put(1430, "سميرم");
        provinces.put(1431, "شاهين شهروميمه");
        provinces.put(1432, "شهرضا");
        provinces.put(1433, "فريدن");
        provinces.put(1434, "فريدونشهر");
        provinces.put(1435, "فلاورجان");
        provinces.put(1436, "کاشان");
        provinces.put(1437, "گلپايگان");
        provinces.put(1438, "لنجان");
        provinces.put(1439, "مبارکه");
        provinces.put(1440, "نائين");
        provinces.put(1441, "نجف آباد");
        provinces.put(1442, "نطنز");
        provinces.put(1443, "خوروبيابانک");
        provinces.put(1444, "بوئين ومياندشت");
        COUNTIES.put(14, provinces);

        provinces = new SparseArray<>(); //ايلام
        provinces.put(0, "انتخاب کنید");
        provinces.put(1521, "آبدانان");
        provinces.put(1522, "ايلام");
        provinces.put(1523, "ايوان");
        provinces.put(1524, "دره شهر");
        provinces.put(1525, "دهلران");
        provinces.put(1526, "چرداول");
        provinces.put(1527, "ملکشاهي");
        provinces.put(1528, "مهران");
        provinces.put(1529, "سيروان");
        provinces.put(1530, "بدره");
        COUNTIES.put(15, provinces);

        provinces = new SparseArray<>(); //بوشهر
        provinces.put(0, "انتخاب کنید");
        provinces.put(1621, "بوشهر");
        provinces.put(1622, "تنگستان");
        provinces.put(1623, "جم");
        provinces.put(1624, "دشتستان");
        provinces.put(1625, "دشتي");
        provinces.put(1626, "دير");
        provinces.put(1627, "ديلم");
        provinces.put(1628, "کنگان");
        provinces.put(1629, "گناوه");
        provinces.put(1630, "عسلويه");
        COUNTIES.put(16, provinces);

        provinces = new SparseArray<>(); //تهران
        provinces.put(0, "انتخاب کنید");
        provinces.put(1721, "اسلامشهر");
        provinces.put(1722, "پاکدشت");
        provinces.put(1723, "تهران");
        provinces.put(1724, "دماوند");
        provinces.put(1725, "رباط کريم");
        provinces.put(1726, "ري");
        provinces.put(1728, "شميرانات");
        provinces.put(1729, "شهريار");
        provinces.put(1730, "فيروزکوه");
        provinces.put(1733, "ورامين");
        provinces.put(1734, "بهارستان");
        provinces.put(1735, "پيشوا");
        provinces.put(1736, "قدس");
        provinces.put(1737, "ملارد");
        provinces.put(1738, "پرديس");
        provinces.put(1739, "قرچک");
        COUNTIES.put(17, provinces);

        provinces = new SparseArray<>(); //چهارمحال و بختياري
        provinces.put(0, "انتخاب کنید");
        provinces.put(1821, "اردل");
        provinces.put(1822, "بروجن");
        provinces.put(1823, "شهرکرد");
        provinces.put(1824, "فارسان");
        provinces.put(1825, "کوهرنگ");
        provinces.put(1826, "کيار");
        provinces.put(1827, "لردگان");
        provinces.put(1828, "سامان");
        provinces.put(1829, "بن");
        COUNTIES.put(18, provinces);

        provinces = new SparseArray<>(); //خراسان جنوبي
        provinces.put(0, "انتخاب کنید");
        provinces.put(1921, "بشرويه");
        provinces.put(1922, "بيرجند");
        provinces.put(1923, "درميان");
        provinces.put(1924, "سرايان");
        provinces.put(1925, "سربيشه");
        provinces.put(1926, "فردوس");
        provinces.put(1927, "قائنات");
        provinces.put(1928, "نهبندان");
        provinces.put(1929, "طبس");
        provinces.put(1930, "خوسف");
        provinces.put(1931, "زيرکوه");
        COUNTIES.put(19, provinces);

        provinces = new SparseArray<>(); //خراسان رضوي
        provinces.put(0, "انتخاب کنید");
        provinces.put(2021, "بجستان");
        provinces.put(2022, "بردسکن");
        provinces.put(2023, "بينالود");
        provinces.put(2024, "تايباد");
        provinces.put(2025, "فيروزه");
        provinces.put(2026, "تربت جام");
        provinces.put(2027, "تربت حيدريه");
        provinces.put(2028, "جغتاي");
        provinces.put(2029, "جوين");
        provinces.put(2030, "چناران");
        provinces.put(2031, "خليل آباد");
        provinces.put(2032, "خواف");
        provinces.put(2033, "درگز");
        provinces.put(2034, "رشتخوار");
        provinces.put(2035, "زاوه");
        provinces.put(2036, "سبزوار");
        provinces.put(2037, "سرخس");
        provinces.put(2038, "فريمان");
        provinces.put(2039, "قوچان");
        provinces.put(2040, "کاشمر");
        provinces.put(2041, "کلات");
        provinces.put(2042, "گناباد");
        provinces.put(2043, "مشهد");
        provinces.put(2044, "مه ولات");
        provinces.put(2045, "نيشابور");
        provinces.put(2046, "باخرز");
        provinces.put(2047, "خوشاب");
        provinces.put(2048, "داورزن");
        COUNTIES.put(20, provinces);

        provinces = new SparseArray<>(); //خراسان شمالي
        provinces.put(0, "انتخاب کنید");
        provinces.put(2121, "اسفراين");
        provinces.put(2122, "بجنورد");
        provinces.put(2123, "جاجرم");
        provinces.put(2124, "شيروان");
        provinces.put(2125, "فاروج");
        provinces.put(2126, "گرمه");
        provinces.put(2127, "مانه وسملقان");
        provinces.put(2128, "رازوجرگلان");
        COUNTIES.put(21, provinces);

        provinces = new SparseArray<>(); //خوزستان
        provinces.put(0, "انتخاب کنید");
        provinces.put(2221, "آبادان");
        provinces.put(2222, "اميديه");
        provinces.put(2223, "انديکا");
        provinces.put(2224, "انديمشک");
        provinces.put(2225, "اهواز");
        provinces.put(2226, "ايذه");
        provinces.put(2227, "باغ ملک");
        provinces.put(2228, "بندرماهشهر");
        provinces.put(2229, "بهبهان");
        provinces.put(2230, "خرمشهر");
        provinces.put(2231, "دزفول");
        provinces.put(2232, "دشت آزادگان");
        provinces.put(2233, "رامشير");
        provinces.put(2234, "رامهرمز");
        provinces.put(2235, "شادگان");
        provinces.put(2236, "شوش");
        provinces.put(2237, "شوشتر");
        provinces.put(2238, "گتوند");
        provinces.put(2239, "لالي");
        provinces.put(2240, "مسجدسليمان");
        provinces.put(2241, "هفتگل");
        provinces.put(2242, "هنديجان");
        provinces.put(2243, "هويزه");
        provinces.put(2244, "باوي");
        provinces.put(2245, "آغاجاري");
        provinces.put(2246, "کارون");
        provinces.put(2247, "حميديه");
        COUNTIES.put(22, provinces);

        //this is old version
//        provinces = new SparseArray<>(); //خوزستان
//        provinces.put(0, "انتخاب کنید");
//        provinces.put(1, "آبادان");
//        provinces.put(2, "آغاجاري");
//        provinces.put(3, "اميديه");
//        provinces.put(4, "انديكا");
//        provinces.put(5, "انديمشك");
//        provinces.put(6, "اهواز");
//        provinces.put(7, "ايذه");
//        provinces.put(8, "باغ ملك");
//        provinces.put(9, "باوي");
//        provinces.put(10, "بندر ماهشهر");
//        provinces.put(11, "بهبهان");
//        provinces.put(12, "حميديه");
//        provinces.put(13, "خرمشهر");
//        provinces.put(14, "دزفول");
//        provinces.put(15, "دشت آزادگان");
//        provinces.put(16, "رامشير");
//        provinces.put(17, "رامهرمز");
//        provinces.put(18, "شادگان");
//        provinces.put(19, "شوش");
//        provinces.put(20, "شوشتر");
//        provinces.put(21, "كارون");
//        provinces.put(22, "گتوند");
//        provinces.put(23, "لالي");
//        provinces.put(24, "مسجد سليمان");
//        provinces.put(25, "هفتکل");
//        provinces.put(26, "هنديجان");
//        provinces.put(27, "هويزه");
//        provinces.put(28, "سوسنگرد");
//        provinces.put(29, "بستان");
//        COUNTIES.put(1, provinces);

        provinces = new SparseArray<>(); //زنجان
        provinces.put(0, "انتخاب کنید");
        provinces.put(2321, "ابهر");
        provinces.put(2322, "ايجرود");
        provinces.put(2323, "خدابنده");
        provinces.put(2324, "خرمدره");
        provinces.put(2325, "زنجان");
        provinces.put(2326, "طارم");
        provinces.put(2327, "ماه نشان");
        provinces.put(2328, "سلطانيه");
        COUNTIES.put(23, provinces);

        provinces = new SparseArray<>(); //سمنان
        provinces.put(0, "انتخاب کنید");
        provinces.put(2421, "دامغان");
        provinces.put(2422, "سمنان");
        provinces.put(2423, "شاهرود");
        provinces.put(2424, "گرمسار");
        provinces.put(2425, "مهدي شهر");
        provinces.put(2426, "آرادان");
        provinces.put(2427, "ميامي");
        provinces.put(2428, "سرخه");
        provinces.put(2429, "شهميرزاد");
        COUNTIES.put(24, provinces);

        provinces = new SparseArray<>(); //سيستان و بلوچستان
        provinces.put(0, "انتخاب کنید");
        provinces.put(2523, "خاش");
        provinces.put(2525, "زابل");
        provinces.put(2526, "مهرستان");
        provinces.put(2527, "زاهدان");
        provinces.put(2528, "زهک");
        provinces.put(2529, "سراوان");
        provinces.put(2531, "سيب وسوران");
        provinces.put(2533, "هيرمند");
        provinces.put(2536, "هامون");
        provinces.put(2537, "نيمروز");
        provinces.put(2538, "ميرجاوه");
        COUNTIES.put(25, provinces);

        provinces = new SparseArray<>(); //فارس
        provinces.put(0, "انتخاب کنید");
        provinces.put(2621, "آباده");
        provinces.put(2622, "ارسنجان");
        provinces.put(2623, "استهبان");
        provinces.put(2624, "اقليد");
        provinces.put(2625, "بوانات");
        provinces.put(2626, "پاسارگاد");
        provinces.put(2627, "جهرم");
        provinces.put(2628, "خرم بيد");
        provinces.put(2630, "داراب");
        provinces.put(2631, "رستم");
        provinces.put(2632, "زرين دشت");
        provinces.put(2633, "سپيدان");
        provinces.put(2634, "سروستان");
        provinces.put(2635, "شيراز");
        provinces.put(2636, "فراشبند");
        provinces.put(2637, "فسا");
        provinces.put(2638, "فيروزآباد");
        provinces.put(2639, "قيروکارزين");
        provinces.put(2640, "کازرون");
        provinces.put(2643, "مرودشت");
        provinces.put(2644, "ممسني");
        provinces.put(2646, "ني ريز");
        provinces.put(2647, "خرامه");
        provinces.put(2648, "کوار");
        COUNTIES.put(26, provinces);

        provinces = new SparseArray<>(); //قزوين
        provinces.put(0, "انتخاب کنید");
        provinces.put(2721, "آبيک");
        provinces.put(2722, "البرز");
        provinces.put(2723, "بوئين زهرا");
        provinces.put(2724, "تاکستان");
        provinces.put(2725, "قزوين");
        provinces.put(2726, "آوج");
        COUNTIES.put(27, provinces);

        provinces = new SparseArray<>(); //قم
        provinces.put(0, "انتخاب کنید");
        provinces.put(2821, "قم");
        COUNTIES.put(28, provinces);

        provinces = new SparseArray<>(); //کردستان
        provinces.put(0, "انتخاب کنید");
        provinces.put(2921, "بانه");
        provinces.put(2922, "بيجار");
        provinces.put(2923, "دهگلان");
        provinces.put(2924, "ديواندره");
        provinces.put(2925, "سروآباد");
        provinces.put(2926, "سقز");
        provinces.put(2927, "سنندج");
        provinces.put(2928, "قروه");
        provinces.put(2929, "کامياران");
        provinces.put(2930, "مريوان");
        COUNTIES.put(29, provinces);

        provinces = new SparseArray<>(); //کرمان
        provinces.put(0, "انتخاب کنید");
        provinces.put(3021, "بافت");
        provinces.put(3022, "بردسير");
        provinces.put(3023, "بم");
        provinces.put(3025, "راور");
        provinces.put(3026, "رفسنجان");
        provinces.put(3028, "ريگان");
        provinces.put(3029, "زرند");
        provinces.put(3030, "سيرجان");
        provinces.put(3031, "شهربابک");
        provinces.put(3034, "کرمان");
        provinces.put(3036, "کوهبنان");
        provinces.put(3038, "ارزوئيه");
        provinces.put(3039, "انار");
        provinces.put(3040, "رابر");
        provinces.put(3042, "فهرج");
        provinces.put(3043, "نرماشير");
        COUNTIES.put(30, provinces);

        provinces = new SparseArray<>(); //کرمانشاه
        provinces.put(0, "انتخاب کنید");
        provinces.put(3121, "اسلام آبادغرب");
        provinces.put(3122, "پاوه");
        provinces.put(3123, "ثلاث باباجاني");
        provinces.put(3124, "جوانرود");
        provinces.put(3125, "دالاهو");
        provinces.put(3126, "روانسر");
        provinces.put(3127, "سرپل ذهاب");
        provinces.put(3128, "سنقر");
        provinces.put(3129, "صحنه");
        provinces.put(3130, "قصرشيرين");
        provinces.put(3131, "کرمانشاه");
        provinces.put(3132, "کنگاور");
        provinces.put(3133, "گيلانغرب");
        provinces.put(3134, "هرسين");
        COUNTIES.put(31, provinces);

        provinces = new SparseArray<>(); //کهگيلويه و بويراحمد
        provinces.put(0, "انتخاب کنید");
        provinces.put(3221, "بهمئي");
        provinces.put(3222, "بويراحمد");
        provinces.put(3223, "دنا");
        provinces.put(3224, "کهگيلويه");
        provinces.put(3225, "گچساران");
        provinces.put(3226, "باشت");
        provinces.put(3227, "چرام");
        provinces.put(3228, "لنده");
        COUNTIES.put(32, provinces);

        //this is old version
//        provinces = new SparseArray<>(); //کهگیلویه و بویراحمد
//        provinces.put(0, "انتخاب کنید");
//        provinces.put(407, "باشت");
//        provinces.put(408, "بهمئی");
//        provinces.put(409, "بویراحمد");
//        provinces.put(413, "چرام");
//        provinces.put(410, "دنا");
//        provinces.put(411, "کهگیلویه");
//        provinces.put(414, "گچساران");
//        provinces.put(412, "لنده");
//        COUNTIES.put(32, provinces);

        provinces = new SparseArray<>(); //گلستان
        provinces.put(0, "انتخاب کنید");
        provinces.put(3321, "آزادشهر");
        provinces.put(3322, "آق قلا");
        provinces.put(3323, "بندرگز");
        provinces.put(3324, "ترکمن");
        provinces.put(3325, "راميان");
        provinces.put(3326, "علي آباد");
        provinces.put(3327, "کردکوي");
        provinces.put(3328, "کلاله");
        provinces.put(3329, "گرگان");
        provinces.put(3330, "گنبدکاووس");
        provinces.put(3331, "مراوه تپه");
        provinces.put(3332, "مينودشت");
        provinces.put(3333, "گاليکش");
        provinces.put(3334, "گميشان");
        COUNTIES.put(33, provinces);

        provinces = new SparseArray<>(); //گيلان
        provinces.put(0, "انتخاب کنید");
        provinces.put(3421, "آستارا");
        provinces.put(3422, "آستانه اشرفيه");
        provinces.put(3423, "املش");
        provinces.put(3424, "بندرانزلي");
        provinces.put(3425, "رشت");
        provinces.put(3426, "رضوانشهر");
        provinces.put(3427, "رودبار");
        provinces.put(3428, "رودسر");
        provinces.put(3429, "سياهکل");
        provinces.put(3430, "شفت");
        provinces.put(3431, "صومعه سرا");
        provinces.put(3432, "طوالش");
        provinces.put(3433, "فومن");
        provinces.put(3434, "لاهيجان");
        provinces.put(3435, "لنگرود");
        provinces.put(3436, "ماسال");
        COUNTIES.put(34, provinces);

        provinces = new SparseArray<>(); //لرستان
        provinces.put(0, "انتخاب کنید");
        provinces.put(3521, "ازنا");
        provinces.put(3522, "اليگودرز");
        provinces.put(3523, "بروجرد");
        provinces.put(3524, "پلدختر");
        provinces.put(3525, "خرم آباد");
        provinces.put(3526, "دلفان");
        provinces.put(3528, "دورود");
        provinces.put(3529, "سلسله");
        provinces.put(3530, "کوهدشت");
        provinces.put(3531, "دوره");
        provinces.put(3532, "رومشکان");
        COUNTIES.put(35, provinces);

        provinces = new SparseArray<>(); //مازندران
        provinces.put(0, "انتخاب کنید");
        provinces.put(3621, "آمل");
        provinces.put(3622, "بابل");
        provinces.put(3623, "بابلسر");
        provinces.put(3624, "بهشهر");
        provinces.put(3625, "تنکابن");
        provinces.put(3626, "جويبار");
        provinces.put(3627, "چالوس");
        provinces.put(3628, "رامسر");
        provinces.put(3629, "ساري");
        provinces.put(3630, "سوادکوه");
        provinces.put(3631, "فريدونکنار");
        provinces.put(3632, "قائم شهر");
        provinces.put(3633, "گلوگاه");
        provinces.put(3634, "محمودآباد");
        provinces.put(3635, "نکا");
        provinces.put(3636, "نور");
        provinces.put(3637, "نوشهر");
        provinces.put(3638, "عباس آباد");
        provinces.put(3639, "مياندورود");
        provinces.put(3640, "سيمرغ");
        provinces.put(3641, "کلاردشت");
        provinces.put(3642, "سوادکوه شمالي");
        COUNTIES.put(36, provinces);

        provinces = new SparseArray<>(); //مرکزي
        provinces.put(0, "انتخاب کنید");
        provinces.put(3721, "آشتيان");
        provinces.put(3722, "اراک");
        provinces.put(3723, "تفرش");
        provinces.put(3724, "خمين");
        provinces.put(3725, "خنداب");
        provinces.put(3726, "دليجان");
        provinces.put(3727, "زرنديه");
        provinces.put(3728, "ساوه");
        provinces.put(3729, "شازند");
        provinces.put(3730, "کميجان");
        provinces.put(3731, "محلات");
        provinces.put(3732, "فراهان");
        COUNTIES.put(37, provinces);

        provinces = new SparseArray<>(); //هرمزگان
        provinces.put(0, "انتخاب کنید");
        provinces.put(3821, "ابوموسي");
        provinces.put(3822, "بستک");
        provinces.put(3824, "بندرلنگه");
        provinces.put(3825, "بندرعباس");
        provinces.put(3826, "پارسيان");
        provinces.put(3827, "جاسک");
        provinces.put(3828, "حاجي آباد");
        provinces.put(3829, "خمير");
        provinces.put(3830, "رودان");
        provinces.put(3831, "سيريک");
        provinces.put(3832, "قشم");
        provinces.put(3833, "ميناب");
        provinces.put(3834, "بشاگرد");
        COUNTIES.put(38, provinces);

        provinces = new SparseArray<>(); //همدان
        provinces.put(0, "انتخاب کنید");
        provinces.put(3921, "اسدآباد");
        provinces.put(3922, "بهار");
        provinces.put(3923, "تويسرکان");
        provinces.put(3924, "رزن");
        provinces.put(3925, "کبودرآهنگ");
        provinces.put(3926, "ملاير");
        provinces.put(3927, "نهاوند");
        provinces.put(3928, "همدان");
        provinces.put(3932, "فامنين");
        COUNTIES.put(39, provinces);

        provinces = new SparseArray<>(); //يزد
        provinces.put(0, "انتخاب کنید");
        provinces.put(4021, "ابرکوه");
        provinces.put(4022, "اردکان");
        provinces.put(4023, "بافق");
        provinces.put(4024, "تفت");
        provinces.put(4025, "خاتم");
        provinces.put(4026, "اشکذر");
        provinces.put(4028, "مهريز");
        provinces.put(4029, "ميبد");
        provinces.put(4030, "يزد");
        provinces.put(4031, "بهاباد");
        COUNTIES.put(40, provinces);

        provinces = new SparseArray<>(); //البرز
        provinces.put(0, "انتخاب کنید");
        provinces.put(4121, "ساوجبلاغ");
        provinces.put(4122, "طالقان");
        provinces.put(4123, "کرج");
        provinces.put(4124, "نظرآباد");
        provinces.put(4125, "اشتهارد");
        provinces.put(4126, "فرديس");
        COUNTIES.put(41, provinces);

        provinces = new SparseArray<>(); //ايرانشهر
        provinces.put(0, "انتخاب کنید");
        provinces.put(5021, "ايرانشهر");
        provinces.put(5022, "چاه بهار");
        provinces.put(5024, "دلگان");
        provinces.put(5030, "سرباز");
        provinces.put(5032, "کنارک");
        provinces.put(5034, "نيک شهر");
        provinces.put(5035, "قصرقند");
        provinces.put(5039, "فنوج");
        COUNTIES.put(50, provinces);

        provinces = new SparseArray<>(); //کرمان جنوب
        provinces.put(0, "انتخاب کنید");
        provinces.put(5124, "جيرفت");
        provinces.put(5127, "رودبارجنوب");
        provinces.put(5132, "عنبرآباد");
        provinces.put(5133, "قلعه گنج");
        provinces.put(5135, "کهنوج");
        provinces.put(5137, "منوجان");
        provinces.put(5141, "فارياب");
        COUNTIES.put(51, provinces);

        provinces = new SparseArray<>(); //لارستان
        provinces.put(0, "انتخاب کنید");
        provinces.put(5229, "خنج");
        provinces.put(5241, "لارستان");
        provinces.put(5242, "لامرد");
        provinces.put(5245, "مهر");
        provinces.put(5249, "گراش");
        COUNTIES.put(52, provinces);
    }

    public static String[] getProvinces() {
        return extractTextFromSparseArray(PROVINCES);
    }

    public static String[] getCountiesByProvinceIndex(int index) {
        return getCountiesByProvinceCode(getProvinceCodeByIndex(index));
    }

    public static String[] getCountiesByProvinceCode(int code) {
        if (COUNTIES.indexOfKey(code) != -1)
            return extractTextFromSparseArray(COUNTIES.get(code));
        return null;
    }

    private static String[] extractTextFromSparseArray(SparseArray<String> keyValues) {
        if (keyValues != null && keyValues.size() > 0) {
            String[] titles = new String[keyValues.size()];
            for (int index = 0; index < keyValues.size(); index++)
                titles[index] = keyValues.valueAt(index);
            return titles;
        }
        return null;
    }

    public static int getProvinceCodeByIndex(int index) {
        if (index >= 0 && index < PROVINCES.size())
            return PROVINCES.keyAt(index);
        return 0;
    }

    public static int getProvinceIndexByCode(int code) {
        if (PROVINCES.indexOfKey(code) != -1)
            return PROVINCES.indexOfKey(code);
        return 0;
    }

    public static int getCountyIndexByCode(int provinceCode, int countyCode) {
        if (COUNTIES.indexOfKey(provinceCode) != -1) {
            SparseArray<String> counties = COUNTIES.get(provinceCode);
            if (counties.indexOfKey(countyCode) != -1)
                return counties.indexOfKey(countyCode);
        }
        return 0;
    }

    public static int getCountyCodeByIndex(int provinceCode, int countyIndex) {
        if (COUNTIES.indexOfKey(provinceCode) != -1) {
            SparseArray<String> counties = COUNTIES.get(provinceCode);
            if (countyIndex >= 0 && countyIndex < counties.size())
                return counties.keyAt(countyIndex);
        }
        return 0;
    }
}
