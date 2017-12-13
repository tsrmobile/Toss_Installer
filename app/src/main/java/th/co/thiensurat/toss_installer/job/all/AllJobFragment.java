package th.co.thiensurat.toss_installer.job.all;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.github.badoualy.datepicker.DatePickerTimeline;
import com.github.badoualy.datepicker.MonthView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mahendra.calendarview.widget.CalendarView;
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
import th.co.thiensurat.toss_installer.utils.Utils;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllJobFragment extends BaseMvpFragment<AllJobInterface.Presenter>
        implements AllJobInterface.View, AllJobAdapter.ClickListener {

    private String selectedDate;
    private String selectedMonth;
    private AllJobAdapter adapter;
    private CustomDialog customDialog;
    private LinearLayoutManager layoutManager;
    private List<JobItem> jobItemList = new ArrayList<JobItem>();

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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

    @BindView(R.id.layout_fail) RelativeLayout relativeLayoutFail;
    @BindView(R.id.timeline) DatePickerTimeline datePickerTimeline;
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
        setDatePickerTimeline();
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

        if (jobItemList.size() == 0) {
            relativeLayoutFail.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            relativeLayoutFail.setVisibility(View.GONE);
        }
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setDatePickerTimeline() {
        datePickerTimeline.setDateLabelAdapter(new MonthView.DateLabelAdapter() {
            @Override
            public CharSequence getLabel(Calendar calendar, int index) {
                int y = calendar.get(Calendar.YEAR);
                int m = calendar.get(Calendar.MONTH) + 1;
                int d = calendar.get(Calendar.DATE);
                String strM, strD;
                if (m < 10) {
                    strM = "0" + m;
                } else {
                    strM = String.valueOf(m);
                }

                if (d < 10) {
                    strD = "0" + d;
                } else {
                    strD = String.valueOf(d);
                }

                return Utils.ConvertMonthThaiCharacter(String.valueOf(y + "-" + strM + "-" + strD));
            }
        });

        datePickerTimeline.setOnDateSelectedListener(new DatePickerTimeline.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int index) {
                month = month + 1;
                if (month < 10) {
                    selectedMonth = "0" + month;
                } else {
                    selectedMonth = String.valueOf(month);
                }

                if (day < 10) {
                    selectedDate = "0" + day;
                } else {
                    selectedDate = String.valueOf(day);
                }
                String date = String.valueOf((year + "-" + selectedMonth + "-" + selectedDate));
                getPresenter().getJobByDate(getActivity(), date);
                Toast.makeText(getActivity(), date, Toast.LENGTH_LONG).show();
            }
        });

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        datePickerTimeline.setFirstVisibleDate(year, month - 2 , 1);
        datePickerTimeline.setSelectedDate(year, month, day);
        datePickerTimeline.setFollowScroll(true);
        datePickerTimeline.centerOnSelection();
    }

    @Override
    public void itemClick(View view, int position) {
        JobItem jobItem = jobItemList.get(position);
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
        getActivity().startActivityForResult(intent, Constance.REQUEST_JOB_DETAIL);
    }
}
