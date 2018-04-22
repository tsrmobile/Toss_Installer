package th.co.thiensurat.toss_installer.jobinstallation.current;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.auth.AuthActivity;
import th.co.thiensurat.toss_installer.base.BaseMvpFragment;
import th.co.thiensurat.toss_installer.detail.DetailActivity;
import th.co.thiensurat.toss_installer.jobinstallation.current.adapter.CurrentJobAdapter;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItemGroup;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItemGroup;
import th.co.thiensurat.toss_installer.network.ConnectionDetector;
import th.co.thiensurat.toss_installer.utils.ActivityResultBus;
import th.co.thiensurat.toss_installer.utils.ActivityResultEvent;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.GPSTracker;
import th.co.thiensurat.toss_installer.utils.MyApplication;
import th.co.thiensurat.toss_installer.utils.helper.OnCustomerListChangedListener;
import th.co.thiensurat.toss_installer.utils.helper.OnStartDragListener;
import th.co.thiensurat.toss_installer.utils.helper.SimpleItemTouchHelperCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentJobFragment extends BaseMvpFragment<CurrentJobInterface.Presenter>
        implements CurrentJobInterface.View, WaveSwipeRefreshLayout.OnRefreshListener, OnStartDragListener
        , OnCustomerListChangedListener, CurrentJobAdapter.ClickListener {

    private GPSTracker gps;
    private String origins;
    private double latitude;
    private double longitude;
    private Location location;

    private List<JobItem> jobItemList;
    private CurrentJobAdapter adapter;
    private CustomDialog customDialog;
    private ItemTouchHelper itemTouchHelper;
    private LinearLayoutManager layoutManager;

    public CurrentJobFragment() {
        // Required empty public constructor
    }

    public static CurrentJobFragment getInstance() {
        return new CurrentJobFragment();
    }

    @Override
    public CurrentJobInterface.Presenter createPresenter() {
        return CurrentJobPresenter.create(getActivity());
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_current_job;
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
        checkLocationPermission();
        gps = new GPSTracker(getActivity());
        customDialog = new CustomDialog(getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new CurrentJobAdapter(getActivity(), CurrentJobFragment.this, CurrentJobFragment.this);
    }

    @Override
    public void setupView() {
        setRecyclerView();
        textViewFail.setVisibility(View.GONE);
    }

    @Override
    public void initialize() {
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            origins = String.valueOf(latitude) + "," + String.valueOf(longitude);

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},  Constance.REQUEST_LOCATION);
        }

        boolean isNetworkAvailable = ConnectionDetector.isConnectingToInternet(getActivity());
        if (!isNetworkAvailable) {
            getPresenter().getCurrentJobLocalDB();
        } else {
            getPresenter().getCurrentJob("job", MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID));
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
            getPresenter().getCurrentJobLocalDB();
        } else {
            getPresenter().getCurrentJob("job", MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID));
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
    public void setJobItemToAdapter(List<JobItem> ItemList) {
        jobItemList = new ArrayList<>();
        jobItemList = getOrderItem(ItemList);
        waveSwipeRefreshLayout.setRefreshing(false);

        adapter.setJobItemList(jobItemList, origins);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setItemClick(this);

        recyclerView.setVisibility(View.VISIBLE);
        relativeLayoutFail.setVisibility(View.GONE);
        circularProgressView.setVisibility(View.GONE);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter.stopThread();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void itemClick(View view, int position) {
        JobItem jobItem = jobItemList.get(position);

        AddressItemGroup addressItemGroup = new AddressItemGroup();
        addressItemGroup.setData(jobItem.getAddress());

        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
        intent.putExtra(Constance.KEY_JOB_ADDR, addressItemGroup);
        startActivityForResult(intent, Constance.REQUEST_JOB_DETAIL);
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
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 10);
            return;
        } else {
            try {
                startActivity(callIntent);
            } catch (android.content.ActivityNotFoundException ex) {
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
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 10);
            return;
        } else {
            try {
                startActivity(callIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Log.e("call mobile", ex.getLocalizedMessage());
            }
        }
        getActivity().startActivityForResult(callIntent, Constance.REQUEST_CALL_PHONE);
    }

    @Override
    public void onNoteListChanged(List<JobItem> jobItemList) {
        List<String> listOfSortedCustomerId = new ArrayList<String>();
        for (JobItem jobItem : jobItemList) {
            listOfSortedCustomerId.add(jobItem.getOrderid());
        }

        Gson gson = new Gson();
        String jsonListOfSortedCustomerIds = gson.toJson(listOfSortedCustomerId);

        MyApplication.getInstance().getPrefManager().setPreferrence(Constance.KEY_SORT_ID, jsonListOfSortedCustomerIds);
    }

    private List<JobItem> getOrderItem(List<JobItem> jobItemList) {
        List<JobItem> jobItems = jobItemList;
        List<JobItem> sortedJob = new ArrayList<JobItem>();

        try {
            String jsonListOfSortedJobItem = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_SORT_ID);

            if (!jsonListOfSortedJobItem.isEmpty()) {
                Gson gson = new Gson();
                List<String> listOfSortedJobID = gson.fromJson(jsonListOfSortedJobItem,
                        new TypeToken<List<String>>() {
                        }.getType());

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

    @Override
    public void onStart() {
        super.onStart();
        ActivityResultBus.getInstance().register(mActivityResultSubscriber);
    }

    @Override
    public void onStop() {
        super.onStop();
        ActivityResultBus.getInstance().unregister(mActivityResultSubscriber);
    }

    private Object mActivityResultSubscriber = new Object() {
        @Subscribe
        public void onActivityResultReceived(ActivityResultEvent event) {
            int requestCode = event.getRequestCode();
            int resultCode = event.getResultCode();
            Intent data = event.getData();
            onActivityResult(requestCode, resultCode, data);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constance.REQUEST_JOB_DETAIL) {
            onRefresh();
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                }
            } else {
                customDialog.dialogFail("Permission denied");
            }
        }
    }
}
