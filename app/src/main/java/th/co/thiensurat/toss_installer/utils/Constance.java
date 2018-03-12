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
    public static int DB_CURRENT_VERSION = 34;

    public static final String STEP_1 = "step_1";
    public static final String STEP_2 = "step_2";
    public static final String STEP_3 = "step_3";
    public static final String STEP_4 = "step_4";
    public static final String STEP_5 = "step_5";
    public static final String STEP_6 = "step_6";
    public static final String STEP_7 = "step_7";

    public static final String printContactStatus = "productPrintContact";
    public static final String printInstallStatus = "productPrintInstall";

    public static int STATUS_DEFAULT = 21;
    public static int STATUS_JOB_FINISH = 01;
    public static int STATUS_CANCEL_ORDER = 90;
    public static int STATUS_CANCEL_INSTALL = 91;

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
    public static final int REQUEST_CONNECT_BT = 0*2300;
    public static final int REQUEST_CONNECT_DEVICE = 17;
    public static final int REQUEST_SIGNATURE = 18;
    public static int REQUEST_EDIT_DETAIL = 19;
    public static int REQUEST_LOCATION = 20;
    public static int REQUEST_STEPVIEW= 21;
    public static int REQUEST_TIMELINE = 22;
    public static int REQUEST_BACKUP = 23;

    public static final String UUID = "00001101-0000-1000-8000-00805F9B34FB";

    public static final String DATEPICKER_TAG = "datepicker";

    public static final String STATE_SCAN = "STATE_SCAN";
    public static final String STATE_NEXT = "STATE_NEXT";
    public static final String STATE_STATUS = "STATE_STATUS";
    public static final String STATE_PRODUCT_CODE = "STATE_PRODUCT_CODE";
    public static final String STATE_DASHBOARD = "STATE_DASHBOARD";
    public static final String STATE_JOB = "STATE_JOB";
    public static final String STATE_INSTALLATION_IMAGE = "STATE_INSTALLATION_IMAGE";

    public static final String KEY_EMPID = "EMPID";
    public static final String KEY_TITLE = "TITLE";
    public static final String KEY_FIRSTNAME = "FIRSTNAME";
    public static final String KEY_LASTNAME = "LASTNAME";
    public static final String KEY_POSITION = "POSITION";
    public static final String KEY_DEPARTMENT = "DEPARTMENT";

    public static final String KEY_SERIAL = "SERIAL";
    public static final String KEY_PRODUCT_CODE = "PRODUCT_CODE";

    public static final String KEY_SORT_ID = "KEY_SORT_ID";
    public static final String KEY_CONTNO = "KEY_CONTNO";
    public static final String KEY_SERIAL_ITEM = "KEY_SERIAL_ITEM";
    //public static final String KEY_PRODUCT_CODE = "KEY_PRODUCT_CODE";
    public static final String KEY_CUSTOMER_SIGN_PATH = "KEY_CUSTOMER_SIGN_PATH";

    public static final String KEY_ORDER_ID = "KEY_ORDER_ID";
    public static final String KEY_JOB_ITEM = "KEY_JOB_ITEM";
    public static final String KEY_JOB_ADDR = "KEY_JOB_ADDR";
    public static final String KEY_JOB_PRODUCT = "KEY_JOB_PRODUCT";
    public static final String KEY_CUSTOMER_NAME = "KEY_CUSTOMER_NAME";
    public static final String KEY_FIRST_OPEN = "KEY_FIRST_OPEN";
    public static final String EXTRA_DEVICE_ADDRESS = "EXTRA_DEVICE_ADDRESS";

    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

}
