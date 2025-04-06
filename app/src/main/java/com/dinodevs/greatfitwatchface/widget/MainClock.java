package com.dinodevs.greatfitwatchface.widget;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;

import com.dinodevs.greatfitwatchface.data.BatteryLevelRepo;
import com.dinodevs.greatfitwatchface.data.CaloriesRepo;
import com.dinodevs.greatfitwatchface.data.TodayDistanceRepo;
import com.dinodevs.greatfitwatchface.resource.ResourceManager;
import com.huami.watch.watchface.util.Util;
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout;
import com.ingenic.iwds.slpt.view.core.SlptPictureView;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;
import com.ingenic.iwds.slpt.view.digital.SlptHourHView;
import com.ingenic.iwds.slpt.view.digital.SlptHourLView;
import com.ingenic.iwds.slpt.view.utils.SimpleFile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainClock extends DigitalClockWidget {

    private TextPaint timeFont, metaFont;
    private Paint metaIconPaint;
    private Bitmap dateIcon, stepsIcon, distanceIcon, batteryIcon, caloriesIcon;

    private String[] digitalNums = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    private static float META_ICON_SIZE = 23f;
    private static float META_TOP_MARGIN = 106f;
    private static float META_LEFT_MARGIN = 4f;
    private static float META_INTERLINE_MARGIN = 11f;
    private static float META_TEXT_SIZE = 20f;

    private static float TIME_RIGHT_MARGIN = 10f;
    private static float MINUTE_TOP_MARGIN = 65f;

    private static float TIME_TOP_MARGIN = 5f;
    private static float TIME_TEXT_SIZE = 80f;

    private static float DATE_TOP_MARGIN = 50f;

    private static int COLOR_ACCENT = 0xffceaf14;
    private static int COLOR_TEXT = 0xffffffff;

    // Languages
    public static String[] codes = {
            "English", "Български", "中文", "Hrvatski", "Czech", "Dansk", "Nederlands", "Français", "Deutsch", "Ελληνικά", "עברית", "Magyar", "Italiano", "日本語", "한국어", "Polski", "Português", "Română", "Русский", "Slovenčina", "Español", "ไทย", "Türkçe", "Tiếng Việt"
    };

    private static String[][] days = {
            //{"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"},
            {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"},     //English
            {"НЕДЕЛЯ", "ПОНЕДЕЛНИК", "ВТОРНИК", "СРЯДА", "ЧЕТВЪРТЪК", "ПЕТЪК", "СЪБОТА"},       //Bulgarian
            {"星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"},                   //Chinese
            {"NEDJELJA", "PONEDJELJAK", "UTORAK", "SRIJEDA", "ČETVRTAK", "PETAK", "SUBOTA"},    //Croatian
            {"NEDĚLE", "PONDĚLÍ", "ÚTERÝ", "STŘEDA", "ČTVRTEK", "PÁTEK", "SOBOTA"},              //Czech
            {"SØNDAG", "MANDAG", "TIRSDAG", "ONSDAG", "TORSDAG", "FREDAG", "LØRDAG"},            //Danish
            {"ZONDAG", "MAANDAG", "DINSDAG", "WOENSDAG", "DONDERDAG", "VRIJDAG", "ZATERDAG"},   //Dutch
            {"DIMANCHE", "LUNDI", "MARDI", "MERCREDI", "JEUDI", "VENDREDI", "SAMEDI"},          //French
            {"SONNTAG", "MONTAG", "DIENSTAG", "MITTWOCH", "DONNERSTAG", "FREITAG", "SAMSTAG"},  //German
            {"ΚΥΡΙΑΚΉ", "ΔΕΥΤΈΡΑ", "ΤΡΊΤΗ", "ΤΕΤΆΡΤΗ", "ΠΈΜΠΤΗ", "ΠΑΡΑΣΚΕΥΉ", "ΣΆΒΒΑΤΟ"},       //Greek
            {"ש'", "ו'", "ה'", "ד'", "ג'", "ב'", "א'"},                                               //Hebrew
            {"VASÁRNAP", "HÉTFŐ", "KEDD", "SZERDA", "CSÜTÖRTÖK", "PÉNTEK", "SZOMBAT"},          //Hungarian
            {"DOMENICA", "LUNEDÌ", "MARTEDÌ", "MERCOLEDÌ", "GIOVEDÌ", "VENERDÌ", "SABATO"},     //Italian
            {"日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"},                   //Japanese
            {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"},                   //Korean
            {"NIEDZIELA", "PONIEDZIAŁEK", "WTOREK", "ŚRODA", "CZWARTEK", "PIĄTEK", "SOBOTA"},   //Polish
            {"DOMINGO", "SEGUNDA", "TERÇA", "QUARTA", "QUINTA", "SEXTA", "SÁBADO"},             //Portuguese
            {"DUMINICĂ", "LUNI", "MARȚI", "MIERCURI", "JOI", "VINERI", "SÂMBĂTĂ"},              //Romanian
            {"ВОСКРЕСЕНЬЕ", "ПОНЕДЕЛЬНИК", "ВТОРНИК", "СРЕДА", "ЧЕТВЕРГ", "ПЯТНИЦА", "СУББОТА"},//Russian
            {"NEDEĽA", "PONDELOK", "UTOROK", "STREDA", "ŠTVRTOK", "PIATOK", "SOBOTA"},          //Slovak
            {"DOMINGO", "LUNES", "MARTES", "MIÉRCOLES", "JUEVES", "VIERNES", "SÁBADO"},         //Spanish
            {"อาทิตย์", "จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ุกร์", "สาร์"},                               //Thai
            {"PAZAR", "PAZARTESI", "SALı", "ÇARŞAMBA", "PERŞEMBE", "CUMA", "CUMARTESI"},        //Turkish
            {"CHỦ NHẬT", "THỨ 2", "THỨ 3", "THỨ 4", "THỨ 5", "THỨ 6", "THỨ 7"}                   //Vietnamese
    };

    public static String[][] days_3let = {
            //{"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"},
            {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"},                  //English
            {"НЕД", "ПОН", "ВТО", "СРЯ", "ЧЕТ", "ПЕТ", "СЪБ"},                  //Bulgarian
            {"星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"},   //Chinese
            {"NED", "PON", "UTO", "SRI", "ČET", "PET", "SUB"},                  //Croatian
            {"NE", "PO", "ÚT", "ST", "ČT", "PÁ", "SO"},                         //Czech
            {"SØN", "MAN", "TIR", "ONS", "TOR", "FRE", "LØR"},                   //Danish
            {"ZON", "MAA", "DIN", "WOE", "DON", "VRI", "ZAT"},                  //Dutch
            {"DIM", "LUN", "MAR", "MER", "JEU", "VEN", "SAM"},                  //French
            {"SO", "MO", "DI", "MI", "DO", "FR", "SA"},                         //German
            {"ΚΥΡ", "ΔΕΥ", "ΤΡΙ", "ΤΕΤ", "ΠΕΜ", "ΠΑΡ", "ΣΑΒ"},                  //Greek
            {"א'", "ב'", "ג'", "ד'", "ה'", "ו'", "ש'"},                         //Hebrew
            {"VAS", "HÉT", "KED", "SZE", "CSÜ", "PÉN", "SZO"},                  //Hungarian
            {"DOM", "LUN", "MAR", "MER", "GIO", "VEN", "SAB"},                  //Italian
            {"日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"},   //Japanese
            {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"},   //Korean
            {"NIE", "PON", "WTO", "ŚRO", "CZW", "PIĄ", "SOB"},                  //Polish
            {"DOM", "SEG", "TER", "QUA", "QUI", "SEX", "SÁB"},                  //Portuguese
            {"DUM", "LUN", "MAR", "MIE", "JOI", "VIN", "SÂM"},                  //Romanian
            {"ВСК", "ПНД", "ВТР", "СРД", "ЧТВ", "ПТН", "СБТ"},                  //Russian
            {"NED", "PON", "UTO", "STR", "ŠTV", "PIA", "SOB"},                  //Slovak
            {"DOM", "LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB"},                  //Spanish
            {"อา.", "จ.", "อ.", "พ.", "พฤ.", "ศ.", "ส."},                        //Thai
            {"PAZ", "PZT", "SAL", "ÇAR", "PER", "CUM", "CMT"},                  //Turkish
            {"CN", "T2", "T3", "T4", "T5", "T6", "T7"}                           //Vietnamese
    };

    private static String[][] months = {
            //{"DECEMBER", "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"},
            {"DECEMBER", "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"},                               //English
            {"ДЕКЕМВРИ", "ЯНУАРИ", "ФЕВРУАРИ", "МАРТ", "АПРИЛ", "МАЙ", "ЮНИ", "ЮЛИ", "АВГУСТ", "СЕПТЕМВРИ", "ОКТОМВРИ", "НОЕМВРИ", "ДЕКЕМВРИ"},                                  //Bulgarian
            {"十二月", "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"},                                                               //Chinese
            {"PROSINAC", "SIJEČANJ", "VELJAČA", "OŽUJAK", "TRAVANJ", "SVIBANJ", "LIPANJ", "SRPANJ", "KOLOVOZ", "RUJAN", "LISTOPAD", "STUDENI", "PROSINAC"},                       //Croatian
            {"PROSINEC", "LEDEN", "ÚNOR", "BŘEZEN", "DUBEN", "KVĚTEN", "ČERVEN", "ČERVENEC", "SRPEN", "ZÁŘÍ", "ŘÍJEN", "LISTOPAD", "PROSINEC"},                                   //Czech
            {"DECEMBER", "JANUAR", "FEBRUAR", "MARTS", "APRIL", "MAJ", "JUNI", "JULI", "AUGUST", "SEPTEMBER", "OKTOBER", "NOVEMBER", "DECEMBER"},                                 //Danish
            {"DECEMBER", "JANUARI", "FEBRUARI", "MAART", "APRIL", "MEI", "JUNI", "JULI", "AUGUSTUS", "SEPTEMBER", "OKTOBER", "NOVEMBER", "DECEMBER"},                             //Dutch
            {"DÉCEMBRE", "JANVIER", "FÉVRIER", "MARS", "AVRIL", "MAI", "JUIN", "JUILLET", "AOÛT", "SEPTEMBRE", "OCTOBRE", "NOVEMBRE", "DÉCEMBRE"},                                //French
            {"DEZEMBER", "JANUAR", "FEBRUAR", "MÄRZ", "APRIL", "MAI", "JUNI", "JULI", "AUGUST", "SEPTEMBER", "OKTOBER", "NOVEMBER", "DEZEMBER"},                                  //German
            {"ΔΕΚΈΜΒΡΙΟΣ", "ΙΑΝΟΥΆΡΙΟΣ", "ΦΕΒΡΟΥΆΡΙΟΣ", "ΜΆΡΤΙΟΣ", "ΑΠΡΊΛΙΟΣ", "ΜΆΙΟΣ", "ΙΟΎΝΙΟΣ", "ΙΟΎΛΙΟΣ", "ΑΎΓΟΥΣΤΟΣ", "ΣΕΠΤΈΜΒΡΙΟΣ", "ΟΚΤΏΒΡΙΟΣ", "ΝΟΈΜΒΡΙΟΣ", "ΔΕΚΈΜΒΡΙΟΣ"},//Greek
            {"דצמבר", "ינואר", "פברואר", "מרץ", "אפריל", "מאי", "יוני", "יולי", "אוגוסט", "ספטמבר", "אוקטובר", "נובמבר", "דצמבר"},                                                //Hebrew
            {"DECEMBER", "JANUÁR", "FEBRUÁR", "MÁRCIUS", "ÁPRILIS", "MÁJUS", "JÚNIUS", "JÚLIUS", "AUGUSZTUS", "SZEPTEMBER", "OKTÓBER", "NOVEMBER", "DECEMBER"},                  //Hungarian
            {"DICEMBRE", "GENNAIO", "FEBBRAIO", "MARZO", "APRILE", "MAGGIO", "GIUGNO", "LUGLIO", "AGOSTO", "SETTEMBRE", "OTTOBRE", "NOVEMBRE", "DICEMBRE"},                      //Italian
            {"12月", "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"},                                                                        //Japanese
            {"12월", "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"},                                                                        //Korean
            {"GRUDZIEŃ", "STYCZEŃ", "LUTY", "MARZEC", "KWIECIEŃ", "MAJ", "CZERWIEC", "LIPIEC", "SIERPIEŃ", "WRZESIEŃ", "PAŹDZIERNIK", "LISTOPAD", "GRUDZIEŃ"},                  //Polish
            {"DEZEMBRO", "JANEIRO", "FEVEREIRO", "MARÇO", "ABRIL", "MAIO", "JUNHO", "JULHO", "AGOSTO", "SETEMBRO", "OUTUBRO", "NOVEMBRO", "DEZEMBRO"},                          //Portuguese
            {"DECEMBRIE", "IANUARIE", "FEBRUARIE", "MARTIE", "APRILIE", "MAI", "IUNIE", "IULIE", "AUGUST", "SEPTEMBRIE", "OCTOMBRIE", "NOIEMBRIE", "DECEMBRIE"},                //Romanian
            {"ДЕКАБРЬ", "ЯНВАРЬ", "ФЕВРАЛЬ", "МАРТ", "АПРЕЛЬ", "МАЙ", "ИЮНЬ", "ИЮЛЬ", "АВГУСТ", "СЕНТЯБРЬ", "ОКТЯБРЬ", "НОЯБРЬ", "ДЕКАБРЬ"},                                    //Russian
            {"DECEMBER", "JANUÁR", "FEBRUÁR", "MAREC", "APRÍL", "MÁJ", "JÚN", "JÚL", "AUGUST", "SEPTEMBER", "OKTÓBER", "NOVEMBER", "DECEMBER"},                                 //Slovak
            {"DICIEMBRE", "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"},                         //Spanish
            {"ันวาคม", "มกราคม", "กุมภาพันธ์", "ีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน"},                                                 //Thai
            {"ARALıK", "OCAK", "ŞUBAT", "MART", "NISAN", "MAYıS", "HAZIRAN", "TEMMUZ", "AĞUSTOS", "EYLÜL", "EKIM", "KASıM", "ARALıK"},                                          //Turkish
            {"THÁNG 12", "THÁNG 1", "THÁNG 2", "THÁNG 3", "THÁNG 4", "THÁNG 5", "THÁNG 6", "THÁNG 7", "THÁNG 8", "THÁNG 9", "THÁNG 10", "THÁNG 11", "THÁNG 12"}                 //Vietnamese
    };

    private static String[][] months_3let = {
            //{"DECEMBER", "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"},
            {"DEC", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"},            //English
            {"ДЕК", "ЯНУ", "ФЕВ", "МАР", "АПР", "МАЙ", "ЮНИ", "ЮЛИ", "АВГ", "СЕП", "ОКТ", "НОЕ", "ДЕК"},            //Bulgarian
            {"十二月", "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"}, //Chinese
            {"PRO", "SIJ", "VE", "OŽU", "TRA", "SVI", "LIP", "SRP", "KOL", "RUJ", "LIS", "STU", "PRO"},             //Croatian
            {"PRO", "LED", "ÚNO", "BŘE", "DUB", "KVĚ", "ČER", "ČER", "SRP", "ZÁŘ", "ŘÍJ", "LIS", "PRO"},            //Czech
            {"DEC", "JAN", "FEB", "MAR", "APR", "MAJ", "JUN", "JUL", "AUG", "SEP", "OKT", "NOV", "DEC"},            //Danish
            {"DEC", "JAN", "FEB", "MAA", "APR", "MEI", "JUN", "JUL", "AUG", "SEP", "OKT", "NOV", "DEC"},            //Dutch
            {"DÉC", "JAN", "FÉV", "MAR", "AVR", "MAI", "JUI", "JUI", "AOÛ", "SEP", "OCT", "NOV", "DÉC"},            //French
            {"DEZ", "JAN", "FEB", "MÄR", "APR", "MAI", "JUN", "JUL", "AUG", "SEP", "OKT", "NOV", "DEZ"},            //German
            {"ΔΕΚ", "ΙΑΝ", "ΦΕΒ", "ΜΑΡ", "ΑΠΡ", "ΜΑΙ", "ΙΟΥΝ", "ΙΟΥΛ", "ΑΥΓ", "ΣΕΠ", "ΟΚΤ", "ΝΟΕ", "ΔΕΚ"},          //Greek
            {"דצמ", "ינו", "פבר", "מרץ", "אפר", "מאי", "יונ", "יול", "אוג", "ספט", "אוק", "נוב", "דצמ"},            //Hebrew
            {"DEC", "JAN", "FEB", "MÁR", "ÁPR", "MÁJ", "JÚN", "JÚL", "AUG", "SZE", "OKT", "NOV", "DEC"},            //Hungarian
            {"DIC", "GEN", "FEB", "MAR", "APR", "MAG", "GIU", "LUG", "AGO", "SET", "OTT", "NOV", "DIC"},            //Italian
            {"12月", "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"},            //Japanese
            {"12월", "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"},            //Korean
            {"GRU", "STY", "LUT", "MAR", "KWI", "MAJ", "CZE", "LIP", "SIE", "WRZ", "PAŹ", "LIS", "GRU"},            //Polish
            {"DEZ", "JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV", "DEZ"},            //Portuguese
            {"DEC", "IAN", "FEB", "MAR", "APR", "MAI", "IUN", "IUL", "AUG", "SEP", "OCT", "NOI", "DEC"},            //Romanian
            {"ДЕК", "ЯНВ", "ФЕВ", "МАР", "АПР", "МАЙ", "ИЮН", "ИЮЛ", "АВГ", "СЕН", "ОКТ", "НОЯ", "ДЕК"},            //Russian
            {"DEC", "JAN", "FEB", "MAR", "APR", "MÁJ", "JÚN", "JÚL", "AUG", "SEP", "OKT", "NOV", "DEC"},            //Slovak
            {"DIC", "ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC"},            //Spanish
            {"ธ.ค.", "ม.ค.", "ก.พ.", "มี.ค.", "เม.ย.", "พ.ค.", "มิ.ย.", "ก.ค.", "ส.ค.", "ก.ย.", "ต.ค.", "พ.ย.", "ธ.ค."},//Thai
            {"ARA", "OCA", "ŞUB", "MAR", "NIS", "MAY", "HAZ", "TEM", "AĞU", "EYL", "EKI", "KAS", "ARA"},            //Turkish
            {"T12", "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12"}                      //Vietnamese
    };

    private final BatteryLevelRepo batteryLevelRepo;
    private final CaloriesRepo caloriesRepo;
    private final TodayDistanceRepo distanceRepo;

    public MainClock(BatteryLevelRepo batteryLevelRepo,
                     CaloriesRepo caloriesRepo,
                     TodayDistanceRepo distanceRepo) {
        this.batteryLevelRepo = batteryLevelRepo;
        this.caloriesRepo = caloriesRepo;
        this.distanceRepo = distanceRepo;
    }

    @Override
    public void init(Context context) {
        //this.background = service.getResources().getDrawable(R.drawable.background); //todo
        //this.background.setBounds(0, 0, 320, 300);
        this.dateIcon = Util.decodeImage(context.getResources(), "icons/date.png");
        this.stepsIcon = Util.decodeImage(context.getResources(), "icons/steps.png");
        this.distanceIcon = Util.decodeImage(context.getResources(), "icons/today_distance.png");
        this.batteryIcon = Util.decodeImage(context.getResources(), "icons/battery.png");
        this.caloriesIcon = Util.decodeImage(context.getResources(), "icons/calories.png");

        this.timeFont = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.timeFont.setTypeface(ResourceManager.getTypeFace(context.getResources(), ResourceManager.Font.GoogleSansMedium));
        this.timeFont.setTextSize(TIME_TEXT_SIZE);
        this.timeFont.setColor(COLOR_ACCENT);
        this.timeFont.setTextAlign(Paint.Align.RIGHT);

        this.metaFont = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.metaFont.setTypeface(ResourceManager.getTypeFace(context.getResources(), ResourceManager.Font.GoogleSansMedium));
        this.metaFont.setTextSize(META_TEXT_SIZE);
        this.metaFont.setColor(COLOR_TEXT);
        this.metaFont.setTextAlign(Paint.Align.LEFT);

        this.metaIconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.metaIconPaint.setColorFilter(new PorterDuffColorFilter(COLOR_ACCENT, PorterDuff.Mode.SRC_IN));
    }

    // Screen open watch mode
    @Override
    public void onDrawDigital(Canvas canvas, float width, float height, float centerX, float centerY, int seconds, int minutes, int hours, int year, int month, int day, int week, int ampm) {
        // Draw background image
        //this.background.draw(canvas);

        canvas.drawColor(Color.BLACK);

        // Draw time
        canvas.drawText(
                Util.formatTime(hours),
                centerX - TIME_RIGHT_MARGIN,
                centerY + TIME_TOP_MARGIN,
                this.timeFont
        );

        canvas.drawText(
                Util.formatTime(minutes),
                centerX - TIME_RIGHT_MARGIN,
                centerY + TIME_TOP_MARGIN + MINUTE_TOP_MARGIN,
                this.timeFont
        );

        // JAVA calendar get/show time library
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, week);

        final Paint.FontMetrics metaFontMetrics = this.metaFont.getFontMetrics();
        final float metaTopOffset = metaFontMetrics.top;

        for (int i = 0; i < 4; i++) {
            final MetadataItem metadataItem = MetadataItem.values()[i];
            final float extraTopOffset = i * (META_INTERLINE_MARGIN + META_ICON_SIZE);
            final float y = META_TOP_MARGIN + extraTopOffset;
            switch (metadataItem) {
                case STEPS:
                    break;
                case DISTANCE:
                    canvas.drawBitmap(
                            this.distanceIcon,
                            new Rect(0, 0, this.distanceIcon.getWidth(), this.distanceIcon.getHeight()),
                            new RectF(centerX, y, centerX + META_ICON_SIZE, y + META_ICON_SIZE),
                            this.metaIconPaint
                    );
                    canvas.drawText(
                            this.distanceRepo.getTodayDistanceKm() + " KM",
                            centerX + META_ICON_SIZE + META_LEFT_MARGIN,
                            y - metaTopOffset,
                            this.metaFont
                    );
                    break;
                case BATTERY:
                    canvas.drawBitmap(
                            this.batteryIcon,
                            new Rect(0, 0, this.batteryIcon.getWidth(), this.batteryIcon.getHeight()),
                            new RectF(centerX, y, centerX + META_ICON_SIZE, y + META_ICON_SIZE),
                            this.metaIconPaint
                    );
                    canvas.drawText(
                            this.batteryLevelRepo.getBatteryLevel() + "%",
                            centerX + META_ICON_SIZE + META_LEFT_MARGIN,
                            y - metaTopOffset,
                            this.metaFont
                    );
                    break;
                case CALORIES:
                    canvas.drawBitmap(
                            this.caloriesIcon,
                            new Rect(0, 0, this.caloriesIcon.getWidth(), this.caloriesIcon.getHeight()),
                            new RectF(centerX, y, centerX + META_ICON_SIZE, y + META_ICON_SIZE),
                            this.metaIconPaint
                    );
                    canvas.drawText(
                            this.caloriesRepo.getCaloriesBurnt() + " KCAL",
                            centerX + META_ICON_SIZE + META_LEFT_MARGIN,
                            y - metaTopOffset,
                            this.metaFont
                    );
                    break;
            }
        }


        final int weekdaynum = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        final String weekday = days_3let[0][weekdaynum];
        final String dateText = weekday + " " + months_3let[0][month] + " " + Util.formatTime(day);
        final float dateTextWidth = this.metaFont.measureText(dateText);
        final float totalDateWidth = META_ICON_SIZE + META_LEFT_MARGIN + dateTextWidth;

        final float dateStartX = centerX - totalDateWidth / 2;
        canvas.drawBitmap(
                this.dateIcon,
                new Rect(0, 0, this.dateIcon.getWidth(), this.dateIcon.getHeight()),
                new RectF(dateStartX, DATE_TOP_MARGIN, dateStartX + META_ICON_SIZE, DATE_TOP_MARGIN + META_ICON_SIZE),
                this.metaIconPaint
        );
        canvas.drawText(
                dateText,
                dateStartX + META_ICON_SIZE + META_LEFT_MARGIN,
                DATE_TOP_MARGIN - metaTopOffset,
                this.metaFont
        );
    }

    // Screen locked/closed watch mode (Slpt mode)
    @Override
    public List<SlptViewComponent> buildSlptViewComponent(Service service) {
        return buildSlptViewComponent(service, false);
    }

    public List<SlptViewComponent> buildSlptViewComponent(Service service, boolean better_resolution) {
        // TODO: 4/6/2025 Must add background in order to reset previous frame
        List<SlptViewComponent> slpt_objects = new ArrayList<>();

        final SlptPictureView background = new SlptPictureView();
        background.setImagePicture(SimpleFile.readFileFromAssets(service, "background_slpt.png"));
        slpt_objects.add(background);

        final Typeface typeFace = ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.GoogleSansMedium);

        final SlptLinearLayout hourLayout = new SlptLinearLayout();
        hourLayout.add(new SlptHourHView());
        hourLayout.add(new SlptHourLView());
        hourLayout.setStringPictureArrayForAll(this.digitalNums);
        hourLayout.setTextAttrForAll(
                TIME_TEXT_SIZE,
                COLOR_ACCENT,
                typeFace
        );
        // Position based on screen on
        hourLayout.alignX = 2;
        hourLayout.alignY = 0;
        hourLayout.setRect(90, 125);
        hourLayout.setStart(160, 160);
        //Add it to the list
        slpt_objects.add(hourLayout);

        /*// Set font
        Typeface timeTypeFace = ResourceManager.getTypeFace(service.getResources(), settings.font);

        if(settings.digital_clock) {
            // Draw hours
            if (settings.hoursBool) {
                SlptLinearLayout hourLayout = new SlptLinearLayout();
                if (settings.no_0_on_hour_first_digit) {// No 0 on first digit
                    SlptViewComponent firstDigit = new SlptHourHView();
                    ((SlptNumView) firstDigit).setStringPictureArray(this.digitalNumsNo0);
                    hourLayout.add(firstDigit);
                    SlptViewComponent secondDigit = new SlptHourLView();
                    ((SlptNumView) secondDigit).setStringPictureArray(this.digitalNums);
                    hourLayout.add(secondDigit);
                } else {
                    hourLayout.add(new SlptHourHView());
                    hourLayout.add(new SlptHourLView());
                    hourLayout.setStringPictureArrayForAll(this.digitalNums);
                }
                hourLayout.setTextAttrForAll(
                        settings.hoursFontSize,
                        settings.hoursColor,
                        timeTypeFace
                );
                // Position based on screen on
                hourLayout.alignX = 2;
                hourLayout.alignY = 0;
                hourLayout.setRect(
                        (int) (2 * settings.hoursLeft + 640),
                        (int) (((float) settings.font_ratio / 100) * settings.hoursFontSize)
                );
                hourLayout.setStart(
                        -320,
                        (int) (settings.hoursTop - ((float) settings.font_ratio / 100) * settings.hoursFontSize)
                );
                //Add it to the list
                slpt_objects.add(hourLayout);
            }

            // Draw minutes
            if (settings.minutesBool) {
                SlptLinearLayout minuteLayout = new SlptLinearLayout();
                minuteLayout.add(new SlptMinuteHView());
                minuteLayout.add(new SlptMinuteLView());
                minuteLayout.setStringPictureArrayForAll(this.digitalNums);
                minuteLayout.setTextAttrForAll(
                        settings.minutesFontSize,
                        settings.minutesColor,
                        timeTypeFace
                );
                // Position based on screen on
                minuteLayout.alignX = 2;
                minuteLayout.alignY = 0;
                minuteLayout.setRect(
                        (int) (2 * settings.minutesLeft + 640),
                        (int) (((float) settings.font_ratio / 100) * settings.minutesFontSize)
                );
                minuteLayout.setStart(
                        -320,
                        (int) (settings.minutesTop - ((float) settings.font_ratio / 100) * settings.minutesFontSize)
                );
                //Add it to the list
                slpt_objects.add(minuteLayout);
            }

            // Draw indicator
            if (settings.indicatorBool) {
                SlptLinearLayout indicatorLayout = new SlptLinearLayout();
                SlptPictureView colon = new SlptPictureView();
                colon.setStringPicture(":");
                indicatorLayout.add(colon);
                indicatorLayout.setTextAttrForAll(
                        settings.indicatorFontSize,
                        settings.indicatorColor,
                        timeTypeFace
                );
                // Position based on screen on
                indicatorLayout.alignX = 2;
                indicatorLayout.alignY = 0;
                indicatorLayout.setRect(
                        (int) (2 * settings.indicatorLeft + 640),
                        (int) (((float) settings.font_ratio / 100) * settings.indicatorFontSize)
                );
                indicatorLayout.setStart(
                        -320,
                        (int) (settings.indicatorTop - ((float) settings.font_ratio / 100) * settings.indicatorFontSize)
                );
                //Add it to the list
                slpt_objects.add(indicatorLayout);
            }

            // Draw Seconds
            if (settings.secondsBool ) { //&& (!settings.isVerge() || better_resolution)
                SlptLinearLayout secondsLayout = new SlptLinearLayout();
                secondsLayout.add(new SlptSecondHView());
                secondsLayout.add(new SlptSecondLView());
                secondsLayout.setTextAttrForAll(
                        settings.secondsFontSize,
                        settings.secondsColor,
                        ResourceManager.getTypeFace(service.getResources(), settings.font)
                );
                // Position based on screen on
                secondsLayout.alignX = 2;
                secondsLayout.alignY = 0;
                secondsLayout.setRect(
                        (int) (2 * settings.secondsLeft + 640),
                        (int) (((float) settings.font_ratio / 100) * settings.secondsFontSize)
                );
                secondsLayout.setStart(
                        -320,
                        (int) (settings.secondsTop - ((float) settings.font_ratio / 100) * settings.secondsFontSize)
                );
                //Add it to the list
                slpt_objects.add(secondsLayout);
            }

            // AM-PM (ONLY FOR 12h format)
            SlptLinearLayout ampm = new SlptLinearLayout();
            SlptPictureView am = new SlptPictureView();
            SlptPictureView pm = new SlptPictureView();
            am.setStringPicture("AM");
            pm.setStringPicture("PM");
            SlptSportUtil.setAmBgView(am);
            SlptSportUtil.setPmBgView(pm);
            ampm.add(am);
            ampm.add(pm);
            ampm.setTextAttrForAll(settings.am_pmFontSize, settings.am_pmColor, ResourceManager.getTypeFace(service.getResources(), settings.font));
            ampm.alignX = 2;
            ampm.alignY = 0;
            tmp_left = (int) this.settings.am_pmLeft;
            if (!this.settings.am_pmAlignLeft) {
                ampm.setRect((tmp_left * 2) + 640, (int) this.settings.am_pmFontSize);
                tmp_left = -320;
            }
            ampm.setStart(tmp_left, (int) (this.settings.am_pmTop - ((settings.font_ratio / 100.0f) * this.settings.am_pmFontSize)));
            slpt_objects.add(ampm);
        }

        if(settings.analog_clock) {
            SlptAnalogHourView slptAnalogHourView = new SlptAnalogHourView();
            slptAnalogHourView.setImagePicture(SimpleFile.readFileFromAssets(service, "timehand/8c/hour"+ ((settings.isVerge())?"_verge":"") +".png"));
            slptAnalogHourView.alignX = (byte) 2;
            slptAnalogHourView.alignY = (byte) 2;
            slptAnalogHourView.setRect(320 + (settings.isVerge()?40:0), 320 + (settings.isVerge()?40:0));
            slpt_objects.add(slptAnalogHourView);

            SlptAnalogMinuteView slptAnalogMinuteView = new SlptAnalogMinuteView();
            slptAnalogMinuteView.setImagePicture(SimpleFile.readFileFromAssets(service, "timehand/8c/minute"+ ((settings.isVerge())?"_verge":"") +".png"));
            slptAnalogMinuteView.alignX = (byte) 2;
            slptAnalogMinuteView.alignY = (byte) 2;
            slptAnalogMinuteView.setRect(320 + (settings.isVerge()?40:0), 320 + (settings.isVerge()?40:0));
            slpt_objects.add(slptAnalogMinuteView);

            if(settings.secondsBool){
                SlptAnalogSecondView slptAnalogSecondView = new SlptAnalogSecondView();
                slptAnalogSecondView.setImagePicture(SimpleFile.readFileFromAssets(service, "timehand/8c/seconds"+ ((settings.isVerge())?"_verge":"") +".png"));
                slptAnalogSecondView.alignX = (byte) 2;
                slptAnalogSecondView.alignY = (byte) 2;
                slptAnalogSecondView.setRect(320 + (settings.isVerge()?40:0), 320 + (settings.isVerge()?40:0));
                slpt_objects.add(slptAnalogSecondView);
            }
        }

        // Only CLOCK?
        if (!show_all)
            return slpt_objects;

        // Draw DATE (30.12.2018)
        if(settings.date>0){
            // Show or Not icon
            if (settings.dateIcon) {
                SlptPictureView dateIcon = new SlptPictureView();
                dateIcon.setImagePicture( SimpleFile.readFileFromAssets(service, ( (better_resolution)?"26wc_":"slpt_" )+"icons/"+settings.is_white_bg+"date.png") );
                dateIcon.setStart(
                        (int) settings.dateIconLeft,
                        (int) settings.dateIconTop
                );
                slpt_objects.add(dateIcon);
            }

            // Set . string
            SlptPictureView point = new SlptPictureView();
            point.setStringPicture(".");
            SlptPictureView point2 = new SlptPictureView();
            point2.setStringPicture(".");

            SlptLinearLayout dateLayout = new SlptLinearLayout();
            dateLayout.add(new SlptDayHView());
            dateLayout.add(new SlptDayLView());
            dateLayout.add(point);//add .
            dateLayout.add(new SlptMonthHView());
            dateLayout.add(new SlptMonthLView());
            dateLayout.add(point2);//add .
            dateLayout.add(new SlptYear3View());
            dateLayout.add(new SlptYear2View());
            dateLayout.add(new SlptYear1View());
            dateLayout.add(new SlptYear0View());
            dateLayout.setTextAttrForAll(
                    settings.dateFontSize,
                    settings.dateColor,
                    timeTypeFace);
            // Position based on screen on
            dateLayout.alignX = 2;
            dateLayout.alignY = 0;
            tmp_left = (int) settings.dateLeft;
            if(!settings.dateAlignLeft) {
                // If text is centered, set rectangle
                dateLayout.setRect(
                        (int) (2 * tmp_left + 640),
                        (int) (((float)settings.font_ratio/100)*settings.dateFontSize)
                );
                tmp_left = -320;
            }
            dateLayout.setStart(
                    tmp_left,
                    (int) (settings.dateTop-((float)settings.font_ratio/100)*settings.dateFontSize)
            );
            //Add it to the list
            slpt_objects.add(dateLayout);
        }

        // Draw day of month
        if(settings.dayBool){
            SlptLinearLayout dayLayout = new SlptLinearLayout();
            dayLayout.add(new SlptDayHView());
            dayLayout.add(new SlptDayLView());
            dayLayout.setTextAttrForAll(
                    settings.dayFontSize,
                    settings.dayColor,
                    timeTypeFace);
            // Position based on screen on
            dayLayout.alignX = 2;
            dayLayout.alignY = 0;
            tmp_left = (int) settings.dayLeft;
            if(!settings.dayAlignLeft) {
                // If text is centered, set rectangle
                dayLayout.setRect(
                        (int) (2 * tmp_left + 640),
                        (int) (((float)settings.font_ratio/100)*settings.dayFontSize)
                );
                tmp_left = -320;
            }
            dayLayout.setStart(
                    tmp_left,
                    (int) (settings.dayTop-((float)settings.font_ratio/100)*settings.dayFontSize)
            );
            //Add it to the list
            slpt_objects.add(dayLayout);
        }

        // Draw month
        if(settings.monthBool){
            // JAVA calendar get/show time library
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH);

            SlptLinearLayout monthLayout = new SlptLinearLayout();

            // if as text
            if(settings.month_as_text) {
                monthLayout.add(new SlptMonthLView());

                // Fix 00 type of month
                if(month>=9){ // 9: October, 10: November, 11: December
                    months_3let[settings.language][0] = months_3let[settings.language][10];
                    months_3let[settings.language][1] = months_3let[settings.language][11];
                    months_3let[settings.language][2] = months_3let[settings.language][12];
                    months[settings.language][0] = months[settings.language][10];
                    months[settings.language][1] = months[settings.language][11];
                    months[settings.language][2] = months[settings.language][12];
                }

                if (settings.three_letters_month_if_text) {
                    monthLayout.setStringPictureArrayForAll(months_3let[settings.language]);
                } else {
                    monthLayout.setStringPictureArrayForAll(months[settings.language]);
                }

            // if as number
            }else{
                // show first digit
                if(month>=9 || !settings.no_0_on_hour_first_digit){
                    monthLayout.add(new SlptMonthHView());
                }
                monthLayout.add(new SlptMonthLView());
            }

            monthLayout.setTextAttrForAll(
                    settings.monthFontSize,
                    settings.monthColor,
                    timeTypeFace);
            // Position based on screen on
            monthLayout.alignX = 2;
            monthLayout.alignY = 0;
            tmp_left = (int) settings.monthLeft;
            if(!settings.monthAlignLeft) {
                // If text is centered, set rectangle
                monthLayout.setRect(
                        (int) (2 * tmp_left + 640),
                        (int) (((float)settings.font_ratio/100)*settings.monthFontSize)
                );
                tmp_left = -320;
            }
            monthLayout.setStart(
                    tmp_left,
                    (int) (settings.monthTop-((float)settings.font_ratio/100)*settings.monthFontSize)
            );
            //Add it to the list
            slpt_objects.add(monthLayout);
        }

        // Draw year number
        if(settings.yearBool){
            SlptLinearLayout yearLayout = new SlptLinearLayout();
            yearLayout.add(new SlptYear3View());
            yearLayout.add(new SlptYear2View());
            yearLayout.add(new SlptYear1View());
            yearLayout.add(new SlptYear0View());
            yearLayout.setTextAttrForAll(
                    settings.yearFontSize,
                    settings.yearColor,
                    timeTypeFace
            );
            // Position based on screen on
            yearLayout.alignX = 2;
            yearLayout.alignY = 0;
            tmp_left = (int) settings.yearLeft;
            if(!settings.yearAlignLeft) {
                // If text is centered, set rectangle
                yearLayout.setRect(
                        (int) (2 * tmp_left + 640),
                        (int) (((float)settings.font_ratio/100)*settings.yearFontSize)
                );
                tmp_left = -320;
            }
            yearLayout.setStart(
                    tmp_left,
                    (int) (settings.yearTop-((float)settings.font_ratio/100)*settings.yearFontSize)
            );
            //Add it to the list
            slpt_objects.add(yearLayout);
        }

        // Set day name font
        Typeface weekfont = ResourceManager.getTypeFace(service.getResources(), settings.font);

        // Draw day name
        if(settings.weekdayBool){
            SlptLinearLayout WeekdayLayout = new SlptLinearLayout();
            WeekdayLayout.add(new SlptWeekView());
            if(settings.three_letters_day_if_text){
                WeekdayLayout.setStringPictureArrayForAll(days_3let[settings.language]);
            }else{
                WeekdayLayout.setStringPictureArrayForAll(days[settings.language]);
            }
            WeekdayLayout.setTextAttrForAll(
                    settings.weekdayFontSize,
                    settings.weekdayColor,
                    weekfont
            );
            // Position based on screen on
            WeekdayLayout.alignX = 2;
            WeekdayLayout.alignY = 0;
            tmp_left = (int) settings.weekdayLeft;
            if(!settings.weekdayAlignLeft) {
                // If text is centered, set rectangle
                WeekdayLayout.setRect(
                        (int) (2 * tmp_left + 640),
                        (int) (((float)settings.font_ratio/100)*settings.weekdayFontSize)
                );
                tmp_left = -320;
            }
            WeekdayLayout.setStart(
                    tmp_left,
                    (int) (settings.weekdayTop-((float)settings.font_ratio/100)*settings.weekdayFontSize)
            );
            //Add it to the list
            slpt_objects.add(WeekdayLayout);
        }*/

        return slpt_objects;
    }

    private enum MetadataItem {
        STEPS,
        DISTANCE,
        CALORIES,
        BATTERY,
    }
}
