package th.co.thiensurat.toss_installer.payment.paymentlist;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import th.co.thiensurat.toss_installer.base.BaseMvpFragment;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItemGroup;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItemGroup;
import th.co.thiensurat.toss_installer.network.ConnectionDetector;
import th.co.thiensurat.toss_installer.payment.detail.CustomerPaymentDetailActivity;
import th.co.thiensurat.toss_installer.payment.paymentitem.PaymentItemActivity;
import th.co.thiensurat.toss_installer.payment.paymentlist.adapter.PaymentAdapter;
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
public class PaymentFragment extends BaseMvpFragment<PaymentInterface.Presenter>
        implements PaymentInterface.View, WaveSwipeRefreshLayout.OnRefreshListener, OnStartDragListener
        , OnCustomerListChangedListener, PaymentAdapter.ClickListener {

    private GPSTracker gps;
    private CustomDialog customDialog;
    private PaymentAdapter adapter;
    private ItemTouchHelper itemTouchHelper;
    private LinearLayoutManager layoutManager;

    private String origins;
    private double latitude;
    private double longitude;
    private List<JobItem> jobItemList;

    public PaymentFragment() {
        // Required empty public constructor
    }

    public static PaymentFragment getInstance() {
        return new PaymentFragment();
    }

    @Override
    public PaymentInterface.Presenter createPresenter() {
        return PaymentPresenter.create(getActivity());
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_payment;
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
        adapter = new PaymentAdapter(getActivity(), PaymentFragment.this, PaymentFragment.this);
    }

    @Override
    public void setupView() {
        setRecyclerView();
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
            //getPresenter().getCurrentJobLocalDB();
        } else {
            getPresenter().requestJobPayment("firstpayment", MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID));
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
        getPresenter().requestJobPayment("firstpayment", MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID));
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
        ActivityResultBus.getInstance().postQueue(new ActivityResultEvent(requestCode, resultCode, data));
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
    public void setJobPaymentToAdapter(List<JobItem> ItemList) {
        jobItemList = new ArrayList<>();
        jobItemList = getOrderItem(ItemList);

        adapter.setJobItemList(jobItemList, origins);
        recyclerView.setAdapter(adapter);
        adapter.setItemClick(this);

        recyclerView.setVisibility(View.VISIBLE);
        relativeLayoutFail.setVisibility(View.GONE);
        circularProgressView.setVisibility(View.GONE);


        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter.stopThread();
        waveSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onNoteListChanged(List<JobItem> jobItemList) {
        List<String> listOfSortedCustomerId = new ArrayList<String>();
        for (JobItem jobItem : jobItemList) {
            listOfSortedCustomerId.add(jobItem.getOrderid());
        }

        Gson gson = new Gson();
        String jsonListOfSortedCustomerIds = gson.toJson(listOfSortedCustomerId);

        MyApplication.getInstance().getPrefManager().setPreferrence(Constance.KEY_SORT_PAYMENT_ID, jsonListOfSortedCustomerIds);
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

        ProductItemGroup productItemGroup = new ProductItemGroup();
        productItemGroup.setProduct(jobItem.getProduct());

        if (jobItem.getProduct().size() > 1) {
            Intent intent = new Intent(getActivity(), PaymentItemActivity.class);
            intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
            intent.putExtra(Constance.KEY_JOB_ADDR, addressItemGroup);
            intent.putExtra(Constance.KEY_JOB_PRODUCT, productItemGroup);
            startActivityForResult(intent, Constance.REQUEST_PAYMENT_ITEM_LIST);
        } else {
            MyApplication.getInstance().getPrefManager().setPreferrence("code", jobItem.getProduct().get(0).getProductCode());
            Intent intent = new Intent(getActivity(), CustomerPaymentDetailActivity.class);
            //Intent intent = new Intent(getActivity(), PaymentItemActivity.class);
            intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
            intent.putExtra(Constance.KEY_JOB_ADDR, addressItemGroup);
            intent.putExtra(Constance.KEY_JOB_PRODUCT, productItemGroup);
            startActivityForResult(intent, Constance.REQUEST_PAYMENT_DETAIL);
        }

    }

    @Override
    public void callLocal(View view, int position) {

    }

    @Override
    public void callMobile(View view, int position) {

    }

    private List<JobItem> getOrderItem(List<JobItem> jobItemList) {
        List<JobItem> jobItems = jobItemList;
        List<JobItem> sortedJob = new ArrayList<JobItem>();

        try {
            String jsonListOfSortedJobItem = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_SORT_PAYMENT_ID);
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
