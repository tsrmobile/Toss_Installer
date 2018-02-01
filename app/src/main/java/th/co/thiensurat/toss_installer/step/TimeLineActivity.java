package th.co.thiensurat.toss_installer.step;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.step.adapter.TimelineAdapter;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineActivity extends AppCompatActivity implements TimelineAdapter.ClickListener {

    private TextView textViewTitle;
    private TimelineAdapter adapter;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        setToolbar();
        initView();

    }

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textViewTitle.setText(getResources().getString(R.string.app_name));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        adapter = new TimelineAdapter(TimeLineActivity.this);
        layoutManager = new LinearLayoutManager(TimeLineActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        List<String> stringList = new ArrayList<String>();
        stringList.add("สแกนสินค้า");
        stringList.add("ตรวจสอบและแก้ไขข้อมูล");
        stringList.add("ถ่ายรูปการติดตั้ง");
        stringList.add("ถ่ายรูปบัตรประชาชน");
        stringList.add("ถ่ายรูปที่อยู่อาศัย");
        stringList.add("เช็คอินตำแหน่ง");
        stringList.add("พิมพ์สัญญ");

        adapter.setTimelineItem(stringList);
        adapter.notifyDataSetChanged();

        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
    }

    @Override
    public void stepClick(View view, int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(TimeLineActivity.this, null));
                break;
            case 1:
                startActivity(new Intent(TimeLineActivity.this, null));
                break;
            case 2:
                startActivity(new Intent(TimeLineActivity.this, null));
                break;
            case 3:
                startActivity(new Intent(TimeLineActivity.this, null));
                break;
            case 4:
                startActivity(new Intent(TimeLineActivity.this, null));
                break;
            case 5:
                startActivity(new Intent(TimeLineActivity.this, null));
                break;
            case 6:
                startActivity(new Intent(TimeLineActivity.this, null));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
