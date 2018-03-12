package th.co.thiensurat.toss_installer.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import th.co.thiensurat.toss_installer.takepicture.TakePictureActivity;
import th.co.thiensurat.toss_installer.takepicturecard.TakeIDCardActivity;


/**
 * Created by teerayut.k on 11/13/2017.
 */

public class ImageConfiguration {

    private Context context;
    private String dirName;

    public ImageConfiguration(Context context) {
        this.context = context;
    }

    public File createImageFile(String dirName) {
        File image = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + dirName + "/");
            image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        } catch (IOException ex) {

        }
        return image;
    }

    public File createImage(String dirName, String imgName) {
        File image = null;
        try {
            //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + dirName + "_" + imgName;
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + dirName + "/");
            image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        } catch (IOException ex) {

        }
        return image;
    }

    public File createImageByType(String orderid, String serial, String type) {
        File image = null;
        try {
            String imageFileName = orderid + serial + type;
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + orderid + "/");
            image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        } catch (IOException e) {

        }
        return image;
    }

    public File createImagePNG(String dirName, String imgName) {
        File image = null;
        try {
            //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            //String imageFileName = dirName + "_" + imgName;
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + dirName + "/");
            image = File.createTempFile(
                    imgName,
                    ".png",
                    storageDir
            );
        } catch (IOException ex) {

        }
        return image;
    }

    public void removeImage(String url) {
        File fileimage = null;
        fileimage = new File(url);
        if (fileimage.exists()) {
            if (fileimage.delete()) {

            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public File getAlbumStorageDir(String albumName) {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }
}
