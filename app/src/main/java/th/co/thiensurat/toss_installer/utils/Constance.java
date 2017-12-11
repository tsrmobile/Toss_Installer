package th.co.thiensurat.toss_installer.utils;

import android.Manifest;

/**
 * Created by teerayut.k on 10/16/2017.
 */

public class Constance {

    public static String DBNAME = "toss_installer";
    public static String TABLE_JOB = "job";
    public static String TABLE_ADDRESS = "Address";
    public static String TABLE_IMAGE = "image";
    public static String TABLE_PRODUCT = "product";
    public static String TABLE_INSTALL_ITEM = "install_item";
    public static int DB_CURRENT_VERSION = 13;
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

    /*public static final String KEY_JOB_ORDERID = "JOB_ORDERID";
    public static final String KEY_JOB_IDCARD = "JOB_IDCARD";
    public static final String KEY_JOB_TITLE = "JOB_TITLE";
    public static final String KEY_JOB_FIRSTNAME = "JOB_FIRSTNAME";
    public static final String KEY_JOB_LASTNAME = "JOB_LASTNAME";
    public static final String KEY_JOB_CONTACT_PHONE = "JOB_CONTACT_PHONE";
    public static final String KEY_JOB_PRODUCT_CODE = "JOB_PRODUCT_CODE";
    public static final String KEY_JOB_PRODUCT_NAME = "JOB_PRODUCT_NAME";
    public static final String KEY_JOB_PRODUCT_QTY = "JOB_PRODUCT_QTY";
    public static final String KEY_JOB_INSTALLDATESTART = "JOB_INSTALL_DATE_START";
    public static final String KEY_JOB_INSTALLDATEEND = "JOB_INSTALL_DATE_END";

    public static final String KEY_PRODUCT_SERIAL = "KEY_PRODUCT_SERIAL";
    public static final String KEY_INSTALL_IMAGE = "KEY_INSTALL_IMAGE";*/

    public static final String KEY_SORT_ID = "KEY_SORT_ID";
    public static final String KEY_SERIAL_ITEM = "KEY_SERIAL_ITEM";

    public static final String KEY_JOB_ITEM = "KEY_JOB_ITEM";
    public static final String KEY_JOB_ADDR = "KEY_JOB_ADDR";

    public static final String EXTRA_DEVICE_ADDRESS = "EXTRA_DEVICE_ADDRESS";

    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

}
