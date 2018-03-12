package th.co.thiensurat.toss_installer.contract.signaturepad;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.ImageConfiguration;
import th.co.thiensurat.toss_installer.utils.MyApplication;

public class SignatureActivity extends AppCompatActivity {

    private String empid;
    private File signPath;

    private String key;
    private Bitmap bitmap;
    private Bitmap bitmap2;
    private String orderid;
    private File mergeSignPath;
    private File customerSignPath;
    private File installationPath;
    private ImageConfiguration imageConfiguration;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static SignatureActivity getsInstance() {
        return new SignatureActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 1.0f;
        params.dimAmount = 0.5f;
        getWindow().setAttributes((WindowManager.LayoutParams) params);
        getWindow().setLayout(((WindowManager.LayoutParams) params).WRAP_CONTENT, ((WindowManager.LayoutParams) params).WRAP_CONTENT);
        setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_signature);
        setUpView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            return false;
        }
        return true;
    }

    @BindView(R.id.getsign) Button buttonSave;
    @BindView(R.id.cancel) Button buttonCancel;
    @BindView(R.id.clear) Button buttonClear;
    @BindView(R.id.signature_pad) SignaturePad signaturePad;
    private void setUpView() {
        ButterKnife.bind(this);
        verifyStoragePermissions(this);
        imageConfiguration = new ImageConfiguration(SignatureActivity.this);
        buttonSave.setOnClickListener( onSign() );
        buttonCancel.setOnClickListener( onCancel() );
        buttonClear.setOnClickListener( onClear() );
        getDataFromIntent();
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                buttonSave.setEnabled(true);
                buttonSave.setBackground(getResources().getDrawable(R.drawable.background_rounded_green));
            }

            @Override
            public void onClear() {

            }
        });
    }

    private void getDataFromIntent() {
        orderid = getIntent().getStringExtra(Constance.KEY_ORDER_ID);
        try {
            customerSignPath = new File(imageConfiguration.getAlbumStorageDir(orderid), String.format("signature_%s.jpg", orderid));
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(customerSignPath.getAbsolutePath(), bmOptions);
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(),true);
            signaturePad.setSignatureBitmap(bitmap);
        } catch (Exception ex) {}
    }

    private View.OnClickListener onClear() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signaturePad.clear();
                buttonSave.setEnabled(false);
                buttonSave.setBackground(getResources().getDrawable(R.drawable.background_rounded_gray));
            }
        };
    }

    private View.OnClickListener onSign() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
                if (addJpgSignatureToGallery(signatureBitmap)) {
                    Toast.makeText(SignatureActivity.this, "บันทึกลายเซ็นแล้ว", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignatureActivity.this, "ไม่สามารถบันทึกลายเซ็นได้", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent();
                intent.putExtra("status", "done");
                intent.putExtra("pathSignContact", mergeSignPath.getAbsolutePath().toString());
                intent.putExtra("pathCustomer", customerSignPath.getAbsolutePath().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        };
    }

    private View.OnClickListener onCancel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        };
    }

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity the activity from which permissions are checked
     */
    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                }
            }
        }
    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            customerSignPath = new File(imageConfiguration.getAlbumStorageDir(orderid), String.format("signature_%s.jpg", orderid));
            mergeSignPath = new File(imageConfiguration.getAlbumStorageDir(orderid), String.format("signature_%s_contact.jpg", orderid));
            /*if (key.equals("contact")) {
                mergeSignPath = new File(imageConfiguration.getAlbumStorageDir(orderid), String.format("signature_%s_contact.jpg", orderid));
            } else {
                mergeSignPath = new File(imageConfiguration.getAlbumStorageDir(orderid), String.format("signature_%s_install_receipt.jpg", orderid));
            }*/
            saveBitmapToJPG(signature, customerSignPath);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        stream.close();

        try {
            AssetManager assetManager = this.getAssets();
            bitmap = BitmapFactory.decodeStream(assetManager.open("k_viruch2.png"), null, null);
            createSingleImageFromMultipleImages(bitmap, getResizedBitmap(newBitmap, 210, 71));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createSingleImageFromMultipleImages(Bitmap firstImage, Bitmap secondImage) throws IOException {
        Bitmap result = Bitmap.createBitmap((firstImage.getWidth() + secondImage.getWidth()), firstImage.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(firstImage, 0, 0, null);
        canvas.drawBitmap(secondImage, firstImage.getWidth(), 0, null);
        OutputStream stream = new FileOutputStream(mergeSignPath);
        result.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        stream.close();
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    /*private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        SignatureActivity.this.sendBroadcast(mediaScanIntent);
    }

    public boolean addSvgSignatureToGallery(String signatureSvg) {
        boolean result = false;
        try {
            fileSVG = new File(getAlbumStorageDir(dirname), String.format("Signature_%s.svg", imagename));
            OutputStream stream = new FileOutputStream(fileSVG);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(signatureSvg);
            writer.close();
            stream.flush();
            stream.close();
            scanMediaFile(fileSVG);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }*/
}
