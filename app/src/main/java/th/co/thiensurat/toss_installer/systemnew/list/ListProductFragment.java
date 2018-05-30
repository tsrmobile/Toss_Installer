package th.co.thiensurat.toss_installer.systemnew.list;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpFragment;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.systemnew.detail.NewDetailActivity;
import th.co.thiensurat.toss_installer.systemnew.list.adapter.ListProductAdapter;
import th.co.thiensurat.toss_installer.utils.ActivityResultBus;
import th.co.thiensurat.toss_installer.utils.ActivityResultEvent;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.MyApplication;

public class ListProductFragment extends BaseMvpFragment<ListProductInterface.Presenter>
        implements ListProductInterface.View, ListProductAdapter.ClickListener {

    private TextView textViewTitle;
    private CustomDialog customDialog;
    private ListProductAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<JobItem> jobItemList;

    public static ListProductFragment getInstance() {
        return new ListProductFragment();
    }

    @Override
    public ListProductInterface.Presenter createPresenter() {
        return ListProductPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_list_product;
    }

    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.textview_no_job) TextView textViewNoJob;
    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void setupInstance() {
        adapter = new ListProductAdapter(getActivity());
        customDialog = new CustomDialog(getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
    }

    @Override
    public void setupView() {
        setRecyclerView();
    }

    @Override
    public void initialize() {
        getJob();
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
        textViewNoJob.setVisibility(View.VISIBLE);
        textViewNoJob.setText(fail);
    }

    @Override
    public void onSuccess(String success) {

    }

    private void getJob() {
        if (MyApplication.getInstance().getPrefManager().getPreferrence("choice").equals("install")) {
            getPresenter().getList("install", MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID));
        } else if (MyApplication.getInstance().getPrefManager().getPreferrence("choice").equals("delivery")) {
            getPresenter().getList("send", MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID));
        }
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void setListToAdapter(List<JobItem> jobItems) {
        textViewNoJob.setVisibility(View.GONE);
        this.jobItemList = jobItems;
        adapter.setJobItemList(jobItemList);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        JobItem jobItem = jobItemList.get(position);
        Intent intent = new Intent(getActivity(), NewDetailActivity.class);
        intent.putExtra(Constance.KEY_ORDER_ID, jobItem.getOrderid());
        startActivityForResult(intent, Constance.REQUEST_NEW_DETAIL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constance.REQUEST_NEW_DETAIL) {
            if (resultCode == getActivity().RESULT_OK) {
                getJob();
            }
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
}
