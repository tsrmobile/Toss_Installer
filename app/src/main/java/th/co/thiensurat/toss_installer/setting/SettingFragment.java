package th.co.thiensurat.toss_installer.setting;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.MainActivity;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpFragment;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.ImageConfiguration;
import th.co.thiensurat.toss_installer.utils.MyApplication;

import static th.co.thiensurat.toss_installer.utils.Constance.PERMISSIONS_STORAGE;
import static th.co.thiensurat.toss_installer.utils.Constance.REQUEST_EXTERNAL_STORAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends BaseMvpFragment<SettingInterface.Presenter> implements SettingInterface.View {

    private String empid;
    private File signPath;
    private File witness;
    private ImageConfiguration imageConfiguration;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment getInstance() {
        return new SettingFragment();
    }

    @Override
    public SettingInterface.Presenter createPresenter() {
        return SettingPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_setting;
    }

    @BindView(R.id.button_save) Button buttonSave;
    @BindView(R.id.button_delete) Button buttonDel;
    @BindView(R.id.signature_pad) SignaturePad signaturePad;
    @BindView(R.id.layout_remove) RelativeLayout relativeLayoutRemove;
    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
        buttonSave.setOnClickListener( onSave() );
        buttonDel.setOnClickListener( onDel() );
        relativeLayoutRemove.setOnClickListener( onClear() );
    }

    @Override
    public void setupInstance() {
        imageConfiguration = new ImageConfiguration(getActivity());
    }

    @Override
    public void setupView() {
        ((MainActivity)getActivity()).setTitle("ตั้งค่า");
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
        verifyStoragePermissions(getActivity());
    }

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
                    Toast.makeText(getActivity(), "บันทึกลายเซ็นแล้ว", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "ไม่สามารถบันทึกลายเซ็นได้", Toast.LENGTH_SHORT).show();
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
            AssetManager assetManager = getActivity().getAssets();
            bitmap = BitmapFactory.decodeStream(assetManager.open("witness.png"), null, null);
            createSingleImageFromMultipleImages(getResizedBitmap(newBitmap, 210, 71), bitmap);
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
        OutputStream stream = new FileOutputStream(witness);
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
}
