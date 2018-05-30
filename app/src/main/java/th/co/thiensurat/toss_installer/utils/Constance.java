package th.co.thiensurat.toss_installer.utils;

import android.Manifest;

/**
 * Created by teerayut.k on 10/16/2017.
 */

public class Constance {

    public static String DBNAME = "toss_installer.sqlite";
    public static String TABLE_JOB = "job";
    public static String TABLE_ADDRESS = "Address";
    public static String TABLE_IMAGE = "image";
    public static String TABLE_PRODUCT = "product";
    public static String TABLE_INSTALL_ITEM = "install_item";
    public static String TABLE_PROVINCE = "Province";
    public static String TABLE_DISTRICT = "Amphur";
    public static String TABLE_SUB_DISTRICT = "District";
    public static String TABLE_STEP = "step";
    public static String TABLE_PAYMENT = "payment";
    public static String TABLE_DEPOSIT = "deposit";
    public static int DB_CURRENT_VERSION = 38;

    public static final String STEP_1 = "step_1";
    public static final String STEP_2 = "step_2";
    public static final String STEP_3 = "step_3";
    public static final String STEP_4 = "step_4";
    public static final String STEP_5 = "step_5";
    public static final String STEP_6 = "step_6";
    public static final String STEP_7 = "step_7";

    public static final String printContactStatus = "productPrintContact";
    public static final String printInstallStatus = "productPrintInstall";

    public static String PRODUCT_STATUS_WAIT = "รอการติดตั้ง";
    public static String PRODUCT_STATUS_READY = "พร้อมติดตั้ง";
    public static String PRODUCT_STATUS_INSTALLED = "ติดตั้งแล้ว";

    public static String IMAGE_TYPE_INSTALL = "install";
    public static String IMAGE_TYPE_CARD = "card";
    public static String IMAGE_TYPE_HOME = "home";
    public static String IMAGE_TYPE_CHECKIN = "checkin";

    public static int REQUEST_SETTINGS = 1;
    public static int REQUEST_CALL_PHONE = 2;
    public static int REQUEST_JOB_DETAIL = 3;
    public static int REQUEST_BLUETOOTH = 4;
    public static int REQUEST_SCAN = 5;
    public static int REQUEST_TAKE_PICTURE = 6;
    public static int REQUEST_CAMERA = 7;
    public static int REQUEST_GALLERY = 8;
    public static int REQUEST_CROP_IMAGE = 9;
    public static int REQUEST_INSTALLATION = 10;
    public static int REQUEST_TAKE_IDCARD = 11;
    public static int REQUEST_TAKE_HOME = 12;
    public static int REQUEST_EXTERNAL_STORAGE = 13;
    public static int REQUEST_CHECKIN_RESULT = 14;
    public static int REQUEST_PRINT_CONTRACT = 15;
    public static int REQUEST_BLUETOOTH_SETTINGS = 16;
    public static int REQUEST_SIGNATURE = 18;
    public static int REQUEST_EDIT_DETAIL = 19;
    public static int REQUEST_LOCATION = 20;
    public static int REQUEST_STEPVIEW= 21;
    public static int REQUEST_TIMELINE = 22;
    public static int REQUEST_BACKUP = 23;
    public static int REQUEST_APP_SETTINGS = 24;
    public static int REQUEST_JOB = 25;
    public static int REQUEST_IMPORT_ITEM = 26;
    public static int REQUEST_ITEM_BALANCE = 27;
    public static int REQUEST_PAYMENT = 28;
    public static int REQUEST_PAYMENT_PAGE = 29;
    public static int REQUEST_PAYMENT_DETAIL = 30;
    public static int REQUEST_PAYMENT_ITEM_LIST = 31;
    public static int REQUEST_DEPOSTI_CHANNEL = 32;
    public static int REQUEST_DEPOSTI = 33;
    public static int REQUEST_NEW_DETAIL = 34;
    public static int REQUEST_CHOICE_INSTALL = 35;
    public static int REQUEST_OLD_INSTALL = 36;
    public static int REQUEST_NEW_INSTALL = 37;
    public static int REQUEST_DELIVERY = 38;


