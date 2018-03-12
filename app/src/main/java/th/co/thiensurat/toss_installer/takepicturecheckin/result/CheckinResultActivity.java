package th.co.thiensurat.toss_installer.takepicturecheckin.result;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.contract.ContractActivity;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.takepicture.adapter.TakePictureAdapter;
import th.co.thiensurat.toss_installer.takepicture.item.ImageItem;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;

public class CheckinResultActivity extends BaseMvpActivity<CheckinResultInterface.Presenter> implements CheckinResultInterface.View, TakePictureAdapter.ClickListener  {

    private TextView textViewTitle;

    private String serial;
    private JobItem jobItem;
    private TakePictureAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<ImageItem> imageItemList = new ArrayList<ImageItem>();

    @Override
    public CheckinResultInterface.Presenter createPresenter() {
        return CheckinResultPresenter.create(CheckinResultActivity.this);
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_checkin_result;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.layout_bottom) RelativeLayout relativeLayoutBottom;
    @BindView(R.id.button_next) Button buttonNext;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
        buttonNext.setOnClickListener( onNext() );
    }

    @Override
    public void setupInstance() {
        adapter = new TakePictureAdapter(CheckinResultActivity.this);
        layoutManager = new LinearLayoutManager(CheckinResultActivity.this);
    }

    @Override
    public void setupView() {
        setToolbar();
        setRecyclerView();
    }

    @Override
    public void initialize() {
        getDataFromIntent();
        getPresenter().getImage(jobItem.getOrderid(), Constance.IMAGE_TYPE_CHECKIN);
    }

    private void getDataFromIntent() {
        jobItem = getIntent().getParcelableExtra(Constance.KEY_JOB_ITEM);
    }

    private void setToolbar() {
        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textViewTitle.setText("รูปเช็คอิน");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        adapter.notifyDataSetChanged();
        adapter.setHideRemoveButton(View.GONE);
        adapter.setOnClickListener(this);
    }

    private View.OnClickListener onNext() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonNext.startAnimation(new AnimateButton().animbutton());
                getPresenter().updateStep(jobItem.getOrderid(), Constance.STEP_6);
                Intent intent = new Intent(CheckinResultActivity.this, ContractActivity.class);
                intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
                startActivityForResult(intent, Constance.REQUEST_PRINT_CONTRACT);
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClicked(View view, int position) {
        ImageItem item = imageItemList.get(position);
        Intent intent = new Intent();
        intent.putExtra("Image_ID", item.getImageId());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void delClicked(View view, int position) {

    }
}
