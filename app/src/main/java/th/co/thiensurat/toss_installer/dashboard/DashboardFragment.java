package th.co.thiensurat.toss_installer.dashboard;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpFragment;
import th.co.thiensurat.toss_installer.dashboard.adapter.DashboardItemAdapter;
import th.co.thiensurat.toss_installer.dashboard.item.DashboardItem;
import th.co.thiensurat.toss_installer.dashboard.item.DashboardItemGroup;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.MyApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends BaseMvpFragment<DashboardInterface.Presenter>
        implements DashboardInterface.View, SwipeRefreshLayout.OnRefreshListener {

    private CustomDialog customDialog;
    private LinearLayoutManager layoutManager;
    private DashboardItemAdapter dashboardItemAdapter;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment getInstance() {
        return new DashboardFragment();
    }

    @Override
    public DashboardInterface.Presenter createPresenter() {
        return DashboardPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_dashbroad;
    }

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void setupInstance() {
        customDialog = new CustomDialog(getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        dashboardItemAdapter = new DashboardItemAdapter(getActivity());
    }

    @Override
    public void setupView() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorAccent,
                R.color.Purple);
    }

    @Override
    public void initialize() {
        getPresenter().requestSummary("summary", MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID));
    }

    @Override
    public void onRefresh() {
        getPresenter().requestSummary("summary", MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID));
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

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constance.STATE_DASHBOARD, getPresenter().getDashboardItemGroup());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        getPresenter().setDashboardItemGroup((DashboardItemGroup) savedInstanceState.getParcelable(Constance.STATE_DASHBOARD));
    }

    @Override
    public void restoreView(Bundle savedInstanceState) {
        super.restoreView(savedInstanceState);
        getPresenter().setDashboardItemToAdapter(getPresenter().getDashboardItemGroup());
    }

    @Override
    public void setDashboardToAdapter(List<DashboardItem> dashboardItemList) {
        swipeRefreshLayout.setRefreshing(false);
        dashboardItemAdapter.setDashboardList(dashboardItemList);
        recyclerView.setAdapter(dashboardItemAdapter);
        dashboardItemAdapter.notifyDataSetChanged();
    }
}
