package th.co.thiensurat.toss_installer.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import th.co.thiensurat.toss_installer.api.request.RequestPayment;
import th.co.thiensurat.toss_installer.api.request.RequestUpdateAddress;
import th.co.thiensurat.toss_installer.api.result.data.DataItem;
import th.co.thiensurat.toss_installer.contract.item.ObjectImage;
import th.co.thiensurat.toss_installer.deposit.item.DepositItem;
import th.co.thiensurat.toss_installer.productwithdraw.item.InstallItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItemGroup;
import th.co.thiensurat.toss_installer.takepicture.item.ImageItem;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.Utils;

/**
 * Created by teerayut.k on 11/13/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private DataItem item;
    private ExDBHelper exDBHelper;
    private SQLiteDatabase sqlite;
    private StringBuilder sb, sb2, sb3, sb4, sb5, sb6, sb7, sb8;

    public static final String JOB_ORDERID = "Orderid";
    public static final String JOB_IDCARD = "IDCard";
    public static final String JOB_TITLE = "Title";
    public static final String JOB_FIRSTNAME = "Firstname";
    public static final String JOB_LASTNAME = "Lastname";
    public static final String JOB_INSTALLSTARTDATE = "Installstartdate";
    public static final String JOB_INSTALLENDDATE = "Installenddate";
    public static final String JOB_INSTALLSTART = "Installstart";
    public static final String JOB_INSTALLEND = "Installend";
    public static final String JOB_STATUS = "Status";
    public static final String JOB_PRESALE = "Presale";
    public static final String JOB_CLOSEDATE = "Closedate";

    public static final String ORDERID = "Orderid";
    public static final String ADDRESTYPECODE = "AddressTypeCode";
    public static final String ADDRESSDETAIL = "AddressDetail";
    public static final String PROVINCENAME = "Province_Name";
    public static final String DISTRICTNAME = "District_Name";
    public static final String SUBDISTRICT = "SubDistrict_Name";
    public static final String ZIPCODE = "Zipcode";
    public static final String PHONE = "Phone";
    public static final String MOBILE = "Mobile";
    public static final String WORK = "Work";
    public static final String EMAIL = "Email";
    public static final String SYNC = "Sync";

    public static final String IMG_ORDER_ID = "order_id";
    public static final String IMG_SERIAL = "img_serial";
    public static final String IMG_PRODUCT_CODE = "img_product_code";
    public static final String IMG_TYPE = "img_type";
    public static final String IMG_URL = "img_url";
    public static final String IMG_SYNC = "img_sync";

    public static final String PRODUCT_ORDER_ID = "orderid";
    public static final String PRODUCT_CODE = "productCode";
    public static final String PRODUCT_NAME = "productName";
    public static final String PRODUCT_MODEL = "productModel";
    public static final String PRODUCT_QTY = "productQty";
    public static final String PRODUCT_ITEM_CODE = "productItemCode";
    public static final String PRODUCT_ITEM_NAME = "productItemName";
    public static final String PRODUCT_ITEM_QTY = "productItemQty";
    public static final String PRODUCT_PRICE = "productPrice";
    public static final String PRODUCT_DISCOUNT = "productDiscount";
    public static final String PRODUCT_DISCOUNT_PERCENT = "productDiscountPercent";
    public static final String PRODUCT_PAYTYPE = "productPaytype";
    public static final String PRODUCT_SERIAL = "productSerial";
    public static final String PRODUCT_STATUS = "productStatus";
    public static final String PRODUCT_CONTACT = "productContact";
    public static final String PRODUCT_INSTALL_DATE = "productInstall";
    public static final String PRODUCT_INSTALL_END = "productInstallEnd";
    public static final String PRODUCT_PRINT_CONTACT = "productPrintContact";
    public static final String PRODUCT_PRINT_INSTALL = "productPrintInstall";
    public static final String PRODUCT_PRERIOD = "productPreriod";
    public static final String PRODUCT_PERPRERIOD = "productPerPreriod";
    public static final String PRODUCT_SYNC = "productSync";

    public static final String STOCK_ID = "stock_id";
    public static final String STOCK_ITEM_SERIAL = "stock_serial";
    public static final String STOCK_ITEM_CODE = "stock_code";
    public static final String STOCK_ITEM_NAME = "stock_name";
    public static final String STOCK_ITEM_DATE = "stock_date";
    public static final String STOCK_ITEM_STATUS = "stock_status";

    public static final String STEP_ID = "step_id";
    public static final String STEP_ORDERID = "step_orderid";
    public static final String STEP_1 = "step_1";
    public static final String STEP_2 = "step_2";
    public static final String STEP_3 = "step_3";
    public static final String STEP_4 = "step_4";
    public static final String STEP_5 = "step_5";
    public static final String STEP_6 = "step_6";
    public static final String STEP_7 = "step_7";
    public static final String STEP_CREATED = "step_create";
    public static final String STEP_UPDATED = "step_update";

    public static final String PAYMENT_ID = "payment_id";
    public static final String PAYMENT_CONTNO = "payment_contno";
    public static final String PAYMENT_PRODUCT_CODE = "payment_product_code";
    public static final String PAYMENT_PERIOD = "payment_period";
    public static final String PAYMENT_METHOD = "payment_method";
    public static final String PAYMENT_TYPE = "payment_type";
    public static final String PAYMENT_DUEDATE = "payment_duedate";
    public static final String PAYMENT_PAYDATE = "payment_paydate";
    public static final String PAYMENT_AMOUNT = "payment_amount";
    public static final String PAYMENT_ACTUAL = "payment_actual";
    public static final String PAYMENT_RECEIPTNO = "payment_receiptno";
    public static final String PAYMENT_DATE = "payment_date";

    public static final String DEPOSIT_ID = "deposit_id";
    public static final String DEPOSIT_CONTNO = "deposit_contno";
    public static final String DEPOSIT_REF1 = "deposit_ref1";
    public static final String DEPOSIT_REF2 = "deposit_ref2";
    public static final String DEPOSIT_CHANNEL = "deposit_channel";
    public static final String DEPOSIT_DATE = "deposit_date";
    public static final String DEPOSIT_AMOUNT = "deposit_amount";
    public static final String DEPOSIT_EMPID = "deposit_empid";
    public static final String DEPOSIT_OFFICER = "deposit_office";

    public DBHelper(Context context) {
        super(context, null, null, 0);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sb = new StringBuilder();
        sb.delete(0, sb.length());
        sb.append("CREATE TABLE " + Constance.TABLE_ADDRESS + " (");
        sb.append(" id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append(" " + ORDERID + " TEXT,");
        sb.append(" " + ADDRESTYPECODE + " TEXT,");
        sb.append(" " + ADDRESSDETAIL + " TEXT,");
        sb.append(" " + PROVINCENAME + " TEXT,");
        sb.append(" " + DISTRICTNAME + " TEXT,");
        sb.append(" " + SUBDISTRICT + " TEXT,");
        sb.append(" " + ZIPCODE + " TEXT,");
        sb.append(" " + PHONE + " TEXT,");
        sb.append(" " + MOBILE + " TEXT,");
        sb.append(" " + WORK + " TEXT,");
        sb.append(" " + EMAIL + " TEXT,");
        sb.append(" " + SYNC + " TEXT)");
        sqLiteDatabase.execSQL(sb.toString());

        sb2 = new StringBuilder();
        sb2.append("CREATE TABLE " + Constance.TABLE_IMAGE + " (");
        sb2.append(" id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb2.append(" " + IMG_ORDER_ID + " TEXT,");
        sb2.append(" " + IMG_SERIAL + " TEXT,");
        sb2.append(" " + IMG_TYPE + " TEXT,");
        sb2.append(" " + IMG_URL + " TEXT,");
        sb2.append(" " + IMG_PRODUCT_CODE + " TEXT,");
        sb2.append(" " + IMG_SYNC + " TEXT)");
        sqLiteDatabase.execSQL(sb2.toString());

        sb3 = new StringBuilder();
        sb3.append("CREATE TABLE " + Constance.TABLE_PRODUCT + " (");
        sb3.append(" id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb3.append(" " + PRODUCT_ORDER_ID + " TEXT,");
        sb3.append(" " + PRODUCT_CODE + " TEXT,");
        sb3.append(" " + PRODUCT_NAME + " TEXT,");
        sb3.append(" " + PRODUCT_MODEL + " TEXT,");
        sb3.append(" " + PRODUCT_QTY + " TEXT,");
        sb3.append(" " + PRODUCT_ITEM_CODE + " TEXT,");
        sb3.append(" " + PRODUCT_ITEM_NAME + " TEXT,");
        sb3.append(" " + PRODUCT_ITEM_QTY + " TEXT,");
        sb3.append(" " + PRODUCT_PRICE + " TEXT,");
        sb3.append(" " + PRODUCT_DISCOUNT + " TEXT,");
        sb3.append(" " + PRODUCT_DISCOUNT_PERCENT + " TEXT,");
        sb3.append(" " + PRODUCT_PAYTYPE + " TEXT,");
        sb3.append(" " + PRODUCT_SERIAL + " TEXT,");
        sb3.append(" " + PRODUCT_STATUS + " TEXT,");
        sb3.append(" " + PRODUCT_CONTACT + " TEXT,");
        sb3.append(" " + PRODUCT_INSTALL_DATE + " TEXT,");
        sb3.append(" " + PRODUCT_INSTALL_END + " TEXT,");
        sb3.append(" " + PRODUCT_PRINT_CONTACT+ " TEXT,");
        sb3.append(" " + PRODUCT_PRINT_INSTALL + " TEXT,");
        sb3.append(" " + PRODUCT_PRERIOD + " TEXT,");
        sb3.append(" " + PRODUCT_PERPRERIOD + " TEXT,");
        sb3.append(" " + PRODUCT_SYNC + " TEXT)");
        sqLiteDatabase.execSQL(sb3.toString());

        sb5 = new StringBuilder();
        sb5.append("CREATE TABLE " + Constance.TABLE_INSTALL_ITEM + " (");
        sb5.append(" id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb5.append(" " + STOCK_ID + " TEXT,");
        sb5.append(" " + STOCK_ITEM_SERIAL + " TEXT,");
        sb5.append(" " + STOCK_ITEM_CODE + " TEXT,");
        sb5.append(" " + STOCK_ITEM_NAME + " TEXT,");
        sb5.append(" " + STOCK_ITEM_DATE + " TEXT,");
        sb5.append(" " + STOCK_ITEM_STATUS + " TEXT)");
        sqLiteDatabase.execSQL(sb5.toString());

        sb4 = new StringBuilder();
        sb4.append("CREATE TABLE " + Constance.TABLE_STEP + " (");
        sb4.append(" " + STEP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb4.append(" " + STEP_ORDERID + " TEXT,");
        sb4.append(" " + STEP_1 + " TEXT,");
        sb4.append(" " + STEP_2 + " TEXT,");
        sb4.append(" " + STEP_3 + " TEXT,");
        sb4.append(" " + STEP_4 + " TEXT,");
        sb4.append(" " + STEP_5 + " TEXT,");
        sb4.append(" " + STEP_6 + " TEXT,");
        sb4.append(" " + STEP_7 + " TEXT,");
        sb4.append(" " + STEP_CREATED + " TEXT,");
        sb4.append(" " + STEP_UPDATED + " TEXT)");
        sqLiteDatabase.execSQL(sb4.toString());

        sb6 = new StringBuilder();
        sb6.append("CREATE TABLE " + Constance.TABLE_JOB + " (");
        sb6.append(" id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb6.append(" " + JOB_ORDERID + " TEXT,");
        sb6.append(" " + JOB_IDCARD + " TEXT,");
        sb6.append(" " + JOB_TITLE + " TEXT,");
        sb6.append(" " + JOB_FIRSTNAME + " TEXT,");
        sb6.append(" " + JOB_LASTNAME + " TEXT,");
        sb6.append(" " + JOB_INSTALLSTARTDATE + " TEXT,");
        sb6.append(" " + JOB_INSTALLENDDATE + " TEXT,");
        sb6.append(" " + JOB_INSTALLSTART + " TEXT,");
        sb6.append(" " + JOB_INSTALLEND + " TEXT,");
        sb6.append(" " + JOB_STATUS + " TEXT,");
        sb6.append(" " + JOB_PRESALE + " TEXT,");
        sb6.append(" " + JOB_CLOSEDATE + " TEXT)");
        sqLiteDatabase.execSQL(sb6.toString());

        sb7 = new StringBuilder();
        sb7.append("CREATE TABLE " + Constance.TABLE_PAYMENT + " (");
        sb7.append(PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb7.append(" " + PAYMENT_CONTNO + " TEXT,");
        sb7.append(" " + PAYMENT_PRODUCT_CODE + " TEXT,");
        sb7.append(" " + PAYMENT_PERIOD + " TEXT,");
        sb7.append(" " + PAYMENT_METHOD + " TEXT,");
        sb7.append(" " + PAYMENT_TYPE + " TEXT,");
        sb7.append(" " + PAYMENT_DUEDATE + " TEXT,");
        sb7.append(" " + PAYMENT_PAYDATE + " TEXT,");
        sb7.append(" " + PAYMENT_AMOUNT + " TEXT,");
        sb7.append(" " + PAYMENT_ACTUAL + " TEXT,");
        sb7.append(" " + PAYMENT_RECEIPTNO + " TEXT,");
        sb7.append(" " + PAYMENT_DATE + " TEXT)");
        sqLiteDatabase.execSQL(sb7.toString());

        sb8 = new StringBuilder();
        sb8.append("CREATE TABLE " + Constance.TABLE_DEPOSIT + " (");
        sb8.append(DEPOSIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb8.append(" " + DEPOSIT_CONTNO + " TEXT,");
        sb8.append(" " + DEPOSIT_CHANNEL + " TEXT,");
        sb8.append(" " + DEPOSIT_AMOUNT + " TEXT,");
        sb8.append(" " + DEPOSIT_DATE + " TEXT,");
        sb8.append(" " + DEPOSIT_REF1 + " TEXT,");
        sb8.append(" " + DEPOSIT_REF2 + " TEXT,");
        sb8.append(" " + DEPOSIT_EMPID + " TEXT,");
        sb8.append(" " + DEPOSIT_OFFICER + " TEXT)");
        sqLiteDatabase.execSQL(sb8.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_ADDRESS_TABLE = "DROP TABLE IF EXISTS " + Constance.TABLE_ADDRESS;
        sqLiteDatabase.execSQL(DROP_ADDRESS_TABLE);

        String DROP_IMAGE_TABLE = "DROP TABLE IF EXISTS " + Constance.TABLE_IMAGE;
        sqLiteDatabase.execSQL(DROP_IMAGE_TABLE);

        String DROP_PRODUCT_TABLE = "DROP TABLE IF EXISTS " + Constance.TABLE_PRODUCT;
        sqLiteDatabase.execSQL(DROP_PRODUCT_TABLE);

        String DROP_ITEM_TABLE = "DROP TABLE IF EXISTS " + Constance.TABLE_INSTALL_ITEM;
        sqLiteDatabase.execSQL(DROP_ITEM_TABLE);

        String DROP_ITEM_STEP = "DROP TABLE IF EXISTS " + Constance.TABLE_STEP;
        sqLiteDatabase.execSQL(DROP_ITEM_STEP);

        String DROP_ITEM_JOB = "DROP TABLE IF EXISTS " + Constance.TABLE_JOB;
        sqLiteDatabase.execSQL(DROP_ITEM_JOB);

        String DROP_ITEM_PAYMENT = "DROP TABLE IF EXISTS " + Constance.TABLE_PAYMENT;
        sqLiteDatabase.execSQL(DROP_ITEM_PAYMENT);

        String DROP_ITEM_DEPOSIT = "DROP TABLE IF EXISTS " + Constance.TABLE_DEPOSIT;
        sqLiteDatabase.execSQL(DROP_ITEM_DEPOSIT);

        onCreate(sqLiteDatabase);
    }

    public void setTableJob(List<JobItem> jobItemList) {
        sqlite = this.getWritableDatabase();
        for (JobItem item : jobItemList) {
            if (!checkItem(Constance.TABLE_JOB, JOB_ORDERID, item.getOrderid())) {
                ContentValues values = new ContentValues();
                values.put(JOB_ORDERID, item.getOrderid());
                values.put(JOB_IDCARD, item.getIDCard());
                values.put(JOB_TITLE, item.getTitle());
                values.put(JOB_FIRSTNAME, item.getFirstName());
                values.put(JOB_LASTNAME, item.getLastName());
                values.put(JOB_INSTALLSTARTDATE, item.getInstallStartDate());
                values.put(JOB_INSTALLENDDATE, item.getInstallEndDate());
                values.put(JOB_INSTALLSTART, (item.getInstallStart().isEmpty()) ? "-" : item.getInstallStart());
                values.put(JOB_INSTALLEND, (item.getInstallEnd().isEmpty()) ? "-" : item.getInstallEnd());
                values.put(JOB_STATUS, item.getStatus());
                values.put(JOB_PRESALE, item.getPresale());
                values.put(JOB_CLOSEDATE, (item.getCloseDate().isEmpty()) ? "-" : item.getCloseDate());
                sqlite.insert(Constance.TABLE_JOB, null, values);
            }

            setTableProductByOrderid(item.getOrderid(), item.getProduct());
            setTableAddressDetial(item.getOrderid(), item.getAddress());
        }


        sqlite.close();
    }

    public List<JobItem> getJob(String status) {
        sqlite = this.getReadableDatabase();
        List<JobItem> jobItemList = new ArrayList<JobItem>();
        JobItem jobItem = null;
        Cursor cursor = sqlite.query (Constance.TABLE_JOB, null,JOB_STATUS + " = ? ",
                new String[] {status},
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String orderid = null;
        while(!cursor.isAfterLast()) {
            orderid = cursor.getString(cursor.getColumnIndex(JOB_ORDERID));
            jobItem = new JobItem()
                    .setOrderid(orderid)
                    .setIDCard(cursor.getString(cursor.getColumnIndex(JOB_IDCARD)))
                    .setTitle(cursor.getString(cursor.getColumnIndex(JOB_TITLE)))
                    .setFirstName(cursor.getString(cursor.getColumnIndex(JOB_FIRSTNAME)))
                    .setLastName(cursor.getString(cursor.getColumnIndex(JOB_LASTNAME)))
                    .setInstallStartDate(cursor.getString(cursor.getColumnIndex(JOB_INSTALLSTARTDATE)))
                    .setInstallEndDate(cursor.getString(cursor.getColumnIndex(JOB_INSTALLENDDATE)))
                    .setInstallStart(cursor.getString(cursor.getColumnIndex(JOB_INSTALLSTART)))
                    .setInstallEnd(cursor.getString(cursor.getColumnIndex(JOB_INSTALLEND)))
                    .setStatus(cursor.getString(cursor.getColumnIndex(JOB_STATUS)))
                    .setPresale(cursor.getString(cursor.getColumnIndex(JOB_PRESALE)))
                    .setCloseDate(cursor.getString(cursor.getColumnIndex(JOB_CLOSEDATE)))
                    .setProduct(getProductByID(orderid).getProduct())
                    .setAddress(getAllAddress(orderid));

            jobItemList.add(jobItem);
            cursor.moveToNext();
        }

        return jobItemList;
    }

    public void setTableProductByOrderid(String orderid, List<ProductItem> productItemList) {
        sqlite = this.getWritableDatabase();
        if (!checkItem(Constance.TABLE_PRODUCT, PRODUCT_ORDER_ID, orderid)) {
            for (int i = 0; i < productItemList.size(); i++) {
                ProductItem productItem = productItemList.get(i);
                if (!productItem.getProductItemQty().equals("0")) {
                    int itemQty = Integer.parseInt(productItem.getProductItemQty());
                    for (int j = 0; j < itemQty; j++) {
                        ContentValues values = new ContentValues();
                        values.put(PRODUCT_ORDER_ID, orderid);
                        values.put(PRODUCT_CODE, productItem.getProductCode());
                        values.put(PRODUCT_NAME, productItem.getProductName());
                        values.put(PRODUCT_MODEL, productItem.getProductModel());
                        values.put(PRODUCT_QTY, productItem.getProductQty());
                        values.put(PRODUCT_ITEM_CODE, productItem.getProductItemCode());
                        values.put(PRODUCT_ITEM_NAME, productItem.getProductItemName());
                        values.put(PRODUCT_ITEM_QTY, String.valueOf(1));
                        values.put(PRODUCT_PRICE, productItem.getProductPrice());
                        values.put(PRODUCT_DISCOUNT, productItem.getProductDiscount());
                        values.put(PRODUCT_DISCOUNT_PERCENT, productItem.getProductDiscountPercent());
                        values.put(PRODUCT_PAYTYPE, productItem.getProductPayType());
                        values.put(PRODUCT_SERIAL, "");
                        values.put(PRODUCT_STATUS, Constance.PRODUCT_STATUS_WAIT);
                        values.put(PRODUCT_CONTACT, "");
                        values.put(PRODUCT_PRINT_CONTACT, "0");
                        values.put(PRODUCT_PRINT_INSTALL, "0");
                        values.put(PRODUCT_PRERIOD, productItem.getProductPayPeriods());
                        values.put(PRODUCT_PERPRERIOD, productItem.getProductPayPerPeriods());
                        sqlite.insert(Constance.TABLE_PRODUCT, null, values);
                    }
                } else if (Integer.parseInt(productItem.getProductQty()) > 1) {
                    for (int k = 0; k < Integer.parseInt(productItem.getProductQty()); k++) {
                        ContentValues values = new ContentValues();
                        values.put(PRODUCT_ORDER_ID, orderid);
                        values.put(PRODUCT_CODE, productItem.getProductCode());
                        values.put(PRODUCT_NAME, productItem.getProductName());
                        values.put(PRODUCT_MODEL, productItem.getProductModel());
                        values.put(PRODUCT_QTY, String.valueOf(1));
                        values.put(PRODUCT_ITEM_CODE, productItem.getProductItemCode());
                        values.put(PRODUCT_ITEM_NAME, productItem.getProductItemName());
                        values.put(PRODUCT_ITEM_QTY, productItem.getProductItemQty());
                        values.put(PRODUCT_PRICE, productItem.getProductPrice());
                        values.put(PRODUCT_DISCOUNT, productItem.getProductDiscount());
                        values.put(PRODUCT_DISCOUNT_PERCENT, productItem.getProductDiscountPercent());
                        values.put(PRODUCT_PAYTYPE, productItem.getProductPayType());
                        values.put(PRODUCT_SERIAL, "");
                        values.put(PRODUCT_STATUS, Constance.PRODUCT_STATUS_WAIT);
                        values.put(PRODUCT_CONTACT, "");
                        values.put(PRODUCT_PRINT_CONTACT, "0");
                        values.put(PRODUCT_PRINT_INSTALL, "0");
                        values.put(PRODUCT_PRERIOD, productItem.getProductPayPeriods());
                        values.put(PRODUCT_PERPRERIOD, productItem.getProductPayPerPeriods());
                        sqlite.insert(Constance.TABLE_PRODUCT, null, values);
                    }
                } else {
                    ContentValues values = new ContentValues();
                    values.put(PRODUCT_ORDER_ID, orderid);
                    values.put(PRODUCT_CODE, productItem.getProductCode());
                    values.put(PRODUCT_NAME, productItem.getProductName());
                    values.put(PRODUCT_MODEL, productItem.getProductModel());
                    values.put(PRODUCT_QTY, productItem.getProductQty());
                    values.put(PRODUCT_ITEM_CODE, productItem.getProductItemCode());
                    values.put(PRODUCT_ITEM_NAME, productItem.getProductItemName());
                    values.put(PRODUCT_ITEM_QTY, productItem.getProductItemQty());
                    values.put(PRODUCT_PRICE, productItem.getProductPrice());
                    values.put(PRODUCT_DISCOUNT, productItem.getProductDiscount());
                    values.put(PRODUCT_DISCOUNT_PERCENT, productItem.getProductDiscountPercent());
                    values.put(PRODUCT_PAYTYPE, productItem.getProductPayType());
                    values.put(PRODUCT_SERIAL, "");
                    values.put(PRODUCT_STATUS, Constance.PRODUCT_STATUS_WAIT);
                    values.put(PRODUCT_CONTACT, "");
                    values.put(PRODUCT_PRINT_CONTACT, "0");
                    values.put(PRODUCT_PRINT_INSTALL, "0");
                    values.put(PRODUCT_PRERIOD, productItem.getProductPayPeriods());
                    values.put(PRODUCT_PERPRERIOD, productItem.getProductPayPerPeriods());
                    sqlite.insert(Constance.TABLE_PRODUCT, null, values);
                }
            }
        }
        sqlite.close();
    }

    public boolean checkItem(String tablename, String colwhere, String orderid) {
        boolean collect = false;
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query (tablename, null, colwhere + " = ? ",
                        new String[] { orderid },
                        null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                collect = true;
            }
        }
        return collect;
    }

    /*public boolean checkItemWithAND(String tablename, String colwhere, String colAnd, String orderid, String valueAND) {
        boolean collect = false;
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query (tablename, null, colwhere + " = ? AND " + colAnd + " = ?",
                new String[] { orderid, valueAND },
                null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                collect = true;
            }
        }
        return collect;
    }*/

    public ProductItemGroup getProductByID(String orderid) {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_PRODUCT, null, PRODUCT_ORDER_ID + " = ?",
                        new String[]{orderid},
                        null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        List<ProductItem> productItemList = new ArrayList<ProductItem>();
        while(!cursor.isAfterLast()) {
            ProductItem productItem = new ProductItem()
                    .setProductID(cursor.getString(0))
                    .setProductCode(cursor.getString(cursor.getColumnIndex(PRODUCT_CODE)))
                    .setProductName(cursor.getString(cursor.getColumnIndex(PRODUCT_NAME)))
                    .setProductModel(cursor.getString(cursor.getColumnIndex(PRODUCT_MODEL)))
                    .setProductQty(cursor.getString(cursor.getColumnIndex(PRODUCT_QTY)))
                    .setProductItemCode(cursor.getString(cursor.getColumnIndex(PRODUCT_ITEM_CODE)))
                    .setProductItemName(cursor.getString(cursor.getColumnIndex(PRODUCT_ITEM_NAME)))
                    .setProductItemQty(cursor.getString(cursor.getColumnIndex(PRODUCT_ITEM_QTY)))
                    .setProductPrice(cursor.getString(cursor.getColumnIndex(PRODUCT_PRICE)))
                    .setProductDiscount(cursor.getString(cursor.getColumnIndex(PRODUCT_DISCOUNT)))
                    .setProductDiscountPercent(cursor.getString(cursor.getColumnIndex(PRODUCT_DISCOUNT_PERCENT)))
                    .setProductPayType(cursor.getString(cursor.getColumnIndex(PRODUCT_PAYTYPE)))
                    .setProductSerial(cursor.getString(cursor.getColumnIndex(PRODUCT_SERIAL)))
                    .setProductStatus(cursor.getString(cursor.getColumnIndex(PRODUCT_STATUS)))
                    .setProductPrintContact(cursor.getString(cursor.getColumnIndex(PRODUCT_PRINT_CONTACT)))
                    .setProductPrintInstall(cursor.getString(cursor.getColumnIndex(PRODUCT_PRINT_INSTALL)))
                    .setProductPayPeriods(cursor.getString(cursor.getColumnIndex(PRODUCT_PRERIOD)))
                    .setProductPayPerPeriods(cursor.getString(cursor.getColumnIndex(PRODUCT_PERPRERIOD)));
            productItemList.add(productItem);
            cursor.moveToNext();
        }
        ProductItemGroup productItemGroup = new ProductItemGroup();
        productItemGroup.setProduct(productItemList);
        sqlite.close();
        return productItemGroup;
    }

    public boolean getProductNotInstall(String orderid) {
        boolean installed = false;
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_PRODUCT, null, PRODUCT_ORDER_ID + " = ? AND " + PRODUCT_STATUS + " = ?",
                        new String[] { orderid, Constance.PRODUCT_STATUS_WAIT },
                        null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                installed = true;
            }
            cursor.close();
        }
        return installed;
    }

    public void updateSerialToTableProduct(String id, String serial) {
        sqlite = this.getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
        String currentDate = sdf.format(new Date());
        ContentValues values = new ContentValues();
        values.put(PRODUCT_SERIAL, serial);
        values.put(PRODUCT_STATUS, Constance.PRODUCT_STATUS_READY);
        values.put(PRODUCT_INSTALL_DATE, currentDate);
        values.put(PRODUCT_INSTALL_END, currentDate);
        long success = sqlite.update(Constance.TABLE_PRODUCT, values, "id = ?", new String[]{id});
        if (success > 0) {
            updateItemInstalled(serial);
        }
        sqlite.close();
    }

    public void updateContactNumberToProduct(String orderid, String contact) {
        sqlite = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRODUCT_CONTACT, contact);
        sqlite.update(Constance.TABLE_PRODUCT, values, PRODUCT_ORDER_ID + " = ?", new String[]{orderid});

        sqlite.close();
    }

    public void updateItemInstalled(String serial) {
        sqlite = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STOCK_ITEM_STATUS, "0");
        long query = sqlite.update(Constance.TABLE_INSTALL_ITEM, values, STOCK_ITEM_SERIAL + " = ?", new String[]{ serial });
        if (query > 0) {
            sqlite.close();
        }
    }

    public void updatePrintStatus(String orderid, String print) {
        sqlite = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(print, 1);
        sqlite.update(Constance.TABLE_PRODUCT, values, PRODUCT_ORDER_ID + " = ?", new String[]{orderid});
        sqlite.close();
    }

    public void setTableAddressDetial(String orderid, List<AddressItem> addressItemList) {
        sqlite = this.getWritableDatabase();
        if (!checkItem(Constance.TABLE_ADDRESS, ORDERID, orderid)) {
            for (AddressItem addressItem : addressItemList) {
                ContentValues values = new ContentValues();
                values.put(ORDERID, orderid);
                values.put(ADDRESTYPECODE, addressItem.getAddressType());
                values.put(ADDRESSDETAIL, addressItem.getAddrDetail());
                values.put(PROVINCENAME, addressItem.getProvince());
                values.put(DISTRICTNAME, addressItem.getDistrict());
                values.put(SUBDISTRICT, addressItem.getSubdistrict());
                values.put(ZIPCODE, addressItem.getZipcode());
                values.put(PHONE, addressItem.getPhone());
                values.put(MOBILE, addressItem.getMobile());
                values.put(WORK, addressItem.getOffice());
                values.put(EMAIL, addressItem.getEmail());
                values.put(SYNC, "-");
                sqlite.insert(Constance.TABLE_ADDRESS, null, values);
            }
        }
        sqlite.close();
    }

    public List<AddressItem> getAllAddress(String orderid) {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_ADDRESS, null, ORDERID + " = ? ", new String[] { orderid },
                        null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        List<AddressItem> addressItemList = new ArrayList<AddressItem>();
        while(!cursor.isAfterLast()) {
            AddressItem addressItem = new AddressItem()
                    .setAddressType(cursor.getString(2))
                    .setAddrDetail(cursor.getString(3))
                    .setProvince(cursor.getString(4))
                    .setDistrict(cursor.getString(5))
                    .setSubdistrict(cursor.getString(6))
                    .setZipcode(cursor.getString(7))
                    .setPhone(cursor.getString(8))
                    .setMobile(cursor.getString(9))
                    .setOffice(cursor.getString(10))
                    .setEmail(cursor.getString(11));

            addressItemList.add(addressItem);
            cursor.moveToNext();
        }

        sqlite.close();
        return addressItemList;
    }

    public List<RequestUpdateAddress.updateBody> getAddressNotSync(Context context) {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_ADDRESS, null, SYNC + " = 0", null,
                        null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        List<RequestUpdateAddress.updateBody> updateBodyList = new ArrayList<>();
        while(!cursor.isAfterLast()) {
            updateBodyList.add(new RequestUpdateAddress.updateBody()
                    .setOrderid(cursor.getString(cursor.getColumnIndex(ORDERID)))
                    .setAddressType(cursor.getString(cursor.getColumnIndex(ADDRESTYPECODE)))
                    .setAddrDetail(cursor.getString(cursor.getColumnIndex(ADDRESSDETAIL)))
                    .setProvince(String.valueOf(getId(context, "province", cursor.getString(cursor.getColumnIndex(PROVINCENAME)))))
                    .setDistrict(String.valueOf(getId(context, "district", cursor.getString(cursor.getColumnIndex(DISTRICTNAME)))))
                    .setSubdistrict(String.valueOf(getId(context, "subdistrict", cursor.getString(cursor.getColumnIndex(SUBDISTRICT)))))
                    .setZipcode(cursor.getString(cursor.getColumnIndex(ZIPCODE)))
                    .setPhone(cursor.getString(cursor.getColumnIndex(PHONE)))
                    .setOffice(cursor.getString(cursor.getColumnIndex(WORK)))
                    .setMobile(cursor.getString(cursor.getColumnIndex(MOBILE)))
                    .setEmail(cursor.getString(cursor.getColumnIndex(EMAIL))));
            cursor.moveToNext();
        }

        sqlite.close();
        return updateBodyList;
    }

    private int getId(Context context, String tableName, String name) {
        String tbName = "";
        String colName = "";
        if (tableName.equals("province")) {
            tbName = "Province";
            colName = "PROVINCE_ID";
        } else if (tableName.equals("district")) {
            tbName = "Amphur";
            colName = "AMPHUR_ID";
        } else if (tableName.equals("subdistrict")) {
            tbName = "District";
            colName = "DISTRICT_ID";
        }

        exDBHelper = new ExDBHelper(context);
        try {
            exDBHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exDBHelper.getId(tbName, colName, name);
    }

    public boolean updateAddress(String orderid, String type, List<AddressItem> addressItemList) {
        long success = 0;
        sqlite = this.getWritableDatabase();
        for (AddressItem item : addressItemList) {
            if (type.equals("AddressIDCard")) {
                ContentValues values = new ContentValues();
                values.put(ADDRESSDETAIL, item.getAddrDetail());
                values.put(PROVINCENAME, item.getProvince());
                values.put(DISTRICTNAME, item.getDistrict());
                values.put(SUBDISTRICT, item.getSubdistrict());
                values.put(ZIPCODE, item.getZipcode());
                values.put(PHONE, item.getPhone());
                values.put(MOBILE, item.getMobile());
                values.put(WORK, item.getOffice());
                values.put(EMAIL, item.getEmail());
                values.put(SYNC, "0");
                success = sqlite.update(Constance.TABLE_ADDRESS, values, ORDERID + " = ? AND " + ADDRESTYPECODE + " = ?"
                        , new String[]{orderid, type});
            } else if (type.equals("AddressInstall")) {
                ContentValues values = new ContentValues();
                values.put(ADDRESSDETAIL, item.getAddrDetail());
                values.put(PROVINCENAME, item.getProvince());
                values.put(DISTRICTNAME, item.getDistrict());
                values.put(SUBDISTRICT, item.getSubdistrict());
                values.put(ZIPCODE, item.getZipcode());
                values.put(PHONE, item.getPhone());
                values.put(MOBILE, item.getMobile());
                values.put(WORK, item.getOffice());
                values.put(EMAIL, item.getEmail());
                values.put(SYNC, "0");
                success = sqlite.update(Constance.TABLE_ADDRESS, values, ORDERID + " = ? AND " + ADDRESTYPECODE + " = ?"
                        , new String[]{orderid, type});
            } else if (type.equals("AddressPayment")) {
                ContentValues values = new ContentValues();
                values.put(ADDRESSDETAIL, item.getAddrDetail());
                values.put(PROVINCENAME, item.getProvince());
                values.put(DISTRICTNAME, item.getDistrict());
                values.put(SUBDISTRICT, item.getSubdistrict());
                values.put(ZIPCODE, item.getZipcode());
                values.put(PHONE, item.getPhone());
                values.put(MOBILE, item.getMobile());
                values.put(WORK, item.getOffice());
                values.put(EMAIL, item.getEmail());
                values.put(SYNC, "0");
                success = sqlite.update(Constance.TABLE_ADDRESS, values, ORDERID + " = ? AND " + ADDRESTYPECODE + " = ?"
                        , new String[]{orderid, type});
            }
        }

        if (success > 0) {
            sqlite.close();
            return true;
        } else {
            sqlite.close();
            return false;
        }
    }

    public void updateAddressSync(String orderid) {
        sqlite = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SYNC, "1");
        sqlite.update(Constance.TABLE_ADDRESS, values, ORDERID + " = ?", new String[]{orderid});
        sqlite.close();
    }

    public void addImage(String orderid, String serial, String type, String url, String productcode) {
        sqlite = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IMG_ORDER_ID, orderid);
        values.put(IMG_SERIAL, serial);
        values.put(IMG_TYPE, type);
        values.put(IMG_URL, url);
        values.put(IMG_PRODUCT_CODE, productcode);
        sqlite.insert(Constance.TABLE_IMAGE, null, values);
        sqlite.close();
    }

    public void editImage(String id, String url) {
        sqlite = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IMG_URL, url);
        sqlite.update(Constance.TABLE_IMAGE, values, "id = ?", new String[]{id});
        sqlite.close();
    }

    public void deleteImage(String id) {
        sqlite = this.getWritableDatabase();
        sqlite.delete(Constance.TABLE_IMAGE, "id = ?",new String[]{id});
        sqlite.close();
    }

    public List<ImageItem> getImage(String orderid, String type) {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_IMAGE, null, IMG_ORDER_ID + " = ? AND " + IMG_TYPE + " = ?",
                        new String[] { orderid, type },
                        null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        List<ImageItem> imageItemList = new ArrayList<ImageItem>();
        while(!cursor.isAfterLast()) {
            ImageItem imageItem = new ImageItem()
                    .setImageId(cursor.getString(0))
                    .setImageOrderId(cursor.getString(1))
                    .setImageSerial(cursor.getString(2))
                    .setImageType(cursor.getString(3))
                    .setImageUrl(cursor.getString(4));
            imageItemList.add(imageItem);
            cursor.moveToNext();
        }
        return imageItemList;
    }

    public List<ObjectImage> getAllImageURI(String orderid) {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_IMAGE, null, IMG_ORDER_ID + " = ? ",
                        new String[] { orderid},
                        null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        List<ObjectImage> fileUris = new ArrayList<ObjectImage>();

        while (!cursor.isAfterLast()) {
            ObjectImage image = new ObjectImage()
                    .setType(cursor.getString(cursor.getColumnIndex(IMG_TYPE)))
                    .setImageName(cursor.getString(cursor.getColumnIndex(IMG_URL)))
                    .setProductCode(cursor.getString(cursor.getColumnIndex(IMG_PRODUCT_CODE)));

            fileUris.add(image);
            cursor.moveToNext();
        }

        return fileUris;
    }

    public String getInstallDate(String orderid) {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_PRODUCT, null, PRODUCT_ORDER_ID + " = ?",
                        new String[] { orderid },
                        null, null, PRODUCT_INSTALL_DATE + " ASC");

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            return cursor.getString(cursor.getColumnIndex(PRODUCT_INSTALL_DATE));
        }

        return null;
    }

    public String getInstallEnd(String orderid) {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_PRODUCT, null, PRODUCT_ORDER_ID + " = ?",
                        new String[] { orderid },
                        null, null, PRODUCT_INSTALL_DATE + " DESC");

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            return cursor.getString(cursor.getColumnIndex(PRODUCT_INSTALL_END));
        }

        return null;
    }

    public void setTableItem(List<InstallItem> installItemList) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());
        sqlite = this.getWritableDatabase();
        int i = 0;
        for (InstallItem item : installItemList) {
            ContentValues values = new ContentValues();
            values.put(STOCK_ID, item.getPrintTakeStockID());
            values.put(STOCK_ITEM_SERIAL, item.getProduct_SerialNum());
            values.put(STOCK_ITEM_CODE, item.getProduct_Code());
            values.put(STOCK_ITEM_NAME, item.getProduct_Name());
            values.put(STOCK_ITEM_DATE, item.getRef_Date());
            values.put(STOCK_ITEM_STATUS, "1");
            sqlite.insert(Constance.TABLE_INSTALL_ITEM, null, values);
            Log.e("count", String.valueOf(i++));
        }
        sqlite.close();
    }

    public List<InstallItem> getItemBalance() {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_INSTALL_ITEM, null, STOCK_ITEM_STATUS + " = 1",
                       null,
                        null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        List<InstallItem> installItemList = new ArrayList<InstallItem>();
        while(!cursor.isAfterLast()) {
            InstallItem item = new InstallItem()
                    .setPrintTakeStockID(cursor.getString(cursor.getColumnIndex(STOCK_ID)))
                    .setProduct_SerialNum(cursor.getString(cursor.getColumnIndex(STOCK_ITEM_SERIAL)))
                    .setProduct_Code(cursor.getString(cursor.getColumnIndex(STOCK_ITEM_CODE)))
                    .setProduct_Name(cursor.getString(cursor.getColumnIndex(STOCK_ITEM_NAME)))
                    .setAStockStatus(cursor.getString(cursor.getColumnIndex(STOCK_ITEM_STATUS)));

            installItemList.add(item);
            cursor.moveToNext();
        }

        return installItemList;
    }

    public boolean checkItemSerial(String serial, String productcode) {
        sqlite = this.getReadableDatabase();
        boolean collect = false;
        Cursor cursor = sqlite.query(Constance.TABLE_INSTALL_ITEM, null,
                        STOCK_ITEM_SERIAL + " = ? AND " + STOCK_ITEM_CODE + " = ? AND " + STOCK_ITEM_STATUS + " = '1'",
                        new String[] { serial, productcode },
                        null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            collect = true;
        }

        cursor.close();
        sqlite.close();
        return collect;
    }

    public boolean checkNumber(String number) {
        boolean collect = false;
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_INSTALL_ITEM, null,STOCK_ID + " = ?",
                        new String[] { number },
                        null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            collect = true;
        }

        cursor.close();
        sqlite.close();
        return collect;
    }

    public String getPayType(String orderid, String productCode, String serial) {
        String payType = "";
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_PRODUCT, null,
                        PRODUCT_ORDER_ID + " = ? AND " + PRODUCT_CODE + " = ? AND " + PRODUCT_SERIAL + " = ?",
                        new String[] { orderid, productCode, serial },
                        null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            payType = cursor.getString(cursor.getColumnIndex(PRODUCT_PAYTYPE));
        }

        Log.e("pay type", payType);
        cursor.close();
        sqlite.close();
        return payType;
    }

    public boolean checkItemExisting() {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_INSTALL_ITEM, null, null, null,
                        null, null, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean isTableExists(String tableName) {
        boolean isExist = false;
        sqlite = this.getWritableDatabase();
        Cursor cursor = sqlite.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                isExist = true;
            }
            cursor.close();
        }
        return isExist;
    }

    public void emptyTable(String tablename) {
        sqlite = this.getWritableDatabase();
        sqlite.delete(tablename, null,null);
        sqlite.close();
    }

    public void setTableStep(String orderid) {
        if (!checkStepCreate(orderid)) {
            sqlite = this.getWritableDatabase();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
            String currentDate = sdf.format(new Date());
            ContentValues values = new ContentValues();
            values.put(STEP_ORDERID, orderid);
            values.put(STEP_1, 1);
            values.put(STEP_2, 0);
            values.put(STEP_3, 0);
            values.put(STEP_4, 0);
            values.put(STEP_5, 0);
            values.put(STEP_6, 0);
            values.put(STEP_7, 0);
            values.put(STEP_CREATED, currentDate);
            sqlite.insert(Constance.TABLE_STEP, null, values);
            sqlite.close();
        }
    }

    public void updateStep(String orderid, String step) {
        sqlite = this.getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
        String currentDate = sdf.format(new Date());
        ContentValues values = new ContentValues();
        values.put(STEP_ORDERID, orderid);
        values.put(step, 1);
        values.put(STEP_UPDATED, currentDate);
        sqlite.update(Constance.TABLE_STEP, values, STEP_ORDERID + " = ?", new String[]{orderid});
        sqlite.close();
    }

    public boolean checkStepCreate(String orderid) {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_STEP, null, STEP_ORDERID + " = ?",
                        new String[] { orderid }, null, null, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public List<String> getAllStep(String orderid) {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_STEP, null, STEP_ORDERID + " = ?",
                        new String[] { orderid }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        List<String> stringList = new ArrayList<String>();
        while (!cursor.isAfterLast()) {
            stringList.add((cursor.getString(cursor.getColumnIndex(STEP_1)).isEmpty()) ? "0" : cursor.getString(cursor.getColumnIndex(STEP_1)));
            stringList.add((cursor.getString(cursor.getColumnIndex(STEP_2)).isEmpty()) ? "0" : cursor.getString(cursor.getColumnIndex(STEP_2)));
            stringList.add((cursor.getString(cursor.getColumnIndex(STEP_3)).isEmpty()) ? "0" : cursor.getString(cursor.getColumnIndex(STEP_3)));
            stringList.add((cursor.getString(cursor.getColumnIndex(STEP_4)).isEmpty()) ? "0" : cursor.getString(cursor.getColumnIndex(STEP_4)));
            stringList.add((cursor.getString(cursor.getColumnIndex(STEP_5)).isEmpty()) ? "0" : cursor.getString(cursor.getColumnIndex(STEP_5)));
            stringList.add((cursor.getString(cursor.getColumnIndex(STEP_6)).isEmpty()) ? "0" : cursor.getString(cursor.getColumnIndex(STEP_6)));
            stringList.add((cursor.getString(cursor.getColumnIndex(STEP_7)).isEmpty()) ? "0" : cursor.getString(cursor.getColumnIndex(STEP_7)));
            cursor.moveToNext();
        }
        return stringList;
    }

    public void setTablePayment(List<RequestPayment.paymentBody> paymentBodies) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sqlite = this.getWritableDatabase();
        for (RequestPayment.paymentBody body : paymentBodies) {
            ContentValues values = new ContentValues();
            values.put(PAYMENT_CONTNO, body.getContno());
            values.put(PAYMENT_PRODUCT_CODE, body.getProductcode());
            values.put(PAYMENT_PERIOD, body.getPeriod());
            values.put(PAYMENT_METHOD, body.getPaymenttype());
            values.put(PAYMENT_TYPE, body.getPaymentreceive());
            values.put(PAYMENT_DUEDATE, body.getDuedate());
            values.put(PAYMENT_PAYDATE, Utils.ConvertDateFormatDB(body.getPaydate()));
            values.put(PAYMENT_AMOUNT, body.getAmount());
            values.put(PAYMENT_ACTUAL, body.getActual());
            values.put(PAYMENT_RECEIPTNO, body.getReceiptno());
            values.put(PAYMENT_DATE, body.getReceiptdate());

            sqlite.insert(Constance.TABLE_PAYMENT, null, values);
        }
        sqlite.close();
    }

    public List<DepositItem> getAllPayment() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_PAYMENT, null,  PAYMENT_METHOD + " = 1",
                        null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        List<DepositItem> depositItemList = new ArrayList<DepositItem>();
        while (!cursor.isAfterLast()) {
            depositItemList.add(new DepositItem()
                    .setId(cursor.getString(cursor.getColumnIndex(PAYMENT_ID)))
                    .setContno(cursor.getString(cursor.getColumnIndex(PAYMENT_CONTNO)))
                    .setDate(cursor.getString(cursor.getColumnIndex(PAYMENT_PAYDATE)))
                    .setAmount(cursor.getString(cursor.getColumnIndex(PAYMENT_ACTUAL)))
            );
            cursor.moveToNext();
        }

        return depositItemList;
    }

    public boolean getPaymentStatus(String code) {
        boolean collect = false;
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_PAYMENT, null, PAYMENT_PRODUCT_CODE + " = ?",
                        new String[] { code }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            collect = true;
        }

        cursor.close();
        sqlite.close();
        return collect;
    }

    public String getDepositRef2() {
        String ref2 = "";
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_DEPOSIT, null, null,
                        null, null, null, DEPOSIT_REF2 + " DESC");
        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            ref2 = cursor.getString(cursor.getColumnIndex(DEPOSIT_REF2));
        }

        return ref2;
    }
}
