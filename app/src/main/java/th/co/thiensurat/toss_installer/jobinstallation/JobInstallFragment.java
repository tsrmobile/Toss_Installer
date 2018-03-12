package th.co.thiensurat.toss_installer.jobinstallation;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.MainActivity;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpFragment;
import th.co.thiensurat.toss_installer.jobinstallation.adapter.JobViewAdapter;
import th.co.thiensurat.toss_installer.jobinstallation.unfinish.JobUnFinishFragment;
import th.co.thiensurat.toss_installer.utils.ActivityResultBus;
import th.co.thiensurat.toss_installer.utils.ActivityResultEvent;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobInstallFragment extends BaseMvpFragment<JobInstallInterface.Presenter> implements JobInstallInterface.View {

    private JobViewAdapter adapter;
    private CustomDialog customDialog;

    public JobInstallFragment() {
        // Required empty public constructor
    }

    public static JobInstallFragment getInstance() {
        return new JobInstallFragment();
    }

    @Override
    public JobInstallInterface.Presenter createPresenter() {
        return JobInstallPresenter.create(getActivity());
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_job_install;
    }

    @BindView(R.id.pager) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void setupInstance() {
        customDialog = new CustomDialog(getActivity());
        adapter = new JobViewAdapter(getFragmentManager());
    }

    @Override
    public void setupView() {
        ((MainActivity) getActivity()).setTitle("รายการงานติดตั้ง");
    }

    @Override
    public void initialize() {
        getPresenter().getAllJob();
    }

    private void setTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("รายการติดตั้งปัจจุบัน"));
        tabLayout.addTab(tabLayout.newTab().setText("รายการติดตั้งคงค้าง"));
        tabLayout.addTab(tabLayout.newTab().setText("รายการติดตั้งสมบูรณ์"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
    public void onLoading() {
        customDialog.dialogLoading();
    }

    @Override
    public void onFail(String fail) {
        customDialog.dialogFail(fail);
    }

    @Override
    public void onDimiss() {
        customDialog.dialogDimiss();
    }

    @Override
    public void onSuccess() {
        setTabLayout();
    }
}
