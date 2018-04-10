package th.co.thiensurat.toss_installer.jobinstallation.finish;


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
import th.co.thiensurat.toss_installer.contract.ContractActivity;
import th.co.thiensurat.toss_installer.jobinstallation.finish.adapter.JobFinishAdapter;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.network.ConnectionDetector;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.MyApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobFinishFragment extends BaseMvpFragment<JobFinishInterface.Presenter>
        implements JobFinishInterface.View, WaveSwipeRefreshLayout.OnRefreshListener, JobFinishAdapter.ClickListener {

    private JobFinishAdapter adapter;
    private CustomDialog customDialog;
    private LinearLayoutManager layoutManager;
    private List<JobItem> jobItemList;

    public JobFinishFragment() {
        // Required empty public constructor
    }

    public static JobFinishFragment getInstance() {
        return new JobFinishFragment();
    }

    @Override
    public JobFinishInterface.Presenter createPresenter() {
        return JobFinishPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_job_finish;
    }

    @BindView(R.id.progress_view) CircularProgressView circularProgressView;
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
        adapter = new JobFinishAdapter(getActivity());
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
            getPresenter().getJobFinish("success", MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID));
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
            getPresenter().getJobFinish("success", MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID));
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
        adapter.setJobItem(jobItemList);
        adapter.setItemClick(this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (jobItemList.size() == 0) {
            relativeLayoutFail.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        relativeLayoutFail.setVisibility(View.GONE);
    }

    @Override
    public void itemClick(View view, int position) {
        JobItem jobItem = jobItemList.get(position);
        Intent intent = new Intent(getActivity(), ContractActivity.class);
        intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
        getActivity().startActivityForResult(intent, Constance.REQUEST_PRINT_CONTRACT);
    }
}
