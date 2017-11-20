package th.co.thiensurat.toss_installer.installation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import th.co.thiensurat.toss_installer.utils.MyApplication;

public class InstallationActivity extends BaseMvpActivity<InstallationInterface.Presenter>
        implements InstallationInterface.View, InstallationAdapter.ClickListener  {

    private String id;
    private JobItem jobItem;
    private TextView textViewTitle;
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
        getPresenter().getProductDetail(InstallationActivity.this, jobItem.getOrderid());
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
    }

    @Override
    public void refreshProduct() {
        getPresenter().getProductDetail(InstallationActivity.this, jobItem.getOrderid());
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
                getPresenter().updateProduct(InstallationActivity.this, id, result.getContents());
                for(ProductItem item : productItemList) {
                    if (item.getProductStatus().equals(Constance.PRODUCT_STATUS_READY)) {
                        relativeLayoutNext.setVisibility(View.VISIBLE);
                    } else {
                        relativeLayoutNext.setVisibility(View.GONE);
                        return;
                    }
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
                startActivityForResult(intent, Constance.REQUEST_TAKE_PICTURE);
            }
        };
    }

    @Override
    public void ClickedListener(View view, int position) {
        ProductItem item = productItemList.get(position);
        id = item.getProductID();
        IntentIntegrator integrator = new IntentIntegrator(InstallationActivity.this);
        integrator.setOrientationLocked(true);
        integrator.setCaptureActivity(CaptureActivityPortrait.class);
        integrator.initiateScan();
    }
}
