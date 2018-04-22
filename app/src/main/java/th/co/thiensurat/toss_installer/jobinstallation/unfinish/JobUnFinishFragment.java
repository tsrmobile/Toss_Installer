package th.co.thiensurat.toss_installer.jobinstallation.unfinish;


import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpFragment;
import th.co.thiensurat.toss_installer.detail.DetailActivity;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItemGroup;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.jobinstallation.unfinish.adapter.JobUnFinishAdapter;
import th.co.thiensurat.toss_installer.network.ConnectionDetector;
import th.co.thiensurat.toss_installer.stepview.StepViewActivity;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.MyApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobUnFinishFragment extends BaseMvpFragment<JobUnFinishInterface.Presenter>
        implements JobUnFinishInterface.View, JobUnFinishAdapter.ClickListener, WaveSwipeRefreshLayout.OnRefreshListener {

    private List<JobItem> jobItemList;
    private CustomDialog customDialog;
    private JobUnFinishAdapter adapter;
    private LinearLayoutManager layoutManager;

    public JobUnFinishFragment() {
        // Required empty public constructor
    }

    public static JobUnFinishFragment getInstance() {
        return new JobUnFinishFragment();
    }

    @Override
    public JobUnFinishInterface.Presenter createPresenter() {
        return JobUnFinishPresenter.create(getActivity());
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_job_un_finish;
    }

    @BindView(R.id.progress_view)
    CircularProgressView circularProgressView;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.textview_fail) TextView textViewFail;
    @BindView(R.id.layout_fail) RelativeLayout relativeLayoutFail;
    @BindView(R.id.waveSwipRefresh) WaveSwipeRefreshLayout waveSwipeRefreshLayout;
    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void setupInstance() {
        adapter = new JobUnFinishAdapter(getActivity());
        customDialog = new CustomDialog(getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
    }

    @Override
    public void setupView() {
        setRecyclerView();
        textViewFail.setVisibility(View.GONE);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            getPresenter().getJobUnFinish("unsuccess", MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID));
        }
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        waveSwipeRefreshLayout.setOnRefreshListener(this);
        waveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
    }

    @Override
    public void onRefresh() {
        boolean isNetworkAvailable = ConnectionDetector.isConnectingToInternet(getActivity());
        if (!isNetworkAvailable) {

        } else {
            getPresenter().getJobUnFinish("unsuccess", MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID));
        }
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
        circularProgressView.setVisibility(View.GONE);
        textViewFail.setVisibility(View.VISIBLE);
        textViewFail.setText(fail);
        relativeLayoutFail.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        waveSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onSuccess(String success) {
        customDialog.dialogSuccess(success);
    }

    @Override
    public void setJobItemToAdapter(List<JobItem> jobItemList) {
        this.jobItemList = jobItemList;
        waveSwipeRefreshLayout.setRefreshing(false);
        adapter.setJobUnFinishItem(jobItemList);
        adapter.setItemClick(this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        recyclerView.setVisibility(View.VISIBLE);
        relativeLayoutFail.setVisibility(View.GONE);
        circularProgressView.setVisibility(View.GONE);
    }

    @Override
    public void itemClick(View view, int position) {
        JobItem jobItem = jobItemList.get(position);

        AddressItemGroup addressItemGroup = new AddressItemGroup();
        addressItemGroup.setData(jobItem.getAddress());
        getPresenter().setProductToTable(jobItem.getOrderid(), jobItem.getProduct());

        if (getPresenter().checkStep(jobItem.getOrderid())) {
            Intent intent = new Intent(getActivity(), StepViewActivity.class);
            intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
            intent.putExtra(Constance.KEY_JOB_ADDR, addressItemGroup);
            startActivityForResult(intent, Constance.REQUEST_STEPVIEW);
        } else {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
            intent.putExtra(Constance.KEY_JOB_ADDR, addressItemGroup);
            startActivityForResult(intent, Constance.REQUEST_EDIT_DETAIL);
        }

    }
}
