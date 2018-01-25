package th.co.thiensurat.toss_installer.utils.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.toss_installer.api.result.data.DataItem;
import th.co.thiensurat.toss_installer.utils.Constance;

/**
 * Created by teerayut.k on 1/3/2018.
 */

public class ExDBHelper extends SQLiteOpenHelper {

    private DataItem item;
    private Context context;
    private SQLiteDatabase sqlite;
    private static final String DATABASE_NAME = "data.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";

    public ExDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    public List<DataItem> getAllProvince() {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_PROVINCE, null, null,
                        null,
                        null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        List<DataItem> dataItemList = new ArrayList<DataItem>();

        while(!cursor.isAfterLast()) {
            item = new DataItem();
            item.setDataId(cursor.getString(0).trim());
            item.setDataCode(cursor.getString(1).trim());
            item.setDataName(cursor.getString(2).trim());

            dataItemList.add(item);
            cursor.moveToNext();
        }
        sqlite.close();
        return dataItemList;
    }

    public List<DataItem> getDistrictByProvince(String id) {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_DISTRICT, null, "PROVINCE_ID = ?",
                        new String[] { id },
                        null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        List<DataItem> dataItemList = new ArrayList<DataItem>();

        while(!cursor.isAfterLast()) {
            item = new DataItem();
            item.setDataId(cursor.getString(0).trim());
            item.setDataCode(cursor.getString(1).trim());
            item.setDataName(cursor.getString(2).trim());

            dataItemList.add(item);
            cursor.moveToNext();
        }
        sqlite.close();
        return dataItemList;
    }

    public List<DataItem> getSubdistrictByDistrict(String id) {
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                (Constance.TABLE_SUB_DISTRICT, null, "AMPHUR_ID = ?",
                        new String[] { id },
                        null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        List<DataItem> dataItemList = new ArrayList<DataItem>();

        while(!cursor.isAfterLast()) {
            item = new DataItem();
            item.setDataId(cursor.getString(0).trim());
            item.setDataCode(cursor.getString(1).trim());
            item.setDataName(cursor.getString(2).trim());

            dataItemList.add(item);
            cursor.moveToNext();
        }
        sqlite.close();
        return dataItemList;
    }

    public String getZipcode(String id) {
        String zipcode = null;
        sqlite = this.getReadableDatabase();
        Cursor cursor = sqlite.query
                ("ZipCode", null, "DISTRICT_ID = ?",
                        new String[] { id },
                        null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if(!cursor.isAfterLast()) {
            zipcode = cursor.getString(5);
        }
        sqlite.close();
        return zipcode;
    }

    private String getDatabasePath() {
        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        File dbFile = context.getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                System.out.println("Copying sucess from Assets folder");
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    public void CopyDataBaseFromAsset() throws IOException{
        InputStream inputStream = context.getAssets().open(DATABASE_NAME);
        String outFileName = getDatabasePath();
        File f = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();
        OutputStream outputStream = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