    public static final int REQUEST_PERMISSIONS = 99;

    public static final String UUID = "00001101-0000-1000-8000-00805F9B34FB";

    public static final String DATEPICKER_TAG = "datepicker";

    public static final String KEY_EMPID = "EMPID";
    public static final String KEY_TITLE = "TITLE";
    public static final String KEY_FIRSTNAME = "FIRSTNAME";
    public static final String KEY_LASTNAME = "LASTNAME";
    public static final String KEY_POSITION = "POSITION";
    public static final String KEY_DEPARTMENT = "DEPARTMENT";
    public static final String KEY_EMPLOYEE_TYPE = "EMPLOYEETYPE";

    public static final String KEY_SESSION = "SESSION";
    public static final String KEY_SERIAL = "SERIAL";
    public static final String KEY_PRODUCT_CODE = "PRODUCT_CODE";

    public static final String KEY_SORT_ID = "KEY_SORT_ID";
    public static final String KEY_SORT_PAYMENT_ID = "KEY_SORT_PAYMENT_ID";
    public static final String KEY_CONTNO = "KEY_CONTNO";
    public static final String KEY_SERIAL_ITEM = "KEY_SERIAL_ITEM";
    //public static final String KEY_PRODUCT_CODE = "KEY_PRODUCT_CODE";
    public static final String KEY_CUSTOMER_SIGN_PATH = "KEY_CUSTOMER_SIGN_PATH";

    public static final String KEY_ORDER_ID = "KEY_ORDER_ID";
    public static final String KEY_JOB_ITEM = "KEY_JOB_ITEM";
    public static final String KEY_JOB_ADDR = "KEY_JOB_ADDR";
    public static final String KEY_JOB_PRODUCT = "KEY_JOB_PRODUCT";
    public static final String KEY_PRODUCT_PAYTYPE = "KEY_PRODUCT_PAYTYPE";
    public static final String KEY_PRODUCT_RECEIVE = "KEY_PRODUCT_RECEIVE";

    public static final String KEY_CHANNEL = "KEY_CHANNEL";
    public static final String KEY_CHANNEL_NAME = "KEY_CHANNEL_NAME";
    public static final String KEY_RECEIVER = "KEY_RECEIVER";
    public static final String KEY_REF = "KEY_REF";

    public static final String KEY_LAT = "KEY_LAT";
    public static final String KEY_LON = "KEY_LON";

    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static final int BANK_LIMIT = 50000;
    public static final int HEAD_OFFICE_LIMIT = 50000;
    public static final int COUNTER_SERVICE_LIMIT = 50000;

    public static final String[] arrBranch = { "กรุณาเลือกสาขา"
            , "สำนักงานใหญ่"
            , "สาขานครราชสีมา"
            , "สาขาเชียงใหม่"
            , "สาขาพิษณุโลก"
            , "สาขาชลบุรี"
            , "สาขาเพชรบุรี"
            , "สาขาระยอง"
            , "สาขาราชบุรี"
            , "สาขาปราจีนบุรี"
            , "สาขาขอนแก่น"
            , "สาขานครสวรรค์"
            , "สาขาจันทบุรี"
            , "สาขานครศรีธรรมราช"
            , "สาขาอุบลราชธานี"
            , "สาขาอุดรธานี"
            , "ศูนย์ร้อยเอ็ด"
            , "สาขาสงขลา"
            , "สาขาฉะเชิงเทรา"
            , "สาขาลพบุรี"
            , "สาขานครปฐม"
            , "สาขาสมุทรปราการ"
            , "สาขาภูเก็ต"
            , "สาขาปทุมธานี"
            , "สาขาสุราษฎร์"
            , "สาขามุกดาหาร"
            , "สาขาบุรีรัมย์"
            , "สาขาหาดใหญ่"
            , "สาขาลำปาง"
            , "สาขาเชียงราย"
            , "สาขาเพชรบุรณ์"
            , "สาขาตรัง"
    };
}
