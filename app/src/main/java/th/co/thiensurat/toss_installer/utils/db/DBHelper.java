package th.co.thiensurat.toss_installer.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import th.co.thiensurat.toss_installer.api.result.JobFinishItem;
import th.co.thiensurat.toss_installer.api.result.JobImageItem;
import th.co.thiensurat.toss_installer.api.result.data.DataItem;
import th.co.thiensurat.toss_installer.api.result.data.DataResult;
import th.co.thiensurat.toss_installer.contract.item.ImageSuccessItem;
import th.co.thiensurat.toss_installer.contract.item.JobSuccessItem;
import th.co.thiensurat.toss_installer.contract.item.ProductSuccessItem;
import th.co.thiensurat.toss_installer.itemlist.item.InstallItem;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.job.item.JobItemGroup;
import th.co.thiensurat.toss_installer.job.item.ProductItem;
import th.co.thiensurat.toss_installer.job.item.ProductItemGroup;
import th.co.thiensurat.toss_installer.takepicture.item.ImageItem;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.MyApplication;

/**
 * Created by teerayut.k on 11/13/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private DataItem item;
    private SQLiteDatabase sqlite;
    private StringBuilder sb, sb2, sb3, sb4, sb5;

    public static final String JOBORDERID = "joborderid";
    public static final String IDCARD = "idcard";
    public static final String TITLE = "title";
    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "lastname";
    public static final String CONTACTPHONE = "contactname";
    public static final String INSTALLSTARTDATE = "installstartdate";
    public static final String INSTALLENDDATE = "installenddate";
    public static final String JOB_PROUCT_CODE = "productcode";
    public static final String JOB_PRODUCT_NAME = "productname";
    public static final String JOB_PRODUCT_QTY = "productqty";
    public static final String INSTALLSTART = "installstart";
    public static final String INSTALLEND = "installend";
    public static final String CANCELDATE = "canceldate";
    public static final String CANCELNOTE = "cancelnote";
    public static final String STATUS = "status";
    public static final String PRESALE = "presale";
    public static final String JOB_CREATED = "created";
    public static final String JOB_UPDATED = "updated";
    public static final String JOB_CONTNO = "contno";

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

    public static final String IMG_ORDER_ID = "order_id";
    public static final String IMG_SERIAL = "img_serial";
    public static final String IMG_PRODUCT_CODE = "img_product_code";
    public static final String IMG_TYPE = "img_type";
    public static final String IMG_URL = "img_url";
    public static final String IMG_STATUS = "img_status";

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

    public static final String STOCK_ID = "stock_id";
    public static final String STOCK_ITEM_SERIAL = "stock_serial";
    public static final String STOCK_ITEM_CODE = "stock_code";
    public static final String STOCK_ITEM_NAME = "stock_name";
    public static final String STOCK_ITEM_DATE = "stock_date";
    public static final String STOCK_ITEM_INSTALL_DATE = "stock_install_date";
    public static final String STOCK_ITEM_STATUS = "stock_status";

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
        sb.append(" " + EMAIL + " TEXT)");
        String CREATE_TABLE_ADDRESS = sb.toString();
        sqLiteDatabase.execSQL(CREATE_TABLE_ADDRESS);

        sb2 = new StringBuilder();
        sb2.append("CREATE TABLE " + Constance.TABLE_IMAGE + " (");
        sb2.append(" id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb2.append(" " + IMG_ORDER_ID + " TEXT,");
        sb2.append(" " + IMG_SERIAL + " TEXT,");
        sb2.append(" " + IMG_TYPE + " TEXT,");
        sb2.append(" " + IMG_URL + " TEXT,");
        sb2.append(" " + IMG_STATUS + " TEXT,");
        sb2.append(" " + IMG_PRODUCT_CODE + " TEXT)");
        String CREATE_TABLE_IMAGE = sb2.toString();
        sqLiteDatabase.execSQL(CREATE_TABLE_IMAGE);

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
        sb3.append(" " + PRODUCT_CONTACT + " TEXT)");
        String CREATE_TABLE_PRODUCT = sb3.toString();
        sqLiteDatabase.execSQL(CREATE_TABLE_PRODUCT);

        sb4 = new StringBuilder();
        sb4.append("CREATE TABLE " + Constance.TABLE_JOB + " (");
        sb4.append(" id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb4.append(" " + JOBORDERID + " TEXT,");
        sb4.append(" " + IDCARD + " TEXT,");
        sb4.append(" " + TITLE + " TEXT,");
        sb4.append(" " + FIRSTNAME + " TEXT,");
        sb4.append(" " + LASTNAME + " TEXT,");
        sb4.append(" " + CONTACTPHONE + " TEXT,");
        sb4.append(" " + INSTALLSTARTDATE + " TEXT,");
        sb4.append(" " + INSTALLENDDATE + " TEXT,");
        sb4.append(" " + JOB_PROUCT_CODE + " TEXT,");
        sb4.append(" " + JOB_PRODUCT_NAME + " TEXT,");
        sb4.append(" " + JOB_PRODUCT_QTY + " TEXT,");
        sb4.append(" " + INSTALLSTART + " TEXT,");
        sb4.append(" " + INSTALLEND + " TEXT,");
        sb4.append(" " + CANCELNOTE + " TEXT,");
        sb4.append(" " + CANCELDATE + " TEXT,");
        sb4.append(" " + JOB_CREATED + " TEXT,");
        sb4.append(" " + JOB_UPDATED + " TEXT,");
        sb4.append(" " + STATUS + " TEXT,");
        sb4.append(" " + PRESALE + " TEXT,");
        sb4.append(" " + JOB_CONTNO + " TEXT)");
        String CREATE_TABLE_JOB = sb4.toString();
        sqLiteDatabase.execSQL(CREATE_TABLE_JOB);

        sb5 = new StringBuilder();
        sb5.append("CREATE TABLE " + Constance.TABLE_INSTALL_ITEM + " (");
        sb5.append(" id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb5.append(" " + STOCK_ID + " TEXT,");
        sb5.append(" " + STOCK_ITEM_SERIAL + " TEXT,");
        sb5.append(" " + STOCK_ITEM_CODE + " TEXT,");
        sb5.append(" " + STOCK_ITEM_NAME + " TEXT,");
        sb5.append(" " + STOCK_ITEM_DATE + " TEXT,");
        sb5.append(" " + STOCK_ITEM_INSTALL_DATE + " TEXT,");
        sb5.append(" " + STOCK_ITEM_STATUS + " TEXT)");
        String CREATE_TABLE_ITEM = sb5.toString();
        sqLiteDatabase.execSQL(CREATE_TABLE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_ADDRESS_TABLE = "DROP TABLE IF EXISTS " + Constance.TABLE_ADDRESS;
        sqLiteDatabase.execSQL(DROP_ADDRESS_TABLE);

        String DROP_IMAGE_TABLE = "DROP TABLE IF EXISTS " + Constance.TABLE_IMAGE;
        sqLiteDatabase.execSQL(DROP_IMAGE_TABLE);

        String DROP_PRODUCT_TABLE = "DROP TABLE IF EXISTS " + Constance.TABLE_PRODUCT;
        sqLiteDatabase.execSQL(DROP_PRODUCT_TABLE);

        String DROP_JOB_TABLE = "DROP TABLE IF EXISTS " + Constance.TABLE_JOB;
        sqLiteDatabase.execSQL(DROP_JOB_TABLE);

        String DROP_ITEM_TABLE = "DROP TABLE IF EXISTS " + Constance.TABLE_INSTALL_ITEM;
        sqLiteDatabase.execSQL(DROP_ITEM_TABLE);

        onCreate(sqLiteDatabase);
    }

    public void setTableJob(List<JobItem> jobItemList) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        String currentDate = sdf.format(new Date());
        sqlite = this.getWritableDatabase();
        for (int i = 0; i < jobItemList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(JOBORDERID, jobItemList.get(i).getOrderid());
            values.put(IDCARD, jobItemList.get(i).getIDCard());
            values.put(TITLE, jobItemList.get(i).getTitle());
            values.put(FIRSTNAME, jobItemList.get(i).getFirstName());
            values.put(LASTNAME, jobItemList.get(i).getLastName());
            values.put(CONTACTPHONE, jobItemList.get(i).getContactphone());
            values.put(INSTALLSTARTDATE, jobItemList.get(i).getInstallStartDate());
            values.put(INSTALLENDDATE, jobItemList.get(i).getInstallEndDate());
            values.put(INSTALLSTART, jobItemList.get(i).getInstallStart());
            values.put(INSTALLEND, jobItemList.get(i).getInstallEnd());

            for (int j = 0; j < jobItemList.get(i).getProduct().size(); j++) {
                ProductItem item = jobItemList.get(i).getProduct().get(j);
                values.put(JOB_PROUCT_CODE, item.getProductCode());
                values.put(JOB_PRODUCT_NAME, item.getProductName());
                values.put(JOB_PRODUCT_QTY, item.getProductQty());
            }

            values.put(INSTALLSTART, "");
            values.put(INSTALLEND, "");
            values.put(CANCELNOTE, "");
            values.put(CANCELDATE, "");
            values.put(STATUS, jobItemList.get(i).getStatus());
            values.put(PRESALE, jobItemList.get(i).getPresale());
            values.put(JOB_CONTNO, jobItemList.get(i).getContno());
            values.put(JOB_CREATED, currentDate);
            sqlite.insert(Constance.TABLE_JOB, null, values);
        }
    }

    public JobItemGroup getJobList(String date) {
        sqlite = this.getReadableDatabase();
        date = date + "%";
        Cursor cursor = sqlite.query
                (Constance.TABLE_JOB, null, INSTALLSTARTDATE + " LIKE ? AND " + STATUS + " = 21",
                        new String[] {date},
                        null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        JobItemGroup jobItemGroup = new JobItemGroup();
        jobItemGroup.setStatus("SUCCESS");
        jobItemGroup.setMessage("SUCCESS");
        List<JobItem> jobItemList = new ArrayList<JobItem>();
        List<AddressItem> addressItemList = null;
        List<ProductItem> productItemList = null;

        while(!cursor.isAfterLast()) {
            JobItem jobItem = new JobItem();
            jobItem.setOrderid(cursor.getString(cursor.getColumnIndex(JOBORDERID)).trim());
            jobItem.setIDCard(cursor.getString(cursor.getColumnIndex(IDCARD)).trim());
            jobItem.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)).trim());
            jobItem.setFirstName(cursor.getString(cursor.getColumnIndex(FIRSTNAME)).trim());
            jobItem.setLastName(cursor.getString(cursor.getColumnIndex(LASTNAME)).trim());
            jobItem.setInstallStartDate(cursor.getString(cursor.getColumnIndex(INSTALLSTARTDATE)).trim());
            jobItem.setInstallEndDate(cursor.getString(cursor.getColumnIndex(INSTALLENDDATE)).trim());
            jobItem.setInstallStart(cursor.getString(cursor.getColumnIndex(INSTALLSTART)));
            jobItem.setInstallEnd(cursor.getString(cursor.getColumnIndex(INSTALLEND)));
            jobItem.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            jobItem.setPresale(cursor.getString(cursor.getColumnIndex(PRESALE)));
            jobItem.setContno(cursor.getString(cursor.getColumnIndex(JOB_CONTNO)));

            addressItemList = new ArrayList<AddressItem>();
            addressItemList = getAllAddress(cursor.getString(1).trim());
            jobItem.setAddress(addressItemList);

            productItemList = new ArrayList<ProductItem>();
            ProductItem productItem = new ProductItem();
            productItem.setProductCode(cursor.getString(9).trim());
            productItem.setProductName(cursor.getString(10).trim());
            productItem.setProductQty(cursor.getString(11).trim());
            productItemList.add(productItem);
            jobItem.setProduct(productItemList);

            jobItemList.add(jobItem);
            cursor.moveToNext();
        }

        jobItemGroup.setData(jobItemList);
        return jobItemGroup;
    }

    public JobItemGroup getAllJob() {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_JOB, null, null, null, null, null, INSTALLSTARTDATE + " DESC");

        if (cursor != null) {
            cursor.moveToFirst();
        }
        JobItemGroup jobItemGroup = new JobItemGroup();
        jobItemGroup.setStatus("SUCCESS");
        jobItemGroup.setMessage("SUCCESS");
        List<JobItem> jobItemList = new ArrayList<JobItem>();
        List<AddressItem> addressItemList = null;
        List<ProductItem> productItemList = null;

        while(!cursor.isAfterLast()) {
            JobItem jobItem = new JobItem();
            jobItem.setOrderid(cursor.getString(cursor.getColumnIndex(JOBORDERID)).trim());
            jobItem.setIDCard(cursor.getString(cursor.getColumnIndex(IDCARD)).trim());
            jobItem.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)).trim());
            jobItem.setFirstName(cursor.getString(cursor.getColumnIndex(FIRSTNAME)).trim());
            jobItem.setLastName(cursor.getString(cursor.getColumnIndex(LASTNAME)).trim());
            jobItem.setInstallStartDate(cursor.getString(cursor.getColumnIndex(INSTALLSTARTDATE)).trim());
            jobItem.setInstallEndDate(cursor.getString(cursor.getColumnIndex(INSTALLENDDATE)).trim());
            jobItem.setInstallStart(cursor.getString(cursor.getColumnIndex(INSTALLSTART)));
            jobItem.setInstallEnd(cursor.getString(cursor.getColumnIndex(INSTALLEND)));
            jobItem.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            jobItem.setPresale(cursor.getString(cursor.getColumnIndex(PRESALE)));
            jobItem.setContno(cursor.getString(cursor.getColumnIndex(JOB_CONTNO)));

            addressItemList = new ArrayList<AddressItem>();
            addressItemList = getAllAddress(cursor.getString(1).trim());
            jobItem.setAddress(addressItemList);

            productItemList = new ArrayList<ProductItem>();
            ProductItem productItem = new ProductItem();
            productItem.setProductCode(cursor.getString(9).trim());
            productItem.setProductName(cursor.getString(10).trim());
            productItem.setProductQty(cursor.getString(11).trim());
            productItemList.add(productItem);
            jobItem.setProduct(productItemList);

            jobItemList.add(jobItem);
            cursor.moveToNext();
        }

        jobItemGroup.setData(jobItemList);
        return jobItemGroup;
    }

    public JobItemGroup getAllJobByDate(String date) {
        sqlite = this.getReadableDatabase();
        date = date + "%";
        Cursor cursor = sqlite.query
                (Constance.TABLE_JOB, null, INSTALLSTARTDATE + " LIKE ?",
                        new String[] { date }, null, null, INSTALLSTARTDATE + " DESC");

        if (cursor != null) {
            cursor.moveToFirst();
        }
        JobItemGroup jobItemGroup = new JobItemGroup();
        jobItemGroup.setStatus("SUCCESS");
        jobItemGroup.setMessage("SUCCESS");
        List<JobItem> jobItemList = new ArrayList<JobItem>();
        List<AddressItem> addressItemList = null;
        List<ProductItem> productItemList = null;

        while(!cursor.isAfterLast()) {
            JobItem jobItem = new JobItem();
            jobItem.setOrderid(cursor.getString(cursor.getColumnIndex(JOBORDERID)).trim());
            jobItem.setIDCard(cursor.getString(cursor.getColumnIndex(IDCARD)).trim());
            jobItem.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)).trim());
            jobItem.setFirstName(cursor.getString(cursor.getColumnIndex(FIRSTNAME)).trim());
            jobItem.setLastName(cursor.getString(cursor.getColumnIndex(LASTNAME)).trim());
            jobItem.setInstallStartDate(cursor.getString(cursor.getColumnIndex(INSTALLSTARTDATE)).trim());
            jobItem.setInstallEndDate(cursor.getString(cursor.getColumnIndex(INSTALLENDDATE)).trim());
            jobItem.setInstallStart(cursor.getString(cursor.getColumnIndex(INSTALLSTART)));
            jobItem.setInstallEnd(cursor.getString(cursor.getColumnIndex(INSTALLEND)));
            jobItem.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            jobItem.setPresale(cursor.getString(cursor.getColumnIndex(PRESALE)));
            jobItem.setContno(cursor.getString(cursor.getColumnIndex(JOB_CONTNO)));

            addressItemList = new ArrayList<AddressItem>();
            addressItemList = getAllAddress(cursor.getString(1).trim());
            jobItem.setAddress(addressItemList);

            productItemList = new ArrayList<ProductItem>();
            ProductItem productItem = new ProductItem();
            productItem.setProductCode(cursor.getString(9).trim());
            productItem.setProductName(cursor.getString(10).trim());
            productItem.setProductQty(cursor.getString(11).trim());
            productItemList.add(productItem);
            jobItem.setProduct(productItemList);

            jobItemList.add(jobItem);
            cursor.moveToNext();
        }

        jobItemGroup.setData(jobItemList);
        return jobItemGroup;
    }

    public boolean setCancelJob(String orderid, String cancelnote) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        String currentDate = sdf.format(new Date());
        sqlite = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CANCELNOTE, cancelnote);
        values.put(CANCELDATE, currentDate);
        values.put(JOB_UPDATED, currentDate);
        values.put(STATUS, "91");
        long success = sqlite.update(Constance.TABLE_JOB, values, JOBORDERID + " = ?", new String[]{ orderid });
        if (success > 0) {
            sqlite.close();
            return true;
        } else {
            sqlite.close();
            return false;
        }
    }

    public boolean setJobFinish(String orderid, String contno) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        String currentDate = sdf.format(new Date());
        sqlite = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(INSTALLSTART, currentDate);
        values.put(INSTALLEND, currentDate);
        values.put(STATUS, "01");
        values.put(JOB_UPDATED, currentDate);
        values.put(JOB_CONTNO, contno);
        long success = sqlite.update(Constance.TABLE_JOB, values, JOBORDERID + " = ?", new String[]{ orderid });
        if (success > 0) {
            sqlite.close();
            return true;
        } else {
            sqlite.close();
            return false;
        }
    }

    public String checkContno(String orderid) {
        String contno = null;
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_JOB, new String[] { JOB_CONTNO }, JOBORDERID + " = ? ",
                        new String[] { orderid },
                        null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            contno = cursor.getString(cursor.getColumnIndex(JOB_CONTNO)).trim();
        }

        sqlite.close();
        return contno;
    }

    public JobFinishItem getFinishData(String orderid, String contno) {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_JOB, null, JOBORDERID + " = ?",
                        new String[] { orderid },
                        null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        JobFinishItem jobFinishItem = null;
        if (!cursor.isAfterLast()) {
            jobFinishItem = new JobFinishItem()
                    .setOrderid(cursor.getString(cursor.getColumnIndex(JOBORDERID)))
                    .setInstallstart(cursor.getString(cursor.getColumnIndex(INSTALLSTART)))
                    .setInstallend(cursor.getString(cursor.getColumnIndex(INSTALLEND)))
                    .setStatus(cursor.getString(cursor.getColumnIndex(STATUS)))
                    .setUsercode(MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID))
                    .setContno(cursor.getString(cursor.getColumnIndex(JOB_CONTNO)));
        }

        sqlite.close();
        return jobFinishItem;
    }

    public boolean getOrderid(String orderid) {
        boolean isExist = false;
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_JOB, null, JOBORDERID + " = ?",
                        new String[] { orderid },
                        null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                isExist = true;
            }
            cursor.close();
        }
        return isExist;
    }

    public void setTableProduct(List<JobItem> jobItemList) {
        sqlite = this.getWritableDatabase();
        for (int i = 0; i < jobItemList.size(); i++) {
            for (ProductItem productItem : jobItemList.get(i).getProduct()) {
                if (!productItem.getProductItemQty().equals("0")) {
                    int itemQty = Integer.parseInt(productItem.getProductItemQty());
                    for (int j = 0; j < itemQty; j++) {
                        ContentValues values = new ContentValues();
                        values.put(PRODUCT_ORDER_ID, jobItemList.get(i).getOrderid());
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
                        sqlite.insert(Constance.TABLE_PRODUCT, null, values);
                    }
                } else if (Integer.parseInt(productItem.getProductQty()) > 1){
                    for (int k = 0; k < Integer.parseInt(productItem.getProductQty()); k++) {
                        ContentValues values = new ContentValues();
                        values.put(PRODUCT_ORDER_ID, jobItemList.get(i).getOrderid());
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
                        sqlite.insert(Constance.TABLE_PRODUCT, null, values);
                    }
                } else {
                    ContentValues values = new ContentValues();
                    values.put(PRODUCT_ORDER_ID, jobItemList.get(i).getOrderid());
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
                    sqlite.insert(Constance.TABLE_PRODUCT, null, values);
                }
            }
        }
        sqlite.close();
    }

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
                    .setProductCode(cursor.getString(2))
                    .setProductName(cursor.getString(3))
                    .setProductModel(cursor.getString(4))
                    .setProductQty(cursor.getString(5))
                    .setProductItemCode(cursor.getString(6))
                    .setProductItemName(cursor.getString(7))
                    .setProductItemQty(cursor.getString(8))
                    .setProductPrice(cursor.getString(9))
                    .setProductDiscount(cursor.getString(10))
                    .setProductDiscountPercent(cursor.getString(11))
                    .setProductPayType(cursor.getString(12))
                    .setProductSerial(cursor.getString(13))
                    .setProductStatus(cursor.getString(14));
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

    public boolean getProductPackage(String orderid, String productcode) {
        boolean installed = false;
        Cursor cursor = sqlite.query
                (Constance.TABLE_PRODUCT, null, PRODUCT_ORDER_ID + " = ? AND " + PRODUCT_CODE + " = ? AND " + PRODUCT_STATUS + " = ?",
                        new String[] { orderid, productcode, Constance.PRODUCT_STATUS_WAIT },
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
        ContentValues values = new ContentValues();
        values.put(PRODUCT_SERIAL, serial);
        values.put(PRODUCT_STATUS, Constance.PRODUCT_STATUS_READY);
        long success = sqlite.update(Constance.TABLE_PRODUCT, values, "id = ?", new String[]{id});
        if (success > 0) {
            updateItemInstalled(serial);
        }
        sqlite.close();
    }

    public void setTableAddress(List<JobItem> jobItemList) {
        sqlite = this.getWritableDatabase();
        for (int i = 0; i < jobItemList.size(); i++) {
            for (AddressItem addressItem : jobItemList.get(i).getAddress()) {
                ContentValues values = new ContentValues();
                values.put(ORDERID, jobItemList.get(i).getOrderid());
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

    /*public List<JobImageItem> getAllImage(String orderid) {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_IMAGE, null, IMG_ORDER_ID + " = ?",
                        new String[] { orderid },
                        null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        List<JobImageItem> imageItemList = new ArrayList<JobImageItem>();
        while(!cursor.isAfterLast()) {
            JobImageItem jobImageItem = new JobImageItem()
                    .setOrderid(cursor.getString(1))
                    .setImageType(cursor.getString(3))
                    .setImageUrl(cursor.getString(4))
                    .setProductcode(cursor.getString(6));
            imageItemList.add(jobImageItem);
            cursor.moveToNext();
        }
        return imageItemList;
    }*/

    public List<ImageItem> getImageWithserial(String orderid, String serial, String type) {
        sqlite = this.getReadableDatabase();
        Cursor cursor = null;
        if (serial.isEmpty()) {
            cursor = sqlite.query
                    (Constance.TABLE_IMAGE, null, IMG_ORDER_ID + " = ? AND " + IMG_TYPE + " = ?",
                            new String[]{orderid, type},
                            null, null, null);
        } else {
            cursor = sqlite.query
                    (Constance.TABLE_IMAGE, null, IMG_ORDER_ID + " = ? AND " + IMG_SERIAL + " = ? AND " + IMG_TYPE + " = ?",
                            new String[]{orderid, serial, type},
                            null, null, null);
        }

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
            values.put(STOCK_ITEM_INSTALL_DATE, "");
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
                    .setPrintTakeStockID(cursor.getString(1))
                    .setProduct_SerialNum(cursor.getString(2))
                    .setProduct_Code(cursor.getString(3))
                    .setProduct_Name(cursor.getString(4))
                    .setAStockStatus(cursor.getString(7));

            installItemList.add(item);
            cursor.moveToNext();
        }

        return installItemList;
    }

    public boolean checkItemSerial(String serial, String productcode) {
        boolean collect = false;
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_INSTALL_ITEM, null,STOCK_ITEM_SERIAL + " = ? AND " + STOCK_ITEM_CODE + " = ? AND " + STOCK_ITEM_STATUS + " = 1",
                        new String[] {serial, productcode},
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

    public void updateItemInstalled(String serial) {
        sqlite = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STOCK_ITEM_STATUS, "0");
        long query = sqlite.update(Constance.TABLE_INSTALL_ITEM, values, STOCK_ITEM_SERIAL + " = ?", new String[]{ serial });
        if (query > 0) {
            sqlite.close();
        }
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

    public List<JobSuccessItem> getDataSuccess(String orderid) {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_JOB, null, JOBORDERID + " = ?", new String[] {orderid}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        List<ImageSuccessItem> imageSuccessItemList;
        List<ProductSuccessItem> productSuccessItemList;
        List<JobSuccessItem> successItemList = new ArrayList<JobSuccessItem>();
        ;
        while (!cursor.isAfterLast()) {
            JobSuccessItem jobSuccessItem = new JobSuccessItem()
                    .setOrderID(cursor.getString(cursor.getColumnIndex(JOBORDERID)))
                    .setContno(cursor.getString(cursor.getColumnIndex(JOB_CONTNO)))
                    .setInstallStartDate(cursor.getString(cursor.getColumnIndex(INSTALLSTART)))
                    .setInstallEndDate(cursor.getString(cursor.getColumnIndex(INSTALLEND)))
                    .setProduct(getProductInstallSuccess(orderid));

            successItemList.add(jobSuccessItem);
            cursor.moveToNext();
        }

        sqlite.close();
        return successItemList;
    }

    public List<ProductSuccessItem> getProductInstallSuccess(String orderid) {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_PRODUCT, null, PRODUCT_ORDER_ID + " = ?", new String[] {orderid}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String productcode = null;
        List<ImageSuccessItem> imageSuccessItemList;
        List<ProductSuccessItem> productSuccessItemList = new ArrayList<ProductSuccessItem>();

        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex(PRODUCT_ITEM_CODE)).equals("-")) {
                productcode = cursor.getString(cursor.getColumnIndex(PRODUCT_CODE));
            } else {
                productcode = cursor.getString(cursor.getColumnIndex(PRODUCT_ITEM_CODE));
            }
            ProductSuccessItem productSuccessItem = new ProductSuccessItem()
                    .setProductCode((productcode))
                    .setProductSerial(cursor.getString(cursor.getColumnIndex(PRODUCT_SERIAL)));

            imageSuccessItemList = new ArrayList<ImageSuccessItem>();
            imageSuccessItemList = getAllImage(orderid);

            productSuccessItem.setImages(imageSuccessItemList);

            productSuccessItemList.add(productSuccessItem);
            cursor.moveToNext();
        }

        return productSuccessItemList;
    }

    public List<ImageSuccessItem> getAllImage(String orderid) {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_IMAGE, null, IMG_ORDER_ID + " = ?",
                        new String[] { orderid },
                        null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        List<ImageSuccessItem> imageItemList = new ArrayList<ImageSuccessItem>();
        while(!cursor.isAfterLast()) {
            ImageSuccessItem imageSuccessItem = new ImageSuccessItem()
                    .setImageType(cursor.getString(cursor.getColumnIndex(IMG_TYPE)))
                    .setImageBase64(encodeImage(new File(cursor.getString(cursor.getColumnIndex(IMG_URL)))))
                    .setImageProductCode(cursor.getString(cursor.getColumnIndex(IMG_PRODUCT_CODE)));
            cursor.moveToNext();
        }
        return imageItemList;
    }

    private String encodeImage(File imagefile) {
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(imagefile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }
}
