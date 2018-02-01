package th.co.thiensurat.toss_installer.jobinstallation.finish;


import android.content.Intent;
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
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpFragment;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.jobinstallation.finish.adapter.JobFinishAdapter;
import th.co.thiensurat.toss_installer.step.TimeLineActivity;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.MyApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobFinishFragment extends BaseMvpFragment<JobFinishInterface.Presenter>
        implements JobFinishInterface.View, SwipeRefreshLayout.OnRefreshListener, JobFinishAdapter.ClickListener {

    private JobFinishAdapter adapter;
    private CustomDialog customDialog;
    private LinearLayoutManager layoutManager;

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
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
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
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorAccent,
                R.color.Purple);
    }

    @Override
    public void onRefresh() {
        getPresenter().getJobFinish("success", MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID));
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

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onSuccess(String success) {
        customDialog.dialogSuccess(success);
    }

    @Override
    public void setJobItemToAdapter(List<JobItem> jobItemList) {
        swipeRefreshLayout.setRefreshing(false);
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

    }
}
