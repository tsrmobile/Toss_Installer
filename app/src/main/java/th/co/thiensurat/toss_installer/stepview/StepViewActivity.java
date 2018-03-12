package th.co.thiensurat.toss_installer.stepview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Body;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.contract.ContractActivity;
import th.co.thiensurat.toss_installer.detail.DetailActivity;
import th.co.thiensurat.toss_installer.installation.InstallationActivity;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItemGroup;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.stepview.adapter.StepViewAdapter;
import th.co.thiensurat.toss_installer.takepicture.TakePictureActivity;
import th.co.thiensurat.toss_installer.takepicturecard.TakeIDCardActivity;
import th.co.thiensurat.toss_installer.takepicturecheckin.MapCheckinActivity;
import th.co.thiensurat.toss_installer.takepicturhome.TakeHomeActivity;
import th.co.thiensurat.toss_installer.utils.Constance;

public class StepViewActivity extends BaseMvpActivity<StepViewInterface.Presenter>
        implements StepViewInterface.View, StepViewAdapter.ClickListerner {

    private JobItem jobItem;
    private TextView textViewTitle;
    private StepViewAdapter adapter;
    private AddressItemGroup addressItemGroup;
    private LinearLayoutManager layoutManager;

    @Override
    public StepViewInterface.Presenter createPresenter() {
        return StepViewPresenter.create(StepViewActivity.this);
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_step_view;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupInstance() {
        adapter = new StepViewAdapter(StepViewActivity.this);
        layoutManager = new LinearLayoutManager(StepViewActivity.this);
    }

    @Override
    public void setupView() {
        setToolbar();
        setRecyclerView();
    }

    @Override
    public void initialize() {
        getDataFromIntent();
        if (getPresenter().checkStep(jobItem.getOrderid())) {
            getPresenter().getStepValue(jobItem.getOrderid());
        } else {
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
            intent.putExtra(Constance.KEY_JOB_ADDR, addressItemGroup);
            startActivity(intent);
        }
    }

    private void getDataFromIntent() {
        jobItem = getIntent().getParcelableExtra(Constance.KEY_JOB_ITEM);
        addressItemGroup = getIntent().getParcelableExtra(Constance.KEY_JOB_ADDR);
    }

    private void setToolbar() {
        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textViewTitle.setText("เลือกขั้นตอนการติดตั้ง");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void setValueToAdapter(List<String> strings) {
        adapter.setValueAdapter(strings);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setClickListerner(this);
    }

    @Override
    public void clickListener(View view, int position) {
        Intent intent = null;
        switch (position) {
            case 0:
                intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
                intent.putExtra(Constance.KEY_JOB_ADDR, addressItemGroup);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(getApplicationContext(), InstallationActivity.class);
                intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(getApplicationContext(), TakePictureActivity.class);
                intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(getApplicationContext(), TakeIDCardActivity.class);
                intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(getApplicationContext(), TakeHomeActivity.class);
                intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
                startActivity(intent);
                break;
            case 5:
                intent = new Intent(getApplicationContext(), MapCheckinActivity.class);
                intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
                startActivity(intent);
                break;
            case 6:
                intent = new Intent(getApplicationContext(), ContractActivity.class);
                intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
                startActivity(intent);
                break;
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

