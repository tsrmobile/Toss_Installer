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
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.installation.adapter.InstallationAdapter;
import th.co.thiensurat.toss_installer.installation.camera.CaptureActivityPortrait;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.job.item.JobItemGroup;
import th.co.thiensurat.toss_installer.job.item.ProductItem;
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
    private String productcode;
    private TextView textViewTitle;
    private CustomDialog customDialog;
    private InstallationAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<ProductItem> productItemList;

    @Override
    public InstallationInterface.Presenter createPresenter() {
        return InstallationPresenter.create();
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
        if (getPresenter().checkItem(InstallationActivity.this)) {
            getPresenter().getProductDetail(InstallationActivity.this, jobItem.getOrderid());
        } else {
            customDialog.dialogWarning("กรุณาเบิกสินค้า\nก่อนทำการติดตั้ง");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.print_menu, menu);
        return true;
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
        if (getPresenter().checkItem(InstallationActivity.this)) {
            getPresenter().getProductDetail(InstallationActivity.this, jobItem.getOrderid());
        } else {
            customDialog.dialogWarning("กรุณาเบิกสินค้า\nก่อนทำการติดตั้ง");
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                relativeLayoutNext.setVisibility(View.GONE);
            } else {
                Log.e("Product serial", result.getContents());
                if (getPresenter().checkSerial(InstallationActivity.this, result.getContents(), productcode)) {
                    getPresenter().updateProduct(InstallationActivity.this, id, result.getContents());
                    for (ProductItem item : productItemList) {
                        if (item.getProductStatus().equals(Constance.PRODUCT_STATUS_READY)) {
                            /*relativeLayoutNext.setVisibility(View.VISIBLE);
                        } else {
                            relativeLayoutNext.setVisibility(View.GONE);
                            return;
                            if (jobItem.getOrderid().startsWith("G")) {
                                Log.e("Order id", "G");
                            }*/
                            /*if (getPresenter().checkPackageInstall(InstallationActivity.this, jobItem.getOrderid(), productcode)) {

                            }*/
                            //onNextStep();
                            getPresenter().getProductDetail(InstallationActivity.this, jobItem.getOrderid());
                        }
                    }
                } else {
                    serial = "000";
                    customDialog.dialogFail("serial สินค้าไม่ถูกต้อง!");
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (item.getItemId() == R.id.menu_print) {

        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener onNext() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonNext.startAnimation(new AnimateButton().animbutton());
                Intent intent = new Intent(getApplicationContext(), TakePictureActivity.class);
                intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
                intent.putExtra(Constance.KEY_SERIAL_ITEM, serial);
                startActivityForResult(intent, Constance.REQUEST_TAKE_PICTURE);
            }
        };
    }

    public void onNextStep() {
        Intent intent = new Intent(getApplicationContext(), TakePictureActivity.class);
        intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
        intent.putExtra(Constance.KEY_SERIAL_ITEM, serial);
        intent.putExtra(Constance.KEY_PRODUCT_CODE, productcode);
        startActivityForResult(intent, Constance.REQUEST_TAKE_PICTURE);
    }

    @Override
    public void ClickedListener(View view, int position) {
        ProductItem item = productItemList.get(position);
        id = item.getProductID();
        productcode = item.getProductCode();
        serial = item.getProductSerial();

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
        final CharSequence choice[] = new CharSequence[] {"สแกนใหม่", "พิมพ์สัญญา"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("เลือกรายการ");
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
