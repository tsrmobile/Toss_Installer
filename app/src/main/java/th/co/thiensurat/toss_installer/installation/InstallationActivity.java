package th.co.thiensurat.toss_installer.installation;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.MainActivity;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.installation.adapter.InstallationAdapter;
import th.co.thiensurat.toss_installer.installation.camera.CaptureActivityPortrait;
import th.co.thiensurat.toss_installer.jobinstallation.item.ConvertItemToGroup;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItemGroup;
import th.co.thiensurat.toss_installer.takepicture.TakePictureActivity;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.MyApplication;

public class InstallationActivity extends BaseMvpActivity<InstallationInterface.Presenter>
        implements InstallationInterface.View, InstallationAdapter.ClickListener  {

    private String id;
    private String serial = "000";
    private JobItem jobItem;

    private TextView textViewTitle;
    private CustomDialog customDialog;
    private InstallationAdapter adapter;
    private LinearLayoutManager layoutManager;

    private String productcode;
    private ProductItemGroup productItemGroup;
    private List<ProductItem> productItemList;

    @Override
    public InstallationInterface.Presenter createPresenter() {
        return InstallationPresenter.create(InstallationActivity.this);
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_installation;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.layoutNext) RelativeLayout relativeLayoutNext;
    @BindView(R.id.button_next) Button buttonNext;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
        buttonNext.setOnClickListener( onNext() );
    }

    @Override
    public void setupInstance() {
        customDialog = new CustomDialog(InstallationActivity.this);
        adapter = new InstallationAdapter(InstallationActivity.this);
        layoutManager = new LinearLayoutManager(InstallationActivity.this);
    }

    @Override
    public void setupView() {
        setToolbar();
        setRecyclerView();
    }

    @Override
    public void initialize() {
        getDataFromIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public void onLoad() {
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
        Log.e("success", success);
        getPresenter().updateProduct(id, success);
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void setProductDetail(List<ProductItem> productList) {
        this.productItemList = productList;
        adapter.setInstallItemList(productItemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setClickListener(this);
        adapter.setLongClickListener(this);

        for (ProductItem item : productItemList) {
            if (item.getProductStatus().equals(Constance.PRODUCT_STATUS_READY)) {
                serial = item.getProductSerial();
                relativeLayoutNext.setVisibility(View.VISIBLE);
            } else {
                relativeLayoutNext.setVisibility(View.GONE);
                return;
            }
        }
    }

    @Override
    public void refreshProduct() {
        if (getPresenter().checkItem()) {
            getPresenter().getProductDetail(jobItem.getOrderid());
        } else {
            customDialog.dialogWarning("กรุณาเบิกสินค้า\nก่อนทำการติดตั้ง");
            return;
        }
    }

    private void setToolbar() {
        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textViewTitle.setText("ติดตั้งสินค้า");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getDataFromIntent() {
        jobItem = getIntent().getParcelableExtra(Constance.KEY_JOB_ITEM);
        //productItemGroup = getIntent().getParcelableExtra(Constance.KEY_JOB_PRODUCT);
        if (getPresenter().checkItem()) {
            getPresenter().getProductDetail(jobItem.getOrderid());
        } else {
            customDialog.dialogWarning("กรุณาเบิกสินค้า\nก่อนทำการติดตั้ง");
        }
        /*try {
            if (jobItem.getStatus().equals("22")) {
                getPresenter().getProductDetail(jobItem.getOrderid());
            } else {
                if (getPresenter().checkItem()) {
                    getPresenter().getProductDetail(jobItem.getOrderid());
                } else {
                    customDialog.dialogWarning("กรุณาเบิกสินค้า\nก่อนทำการติดตั้ง");
                }
            }
        } catch (Exception e) {
            Log.e("installation", e.getMessage() + "\n" + jobItem.getStatus());
            if (getPresenter().checkItem()) {
                getPresenter().getProductDetail(jobItem.getOrderid());
            } else {
                customDialog.dialogWarning("กรุณาเบิกสินค้า\nก่อนทำการติดตั้ง");
            }
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            serial = result.getContents();
            MyApplication.getInstance().getPrefManager().setPreferrence(Constance.KEY_SERIAL, serial);
            if(result.getContents() == null) {
                relativeLayoutNext.setVisibility(View.GONE);
            } else {
                if (getPresenter().checkSerial(MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_SERIAL),
                        productcode)) {
                    getPresenter().updateSerialToServer(
                            jobItem.getOrderid(),
                            MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_PRODUCT_CODE),
                            MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_SERIAL));
                    for (ProductItem item : productItemList) {
                        if (item.getProductStatus().equals(Constance.PRODUCT_STATUS_READY)) {
                            relativeLayoutNext.setVisibility(View.VISIBLE);
                        } else {
                            relativeLayoutNext.setVisibility(View.GONE);
                            onNextStep();
                        }
                    }
                } else {
                    customDialog.dialogFail("รหัสสินค้าไม่ถูกต้อง!");
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(InstallationActivity.this, MainActivity.class);
            /*intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            setResult(RESULT_OK, intent);*/
            startActivity(intent);
            finish();
        } else if (item.getItemId() == R.id.menu_home) {
            Intent intent = new Intent(InstallationActivity.this, MainActivity.class);
            /*intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);*/
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
                onNextStep();
            }
        };
    }

    public void onNextStep() {
        Intent intent = new Intent(InstallationActivity.this, TakePictureActivity.class);
        intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
        startActivityForResult(intent, Constance.REQUEST_TAKE_PICTURE);
    }

    @Override
    public void ClickedListener(View view, int position) {
        ProductItem item = productItemList.get(position);
        id = item.getProductID();
        productcode = item.getProductCode();
        MyApplication.getInstance().getPrefManager().setPreferrence(Constance.KEY_PRODUCT_CODE, item.getProductCode());
        if (item.getProductStatus().equals("พร้อมติดตั้ง")) {
            onNextStep();
        } else {
            IntentIntegrator integrator = new IntentIntegrator(InstallationActivity.this);
            integrator.setOrientationLocked(true);
            integrator.setCaptureActivity(CaptureActivityPortrait.class);
            integrator.initiateScan();
        }
    }

    @Override
    public void LongClickedListener(View view, int position) {
        ProductItem item = productItemList.get(position);
        id = item.getProductID();
        productcode = item.getProductCode();
        MyApplication.getInstance().getPrefManager().setPreferrence(Constance.KEY_PRODUCT_CODE, item.getProductCode());
        final CharSequence choice[] = new CharSequence[] {"สแกนใหม่", "พิมพ์สัญญา"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("เลือก");
        builder.setIcon(getResources().getDrawable(R.drawable.ic_info_outline_white_24dp));
        builder.setItems(choice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (String.valueOf(choice[which]).equals("สแกนใหม่")) {
                    IntentIntegrator integrator = new IntentIntegrator(InstallationActivity.this);
                    integrator.setOrientationLocked(true);
                    integrator.setCaptureActivity(CaptureActivityPortrait.class);
                    integrator.initiateScan();
                } else {

                }
            }
        });
        builder.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return false;
        }
        return super.dispatchKeyEvent(event);
    }
}
