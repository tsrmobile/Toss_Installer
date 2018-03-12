package th.co.thiensurat.toss_installer.utils;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.api.result.data.DataItem;


/**
 * Created by teerayut.k on 11/29/2017.
 */

public class Utils {

    private static Context context;
    private static DataItem dataItem;

    public Utils(Context context) {
        this.context = context;
    }

    public static String ConvertDateFormat(String strdate) {
        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
        int year = 0, month = 0, day = 0;
        String d, m, y;
        try {
            Date date = dbFormat.parse(strdate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH) + 1;
            day = c.get(Calendar.DATE);
            if (day < 10) {
                d = "0" + day;
            } else {
                d = String.valueOf(day);
            }

            if (month < 10) {
                m = "0" + month;
            } else {
                m = String.valueOf(month);
            }

            return String.format("%s/%s/%s", d, m, year + 543);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String fullDate(String str) {
        String dateTime = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(str);
        } catch (ParseException parseException) {
            // Date is invalid. Do what you want.
        } catch(Exception exception) {
            // Generic catch. Do what you want.
        }

        dateFormat = new SimpleDateFormat("dd MMM", new Locale("th", "TH"));

        String returnDate = dateFormat.format(date) + " " + subStringforYear(str);
        return returnDate;
    }

    public static String splitDate(String str) {
        String dateTime = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(str);
        } catch (ParseException parseException) {
            // Date is invalid. Do what you want.
        } catch(Exception exception) {
            // Generic catch. Do what you want.
        }

        dateFormat = new SimpleDateFormat("dd");
        return dateFormat.format(date);
    }

    public static String splitMonth(String str) {
        DateFormat monthFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = monthFormat.parse(str);
        } catch (ParseException parseException) {
            // Date is invalid. Do what you want.
        } catch(Exception exception) {
            // Generic catch. Do what you want.
        }
        monthFormat = new SimpleDateFormat("MMM", new Locale("th", "TH"));

        String monthYear = monthFormat.format(date) + " " + subStringforYear(str);
        return monthYear;
    }

    public static String ConvertMonthThaiCharacter(String str) {
        DateFormat monthFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = monthFormat.parse(str);
        } catch (ParseException parseException) {
            // Date is invalid. Do what you want.
        } catch(Exception exception) {
            // Generic catch. Do what you want.
        }
        monthFormat = new SimpleDateFormat("MMM", new Locale("th", "TH"));
        return monthFormat.format(date);
    }

    private static String subStringforYear(String str){
        DateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        String fullyear = null;
        try {
            date = yearFormat.parse(str);
            yearFormat = new SimpleDateFormat("yyyy");
            fullyear = yearFormat.format(date);
            int fyear = Integer.parseInt(fullyear) + 543;
            fullyear = ""+ fyear;
            date = yearFormat.parse(fullyear);
        } catch (ParseException parseException) {
            // Date is invalid. Do what you want.
        } catch(Exception exception) {
            // Generic catch. Do what you want.
        }

        yearFormat = new SimpleDateFormat("yy");
        return yearFormat.format(date);
    }

    public static String ConvertMItoKM(String distance) {
        String[] dis = distance.split(" ");
        double dist = Double.parseDouble(dis[0]) * 1.609;
        return String.valueOf(formatDecimal((float) dist)) + " กม.";
    }

    public static String ConvertDurationToThai(String duration) {
        String[] dur = duration.split(" ");
        if (dur.length > 2) {
            return String.valueOf(dur[0]) + " ชม. " + String.valueOf(dur[2]) + " น.";
        } else {

            return String.valueOf(dur[0]) + " น.";
        }
    }

    public static String formatDecimal(float number) {
        float point = 0.004f;
        if (Math.abs(Math.round(number) - number) < point) {
            return String.format("%,.2f", number);
        } else {
            return String.format("%,.2f", number);
        }
    }

    public static final Calendar FIRST_DAY_OF_TIME;
    public static final Calendar LAST_DAY_OF_TIME;
    public static final int DAYS_OF_TIME;

    static {
        FIRST_DAY_OF_TIME = Calendar.getInstance();
        FIRST_DAY_OF_TIME.set(1900, Calendar.JANUARY, 1);
        LAST_DAY_OF_TIME = Calendar.getInstance();
        LAST_DAY_OF_TIME.set(2100, Calendar.DECEMBER, 31);
        DAYS_OF_TIME = 73413; //(int) ((LAST_DAY_OF_TIME.getTimeInMillis() - FIRST_DAY_OF_TIME.getTimeInMillis()) / (24 * 60 * 60 * 1000));
    }

    /**
     * Get the position in the ViewPager for a given day
     *
     * @param day
     * @return the position or 0 if day is null
     */
    public static int getPositionForDay(Calendar day) {
        if (day != null) {
            return (int) ((day.getTimeInMillis() - FIRST_DAY_OF_TIME.getTimeInMillis())
                    / 86400000  //(24 * 60 * 60 * 1000)
            );
        }
        return 0;
    }

    /**
     * Get the day for a given position in the ViewPager
     *
     * @param position
     * @return the day
     * @throws IllegalArgumentException if position is negative
     */
    public static Calendar getDayForPosition(int position) throws IllegalArgumentException {
        if (position < 0) {
            throw new IllegalArgumentException("position cannot be negative");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(FIRST_DAY_OF_TIME.getTimeInMillis());
        cal.add(Calendar.DAY_OF_YEAR, position);
        return cal;
    }


    public static String getFormattedDate(Context context, long date) {
        final String defaultPattern = "yyyy-MM-dd";

        String pattern = null;
        if (context != null) {
            pattern = context.getString(R.string.date_format);
        }

        if (pattern == null) {
            pattern = defaultPattern;
        }
        SimpleDateFormat simpleDateFormat = null;
        try {
            simpleDateFormat = new SimpleDateFormat(pattern);
        } catch (IllegalArgumentException e) {
            simpleDateFormat = new SimpleDateFormat(defaultPattern);
        }

        return simpleDateFormat.format(new Date(date));
    }


}
