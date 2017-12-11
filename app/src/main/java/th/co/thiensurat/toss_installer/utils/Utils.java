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

import th.co.thiensurat.toss_installer.detail.edit.item.DataItem;

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
        try {
            Date date = dbFormat.parse(strdate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH) + 1;
            day = c.get(Calendar.DATE);
            return String.format("%s/%s/%s", day, month, year + 543);
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

        /*if (MyApplication.getInstance().getPrefManager().getApplicationLanguage().equals("TH")) {
            dateFormat = new SimpleDateFormat("dd MMM");
        } else {
            dateFormat = new SimpleDateFormat("dd MMM", Locale.US);
        }*/
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

        /*if (MyApplication.getInstance().getPrefManager().getApplicationLanguage().equals("TH")) {
            monthFormat = new SimpleDateFormat("MMM");
        } else {
            monthFormat = new SimpleDateFormat("MMM", Locale.US);
        }*/
        monthFormat = new SimpleDateFormat("MMM", new Locale("th", "TH"));

        String monthYear = monthFormat.format(date) + " " + subStringforYear(str);
        return monthYear;
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
        /*int fullyear = Integer.parseInt(yearFormat.format(date)) + 543;


        int year = Integer.parseInt(yearFormat.format(date)) + 543;

        String y = "" + year;*/
        Log.e("format year: ", str + ", "+ fullyear);
        return yearFormat.format(date);
    }

    public static List<DataItem> initProvince() {
        List<DataItem> dataItemList = new ArrayList<DataItem>();
        dataItem = new DataItem()
                .setDataid("")
                .setDatacode("")
                .setDataname("");
        dataItemList.add(dataItem);

        return dataItemList;
    }
}
