package th.co.thiensurat.toss_installer.setting.signature;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.ImageConfiguration;
import th.co.thiensurat.toss_installer.utils.MyApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignActivity extends BaseMvpActivity<SignInterface.Presenter> implements SignInterface.View {

    private String empid;
    private File signPath;
    private File witness;
    private TextView textViewTitle;
    private ImageConfiguration imageConfiguration;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    public SignInterface.Presenter createPresenter() {
        return SignPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_sign;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.button_save) Button buttonSave;
    @BindView(R.id.button_delete) Button buttonDel;
    @BindView(R.id.signature_pad) SignaturePad signaturePad;
    @BindView(R.id.layout_remove) RelativeLayout relativeLayoutRemove;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
        buttonSave.setOnClickListener( onSave() );
        buttonDel.setOnClickListener( onDel() );
        relativeLayoutRemove.setOnClickListener( onClear() );
    }

    @Override
    public void setupInstance() {
        imageConfiguration = new ImageConfiguration(this);
    }

    @Override
    public void setupView() {
        setToolbar();
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                buttonDel.setEnabled(true);
                buttonDel.setBackground(getResources().getDrawable(R.drawable.background_accent));
            }

            @Override
            public void onClear() {

            }
        });
    }

    @Override
    public void initialize() {
        try {
            empid = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID);

            witness = new File(imageConfiguration.getAlbumStorageDir(empid), String.format("signature_witness.jpg"));

            signPath = new File(imageConfiguration.getAlbumStorageDir(empid), String.format("signature_%s.jpg", empid));
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(signPath.getAbsolutePath(), bmOptions);
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(),true);
            signaturePad.setSignatureBitmap(bitmap);
        } catch (Exception e) {
            buttonDel.setEnabled(false);
            buttonDel.setBackground(getResources().getDrawable(R.drawable.background_gray));
        }
        verifyStoragePermissions(this);
    }

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_clear_white_24dp));
        textViewTitle.setText("เพิ่มลายเซ็นต์");
        setSupportActionBar(toolbar);
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

    private View.OnClickListener onClear() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signaturePad.clear();
            }
        };
    }

    private View.OnClickListener onSave() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSave.startAnimation(new AnimateButton().animbutton());
                Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
                if (saveSignatureBitmap(signatureBitmap)) {
                    Toast.makeText(getApplicationContext(), "บันทึกลายเซ็นแล้ว", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "ไม่สามารถบันทึกลายเซ็นได้", Toast.LENGTH_SHORT).show();

                }
            }
        };
    }

    private View.OnClickListener onDel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonDel.startAnimation(new AnimateButton().animbutton());
                if (signPath.exists()) {
                    imageConfiguration.removeImage(signPath.getAbsolutePath());
                    buttonDel.setEnabled(false);
                    buttonDel.setBackground(getResources().getDrawable(R.drawable.background_gray));
                    signaturePad.clear();

                }
            }
        };
    }

    private boolean saveSignatureBitmap(Bitmap bitmap) {
        boolean result = false;
        try {
            signPath = new File(imageConfiguration.getAlbumStorageDir(empid), String.format("signature_%s.jpg", empid));
            saveBitmapToJPG(bitmap, signPath);
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
            AssetManager assetManager = getAssets();
            bitmap = BitmapFactory.decodeStream(assetManager.open("witness.png"), null, null);
            imageConfiguration.createSingleImageFromMultipleImages(imageConfiguration.getResizedBitmap(newBitmap, 250, 60), bitmap, witness);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
