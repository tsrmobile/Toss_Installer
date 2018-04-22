package th.co.thiensurat.toss_installer.takepicture;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.BuildConfig;
import th.co.thiensurat.toss_installer.MainActivity;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.contract.ContractActivity;
import th.co.thiensurat.toss_installer.installation.InstallationActivity;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItemGroup;
import th.co.thiensurat.toss_installer.takepicture.adapter.TakePictureAdapter;
import th.co.thiensurat.toss_installer.takepicture.item.ImageItem;
import th.co.thiensurat.toss_installer.takepicturecard.TakeIDCardActivity;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.ImageConfiguration;
import th.co.thiensurat.toss_installer.utils.MyApplication;
import th.co.thiensurat.toss_installer.utils.PermissionUtil;

import static th.co.thiensurat.toss_installer.utils.Constance.REQUEST_CAMERA;

public class TakePictureActivity extends BaseMvpActivity<TakePictureInterface.Presenter>
        implements TakePictureInterface.View, TakePictureAdapter.ClickListener {

    private File file;
    private Uri pictureUri;
    private TextView textViewTitle;
    private CustomDialog customDialog;
    private ImageConfiguration imageConfiguration;

    private String action;
    private String id = "-1";
    private String serial;
    private String productcode;
    private TakePictureAdapter adapter;
    private List<ImageItem> imageItemList;
    private LinearLayoutManager layoutManager;

    private JobItem jobItem;
    private ProductItemGroup productItemGroup;
    private List<ProductItem> productItemList;

    private File image;

    @Override
    public TakePictureInterface.Presenter createPresenter() {
        return TakePicturePresenter.create(TakePictureActivity.this);
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_take_picture;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.floating_camera) Button floatingActionButton;
    @BindView(R.id.layout_bottom) RelativeLayout relativeLayoutBottom;
    @BindView(R.id.button_next) Button buttonNext;
    @BindView(R.id.layout_fail) RelativeLayout relativeLayoutFail;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    @Override
    public void setupInstance() {
        imageConfiguration = new ImageConfiguration(TakePictureActivity.this);
        adapter = new TakePictureAdapter(TakePictureActivity.this);
        customDialog = new CustomDialog(TakePictureActivity.this);
        layoutManager = new LinearLayoutManager(TakePictureActivity.this);
    }

    @Override
    public void setupView() {
        setToolbar();
        setRecyclerView();
        buttonNext.setOnClickListener( onNext() );
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingActionButton.startAnimation(new AnimateButton().animbutton());
                id = "-1";
                try {
                    imageChooser();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void initialize() {
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        jobItem = getIntent().getParcelableExtra(Constance.KEY_JOB_ITEM);
        serial = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_SERIAL);
        productcode = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_PRODUCT_CODE);
        getPresenter().getImage(jobItem.getOrderid(), Constance.IMAGE_TYPE_INSTALL);
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void setImageToAdapter(List<ImageItem> imageItems) {
        this.imageItemList = imageItems;
        adapter.setPictureItem(imageItemList);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(this);
        adapter.notifyDataSetChanged();

        if (imageItemList.size() > 0) {
            relativeLayoutBottom.setVisibility(View.VISIBLE);
            relativeLayoutFail.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            relativeLayoutBottom.setVisibility(View.GONE);
            relativeLayoutFail.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoading() {
        customDialog.dialogLoading();
    }

    @Override
    public void onDismiss() {
        customDialog.dialogDimiss();
    }

    @Override
    public void onFail(String fail) {
        customDialog.dialogFail(fail);
    }

    @Override
    public void onSuccess(String success) {
        refresh();
    }

    @Override
    public void refresh() {
        getPresenter().getImage(jobItem.getOrderid(), Constance.IMAGE_TYPE_INSTALL);
    }

    private void setToolbar() {
        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textViewTitle.setText("ถ่ายรูปการติดตั้ง");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gallery_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(TakePictureActivity.this, InstallationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            setResult(RESULT_OK, intent);
            finish();
        } else if (item.getItemId() == R.id.menu_gallery) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, Constance.REQUEST_GALLERY);
        } else if (item.getItemId() == R.id.menu_home) {
            Intent intent = new Intent(TakePictureActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener onNext() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonNext.startAnimation(new AnimateButton().animbutton());
                if (getPresenter().getAllItem(jobItem.getOrderid()).size() > 1) {
                    if (getPresenter().getItemInstalled(jobItem.getOrderid())) {
                        onNextInstall();
                    } else {
                        onNextStep();
                    }
                } else {
                    onNextStep();
                }
            }
        };
    }

    private void imageChooser() throws IOException {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = imageConfiguration.createImageByType(jobItem.getOrderid(), serial, Constance.IMAGE_TYPE_INSTALL);
        pictureUri = Uri.fromFile( file );
        takePicture.putExtra( MediaStore.EXTRA_OUTPUT, pictureUri );
        startActivityForResult(takePicture, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constance.REQUEST_GALLERY) {
                Uri selectedImage = data.getData();
                setImage(new File(imageConfiguration.getRealPathFromURI(selectedImage)));
            } else if (requestCode == REQUEST_CAMERA) {
                setImage(file);
            }
        }
    }

    @Override
    public void itemClicked(View view, int position) {
        ImageItem item = imageItemList.get(position);
        id = item.getImageId();
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = imageConfiguration.createImageByType(jobItem.getOrderid(), serial, Constance.IMAGE_TYPE_INSTALL);
        pictureUri = Uri.fromFile( file );
        takePicture.putExtra( MediaStore.EXTRA_OUTPUT, pictureUri );
        startActivityForResult(takePicture, REQUEST_CAMERA);
    }

    @Override
    public void delClicked(View view, int position) {
        ImageItem item = imageItemList.get(position);
        id = item.getImageId();
        getPresenter().delImage(id);
        imageConfiguration.removeImage(item.getImageUrl());
    }

    private void setImage(File file) {
        image = new File(file.getAbsolutePath());
        String url = file.getPath().toString();
        if (id.equals("-1")) {
            getPresenter().saveImageUrl(jobItem.getOrderid()
                    , serial
                    , Constance.IMAGE_TYPE_INSTALL, url, productcode);
        } else {
            getPresenter().editImageUrl(id, url);
        }

        floatingActionButton.setVisibility(View.GONE);
    }

    private void onNextStep() {
        getPresenter().updateStep(jobItem.getOrderid(), Constance.STEP_2);
        getPresenter().updateStep(jobItem.getOrderid(), Constance.STEP_3);

        if (getPresenter().getProductPayType(jobItem.getOrderid(), productcode, serial).equals("2")) {
            Intent intent = new Intent(TakePictureActivity.this, TakeIDCardActivity.class);
            intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
            startActivityForResult(intent, Constance.REQUEST_TAKE_IDCARD);
        } else {
            Intent intent = new Intent(TakePictureActivity.this, ContractActivity.class);
            intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
            startActivityForResult(intent, Constance.REQUEST_PRINT_CONTRACT);
        }
    }

    private void onNextInstall() {
        Intent intent = new Intent(getApplicationContext(), InstallationActivity.class);
        intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
        startActivity(intent);
    }
}
