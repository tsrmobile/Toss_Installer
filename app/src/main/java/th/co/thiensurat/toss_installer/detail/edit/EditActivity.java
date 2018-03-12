package th.co.thiensurat.toss_installer.detail.edit;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.detail.edit.adapter.DetailTabAdapter;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItemGroup;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.utils.Constance;

public class EditActivity extends AppCompatActivity {

    private JobItem jobItem;
    private TextView textViewTitle;
    private DetailTabAdapter detailTabAdapter;
    private AddressItemGroup addressItemGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setUpView();
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;
    private void setUpView() {
        ButterKnife.bind(this);
        setToolbar();
        setTabLayout();
    }

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textViewTitle.setText("ตรวจสอบและแก้ไขข้อมูล");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("ที่อยู่ตามบัตรฯ"));
        tabLayout.addTab(tabLayout.newTab().setText("ที่อยู่ติดตั้ง"));
        tabLayout.addTab(tabLayout.newTab().setText("ที่อยู่เก็บเงิน"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setFillViewport(true);
        detailTabAdapter = new DetailTabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(detailTabAdapter);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            setResult(RESULT_CANCELED);
            finish();
        }
        return true;
    }
}
