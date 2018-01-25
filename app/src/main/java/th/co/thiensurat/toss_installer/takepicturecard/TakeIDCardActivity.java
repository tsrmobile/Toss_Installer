package th.co.thiensurat.toss_installer.takepicturecard;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.BuildConfig;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.takepicture.TakePictureActivity;
import th.co.thiensurat.toss_installer.takepicture.adapter.TakePictureAdapter;
import th.co.thiensurat.toss_installer.takepicture.item.ImageItem;
import th.co.thiensurat.toss_installer.takepicturhome.TakeHomeActivity;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.ImageConfiguration;
import th.co.thiensurat.toss_installer.utils.PermissionUtil;

import static th.co.thiensurat.toss_installer.utils.Constance.REQUEST_CAMERA;

public class TakeIDCardActivity extends BaseMvpActivity<TakeIDCardInterface.Presenter>
        implements TakeIDCardInterface.View, TakePictureAdapter.ClickListener {

    private File file;
    private Uri pictureUri;
    private TextView textViewTitle;
    private CustomDialog customDialog;
    private PermissionUtil permissionUtil;
    private ImageConfiguration imageConfiguration;

    private String serial;
    private String id = "-1";
    private JobItem jobItem;
    private String productcode;
    private TakePictureAdapter adapter;
    private List<ImageItem> imageItemList;
    private LinearLayoutManager layoutManager;

    @Override
    public TakeIDCardInterface.Presenter createPresenter() {
        return TakeIDCardPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_take_idcard;
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
        permissionUtil = new PermissionUtil();
        imageConfiguration = new ImageConfiguration(TakeIDCardActivity.this);
        adapter = new TakePictureAdapter(TakeIDCardActivity.this);
        customDialog = new CustomDialog(TakeIDCardActivity.this);
        layoutManager = new LinearLayoutManager(TakeIDCardActivity.this);
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
        getPresenter().getImage(TakeIDCardActivity.this, jobItem.getOrderid(), Constance.IMAGE_TYPE_CARD);
    }

    private void getDataFromIntent() {
        jobItem = getIntent().getParcelableExtra(Constance.KEY_JOB_ITEM);
        serial = getIntent().getStringExtra(Constance.KEY_SERIAL_ITEM);
        productcode = getIntent().getStringExtra(Constance.KEY_PRODUCT_CODE);
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setToolbar() {
        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textViewTitle.setText("ถ่ายรูปบัตรประชาชน");
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
            setResult(RESULT_CANCELED);
            finish();
        } else if (item.getItemId() == R.id.menu_gallery) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            this.startActivityForResult(pickPhoto, Constance.REQUEST_GALLERY);
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener onNext() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonNext.startAnimation(new AnimateButton().animbutton());
                Intent intent = new Intent(TakeIDCardActivity.this, TakeHomeActivity.class);
                intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
                intent.putExtra(Constance.KEY_SERIAL_ITEM, serial);
                intent.putExtra(Constance.KEY_PRODUCT_CODE, productcode);
                startActivityForResult(intent, Constance.REQUEST_TAKE_HOME);
            }
        };
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
    public void refresh() {
        getPresenter().getImage(TakeIDCardActivity.this, jobItem.getOrderid(), Constance.IMAGE_TYPE_CARD);
    }

    private void imageChooser() throws IOException {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = imageConfiguration.createImageFile(jobItem.getOrderid());
        pictureUri = Uri.fromFile( file );
        takePicture.putExtra( MediaStore.EXTRA_OUTPUT, pictureUri );
        startActivityForResult(takePicture, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constance.REQUEST_GALLERY) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                setImage(new File(imageConfiguration.getRealPathFromURI(selectedImage)));
            }
        } else if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                setImage(file);
            }
        }
    }

    @Override
    public void itemClicked(View view, int position) {
        ImageItem item = imageItemList.get(position);
        id = item.getImageId();
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = imageConfiguration.createImageFile(jobItem.getOrderid());
        pictureUri = Uri.fromFile( file );
        takePicture.putExtra( MediaStore.EXTRA_OUTPUT, pictureUri );
        startActivityForResult(takePicture, REQUEST_CAMERA);
    }

    @Override
    public void delClicked(View view, int position) {
        ImageItem item = imageItemList.get(position);
        id = item.getImageId();
        getPresenter().delImage(TakeIDCardActivity.this, id);
        imageConfiguration.removeImage(item.getImageUrl());
    }

    private void setImage(File file) {
        String url = file.getPath().toString();
        if (id.equals("-1")) {
            getPresenter().saveImageUrl(TakeIDCardActivity.this, jobItem.getOrderid(), Constance.IMAGE_TYPE_CARD, url, productcode);
        } else {
            getPresenter().editImageUrl(TakeIDCardActivity.this, id, url);
        }
    }
}
