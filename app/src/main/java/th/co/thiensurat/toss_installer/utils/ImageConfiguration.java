package th.co.thiensurat.toss_installer.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import th.co.thiensurat.toss_installer.BuildConfig;
import th.co.thiensurat.toss_installer.takepicture.TakePictureActivity;
import th.co.thiensurat.toss_installer.takepicturecard.TakeIDCardActivity;

/**
 * Created by teerayut.k on 11/13/2017.
 */

public class ImageConfiguration {

    private Context context;

    public ImageConfiguration(Context context) {
        this.context = context;
    }

    public File createImageFile() {
        File image = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(
                    imageFileName,
                    ".jpg",
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
                if (context instanceof TakePictureActivity) {
                    ((TakePictureActivity) context).refresh();
                } else if (context instanceof TakeIDCardActivity) {
                    ((TakeIDCardActivity) context).refresh();
                }
            }
        }
    }
}
