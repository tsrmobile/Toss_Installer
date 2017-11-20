package th.co.thiensurat.toss_installer.job;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.MainActivity;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpFragment;
import th.co.thiensurat.toss_installer.detail.DetailActivity;
import th.co.thiensurat.toss_installer.job.adapter.JobAdapter;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.job.item.JobItemGroup;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.MyApplication;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;
import th.co.thiensurat.toss_installer.utils.helper.OnCustomerListChangedListener;
import th.co.thiensurat.toss_installer.utils.helper.OnStartDragListener;
import th.co.thiensurat.toss_installer.utils.helper.SimpleItemTouchHelperCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobFragment extends BaseMvpFragment<JobInterface.Presenter>
        implements JobInterface.View, OnStartDragListener, SwipeRefreshLayout.OnRefreshListener,
        JobAdapter.ClickListener, OnCustomerListChangedListener {

    private JobAdapter adapter;
    private CustomDialog customDialog;
    private ItemTouchHelper itemTouchHelper;
    private LinearLayoutManager layoutManager;
    private List<JobItem> jobItemList;

    public JobFragment() {
        // Required empty public constructor
    }

    public static JobFragment getInstance() {
        return new JobFragment();
    }

    @Override
    public JobInterface.Presenter createPresenter() {
        return JobPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_job;
    }

    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.layout_fail) RelativeLayout relativeLayoutFail;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);

    }

    @Override
    public void setupInstance() {
        adapter = new JobAdapter(getActivity(), JobFragment.this, JobFragment.this);
        customDialog = new CustomDialog(getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
    }

    @Override
    public void setupView() {
        ((MainActivity) getActivity()).setTitle("รายการงานติดตั้ง");
        setRecyclerView();
    }

    @Override
    public void initialize() {
        getPresenter().Jobrequest("job", MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID));
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
        getPresenter().Jobrequest("job", MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID));
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
        //customDialog.dialogFail(fail);
        relativeLayoutFail.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(String success) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constance.STATE_JOB, getPresenter().getJobItemGroup());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        getPresenter().setJobItemGroup((JobItemGroup) savedInstanceState.getParcelable(Constance.STATE_JOB));
    }

    @Override
    public void restoreView(Bundle savedInstanceState) {
        super.restoreView(savedInstanceState);
        getPresenter().setJobItemToAdapter(getPresenter().getJobItemGroup());
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void setJobItemToAdapter(List<JobItem> itemList) {
        this.jobItemList = getOrderItem(itemList);
        swipeRefreshLayout.setRefreshing(false);
        adapter.setJobList(jobItemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setItemClick(this);

        getPresenter().insertDataToSQLite(getActivity(), jobItemList);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void itemClick(View view, int position) {
        JobItem jobItem = jobItemList.get(position);
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
        getActivity().startActivityForResult(intent, Constance.REQUEST_JOB_DETAIL);
    }

    @Override
    public void callLocal(View view, int position) {
        JobItem item = jobItemList.get(position);
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        for (AddressItem addressItem : item.getAddress()) {
            if (addressItem.getAddressType().equals("AddressInstall")) {
                callIntent.setData(Uri.parse("tel:" + addressItem.getPhone()));
            }
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //request permission from user if the app hasn't got the required permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                    10);
            return;
        }else {     //have got permission
            try{
                startActivity(callIntent);  //call activity and make phone call
            }
            catch (android.content.ActivityNotFoundException ex){
               Log.e("call phone", ex.getLocalizedMessage());
            }
        }
        getActivity().startActivityForResult(callIntent, Constance.REQUEST_CALL_PHONE);
    }

    @Override
    public void callMobile(View view, int position) {
        JobItem item = jobItemList.get(position);
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        for (AddressItem addressItem : item.getAddress()) {
            if (addressItem.getAddressType().equals("AddressInstall")) {
                callIntent.setData(Uri.parse("tel:" + addressItem.getMobile()));
            }
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //request permission from user if the app hasn't got the required permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                    10);
            return;
        }else {     //have got permission
            try{
                startActivity(callIntent);  //call activity and make phone call
            }
            catch (android.content.ActivityNotFoundException ex){
                Log.e("call mobile", ex.getLocalizedMessage());
            }
        }
        getActivity().startActivityForResult(callIntent, Constance.REQUEST_CALL_PHONE);
    }

    @Override
    public void onNoteListChanged(List<JobItem> jobItemList) {
        List<String> listOfSortedCustomerId = new ArrayList<String>();
        for (JobItem jobItem: jobItemList){
            listOfSortedCustomerId.add(jobItem.getOrderid());
        }

        Gson gson = new Gson();
        String jsonListOfSortedCustomerIds = gson.toJson(listOfSortedCustomerId);

        MyApplication.getInstance().getPrefManager().setPreferrence(Constance.KEY_SORT_ID, jsonListOfSortedCustomerIds);
    }

    private List<JobItem> getOrderItem(List<JobItem> jobItemList){

        List<JobItem> jobItems = jobItemList;
        List<JobItem> sortedJob = new ArrayList<JobItem>();

        try {
            String jsonListOfSortedJobItem = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_SORT_ID);

            if (!jsonListOfSortedJobItem.isEmpty()) {
                Gson gson = new Gson();
                List<String> listOfSortedJobID = gson.fromJson(jsonListOfSortedJobItem,
                        new TypeToken<List<String>>() {}.getType());

                if (listOfSortedJobID != null && listOfSortedJobID.size() > 0) {
                    for (String id : listOfSortedJobID) {
                        for (JobItem jobItem : jobItems) {
                            if (jobItem.getOrderid().equals(id)) {
                                sortedJob.add(jobItem);
                                jobItems.remove(jobItem);
                                break;
                            }
                        }
                    }
                }
                if (jobItems.size() > 0) {
                    sortedJob.addAll(jobItems);
                }
                return sortedJob;
            } else {
                return jobItems;
            }
        } catch (Exception ex) {
            return jobItems;
        }
    }
}
