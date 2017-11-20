package th.co.thiensurat.toss_installer.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.job.item.ProductItem;
import th.co.thiensurat.toss_installer.job.item.ProductItemGroup;
import th.co.thiensurat.toss_installer.takepicture.item.ImageItem;
import th.co.thiensurat.toss_installer.utils.Constance;

/**
 * Created by teerayut.k on 11/13/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private StringBuilder sb, sb2, sb3;
    private SQLiteDatabase sqlite;

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

    public static final String COL_IMG_ORDER_ID = "order_id";
    public static final String COL_IMG_TYPE = "img_type";
    public static final String COL_IMG_URL = "img_url";
    public static final String COL_IMG_STATUS = "img_status";

    public static final String PRODUCT_ORDER_ID = "orderid";
    public static final String PRODUCT_CODE = "productCode";
    public static final String PRODUCT_NAME = "productName";
    public static final String PRODUCT_QTY = "productQty";
    public static final String PRODUCT_ITEM_CODE = "productItemCode";
    public static final String PRODUCT_ITEM_NAME = "productItemName";
    public static final String PRODUCT_ITEM_QTY = "productItemQty";
    public static final String PRODUCT_SERIAL = "productSerial";
    public static final String PRODUCT_STATUS = "productStatus";

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
        sb2.append(" " + COL_IMG_ORDER_ID + " TEXT,");
        sb2.append(" " + COL_IMG_TYPE + " TEXT,");
        sb2.append(" " + COL_IMG_URL + " TEXT,");
        sb2.append(" " + COL_IMG_STATUS + " TEXT)");
        String CREATE_TABLE_IMAGE = sb2.toString();
        sqLiteDatabase.execSQL(CREATE_TABLE_IMAGE);

        sb3 = new StringBuilder();
        sb3.append("CREATE TABLE " + Constance.TABLE_PRODUCT + " (");
        sb3.append(" id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb3.append(" " + PRODUCT_ORDER_ID + " TEXT,");
        sb3.append(" " + PRODUCT_CODE + " TEXT,");
        sb3.append(" " + PRODUCT_NAME + " TEXT,");
        sb3.append(" " + PRODUCT_QTY + " TEXT,");
        sb3.append(" " + PRODUCT_ITEM_CODE + " TEXT,");
        sb3.append(" " + PRODUCT_ITEM_NAME + " TEXT,");
        sb3.append(" " + PRODUCT_ITEM_QTY + " TEXT,");
        sb3.append(" " + PRODUCT_SERIAL + " TEXT,");
        sb3.append(" " + PRODUCT_STATUS + " TEXT)");
        String CREATE_TABLE_PRODUCT = sb3.toString();
        sqLiteDatabase.execSQL(CREATE_TABLE_PRODUCT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_ADDRESS_TABLE = "DROP TABLE IF EXISTS " + Constance.TABLE_ADDRESS;
        sqLiteDatabase.execSQL(DROP_ADDRESS_TABLE);

        String DROP_IMAGE_TABLE = "DROP TABLE IF EXISTS " + Constance.TABLE_IMAGE;
        sqLiteDatabase.execSQL(DROP_IMAGE_TABLE);

        String DROP_PRODUCT_TABLE = "DROP TABLE IF EXISTS " + Constance.TABLE_PRODUCT;
        sqLiteDatabase.execSQL(DROP_PRODUCT_TABLE);

        onCreate(sqLiteDatabase);
    }

    public void setTableProduct(List<JobItem> jobItemList) {
        String temp = "";
        String temp2 = "";
        for (int i = 0; i < jobItemList.size(); i++) {
            sqlite = this.getWritableDatabase();
            for (ProductItem productItem : jobItemList.get(i).getProduct()) {
                if (temp.isEmpty()) {
                    temp = productItem.getProductCode();
                } else {
                    temp2 = temp;
                    temp = productItem.getProductCode();
                }
                if (!productItem.getProductItemQty().isEmpty()) {
                    int itemQty = Integer.parseInt(productItem.getProductItemQty());
                    for (int j = 0; j < itemQty; j++) {
                        ContentValues values = new ContentValues();
                        values.put(PRODUCT_ORDER_ID, jobItemList.get(i).getOrderid());
                        values.put(PRODUCT_CODE, productItem.getProductCode());
                        values.put(PRODUCT_NAME, productItem.getProductName());
                        values.put(PRODUCT_QTY, productItem.getProductQty());
                        values.put(PRODUCT_ITEM_CODE, productItem.getProductItemCode());
                        values.put(PRODUCT_ITEM_NAME, productItem.getProductItemName());
                        values.put(PRODUCT_ITEM_QTY, String.valueOf(1));
                        values.put(PRODUCT_SERIAL, "");
                        values.put(PRODUCT_STATUS, Constance.PRODUCT_STATUS_WAIT);
                        sqlite.insert(Constance.TABLE_PRODUCT, null, values);
                    }
                } else {
                    ContentValues values = new ContentValues();
                    values.put(PRODUCT_ORDER_ID, jobItemList.get(i).getOrderid());
                    values.put(PRODUCT_CODE, productItem.getProductCode());
                    values.put(PRODUCT_NAME, productItem.getProductName());
                    values.put(PRODUCT_QTY, productItem.getProductQty());
                    values.put(PRODUCT_ITEM_CODE, productItem.getProductItemCode());
                    values.put(PRODUCT_ITEM_NAME, productItem.getProductItemName());
                    values.put(PRODUCT_ITEM_QTY, productItem.getProductItemQty());
                    values.put(PRODUCT_SERIAL, "");
                    values.put(PRODUCT_STATUS, Constance.PRODUCT_STATUS_WAIT);
                    sqlite.insert(Constance.TABLE_PRODUCT, null, values);
                }
            }
        }
        sqlite.close();
    }

    public ProductItemGroup getProductByID(String orderid) {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_PRODUCT, null, PRODUCT_ORDER_ID + " = ? ",
                        new String[] { orderid },
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
                    .setProductQty(cursor.getString(4))
                    .setProductItemCode(cursor.getString(5))
                    .setProductItemName(cursor.getString(6))
                    .setProductItemQty(cursor.getString(7))
                    .setProductSerial(cursor.getString(8))
                    .setProductStatus(cursor.getString(9));
            productItemList.add(productItem);
            cursor.moveToNext();
        }
        ProductItemGroup productItemGroup = new ProductItemGroup();
        productItemGroup.setProduct(productItemList);
        sqlite.close();
        return productItemGroup;
    }

    public void updateSerialToTableProduct(String id, String serial) {
        sqlite = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRODUCT_SERIAL, serial);
        values.put(PRODUCT_STATUS, Constance.PRODUCT_STATUS_READY);
        sqlite.update(Constance.TABLE_PRODUCT, values, "id = ?", new String[]{id});
        sqlite.close();
    }

    public void setTableAddress(List<JobItem> jobItemList) {
        for (int i = 0; i < jobItemList.size(); i++) {
            sqlite = this.getWritableDatabase();
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

    public void addImage(String orderid, String type, String url) {
        sqlite = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_IMG_ORDER_ID, orderid);
        values.put(COL_IMG_TYPE, type);
        values.put(COL_IMG_URL, url);
        sqlite.insert(Constance.TABLE_IMAGE, null, values);
        sqlite.close();
    }

    public void editImage(String id, String url) {
        sqlite = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_IMG_URL, url);
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
                (Constance.TABLE_IMAGE, null, COL_IMG_ORDER_ID + " = ? AND " + COL_IMG_TYPE + " = ?",
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
                    .setImageType(cursor.getString(2))
                    .setImageUrl(cursor.getString(3));
            imageItemList.add(imageItem);
            cursor.moveToNext();
        }
        return imageItemList;
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
}
