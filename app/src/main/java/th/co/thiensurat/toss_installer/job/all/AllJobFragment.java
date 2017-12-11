package th.co.thiensurat.toss_installer.job.all;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.MainActivity;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpFragment;
import th.co.thiensurat.toss_installer.contract.signaturepad.SignatureActivity;
import th.co.thiensurat.toss_installer.detail.DetailActivity;
import th.co.thiensurat.toss_installer.installation.step.StepViewActivity;
import th.co.thiensurat.toss_installer.job.all.adapter.AllJobAdapter;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllJobFragment extends BaseMvpFragment<AllJobInterface.Presenter>
        implements AllJobInterface.View, AllJobAdapter.ClickListener {

    private AllJobAdapter adapter;
    private CustomDialog customDialog;
    private LinearLayoutManager layoutManager;
    private List<JobItem> jobItemList = new ArrayList<JobItem>();

    public AllJobFragment() {
        // Required empty public constructor
    }

    public static AllJobFragment getInstance() {
        return new AllJobFragment();
    }

    @Override
    public AllJobInterface.Presenter createPresenter() {
        return AllJobPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_all_job;
    }

    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void setupInstance() {
        adapter = new AllJobAdapter(getActivity());
        customDialog = new CustomDialog(getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
    }

    @Override
    public void setupView() {
        ((MainActivity) getActivity()).setTitle("รายการงานติดตั้งทั้งหมด");
        setRecyclerView();
    }

    @Override
    public void initialize() {
        getPresenter().getJobFromSqlite(getActivity());
    }


    @Override
    public void setJobToCalendar(List<JobItem> itemList) {
        this.jobItemList = itemList;
        adapter.setJobItemList(jobItemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setClickListener(this);
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void itemClick(View view, int position) {
        JobItem jobItem = jobItemList.get(position);
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
        getActivity().startActivityForResult(intent, Constance.REQUEST_JOB_DETAIL);
    }
}
