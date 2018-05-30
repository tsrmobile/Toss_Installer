package th.co.thiensurat.toss_installer.deposit.deposit;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.deposit.adapter.DepositAdapter;
import th.co.thiensurat.toss_installer.deposit.item.DepositItem;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;

public class DepositActivity extends BaseMvpActivity<DepositInterface.Presenter>
        implements DepositInterface.View, DepositAdapter.SelectedAll {

    private int pos = -1;
    private float sum = 0;
    private TextView textViewTitle;

    private DepositAdapter adapter;
    private CustomDialog customDialog;
    private LinearLayoutManager layoutManager;
    private List<DepositItem> depositItemList;

    private DecimalFormat df = new DecimalFormat("#,###.00");

    @Override
    public DepositInterface.Presenter createPresenter() {
        return DepositPresenter.create(this);
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_deposit;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.summary) TextView textViewSum;
    @BindView(R.id.button_print) Button buttonPrint;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupInstance() {
        adapter = new DepositAdapter(this);
        customDialog = new CustomDialog(this);
        layoutManager = new LinearLayoutManager(this);
    }

    @Override
    public void setupView() {
        setToolbar();
        setRecyclerView();
        buttonPrint.setOnClickListener(onPrint());
    }

    @Override
    public void initialize() {
        getPresenter().getAllActual();
        getDataFromIntent();
    }

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        textViewTitle.setText("จำนวนเงินที่นำส่ง");
        setSupportActionBar(toolbar);
    }

    private void getDataFromIntent() {
        String channel = getIntent().getStringExtra(Constance.KEY_CHANNEL);
        String brach = getIntent().getStringExtra(Constance.KEY_CHANNEL_NAME);
        String receiver = getIntent().getStringExtra(Constance.KEY_RECEIVER);
        String ref = getIntent().getStringExtra(Constance.KEY_REF);
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void setActualToAdapter(List<DepositItem> depositItems) {
        this.depositItemList = depositItems;
        adapter.setDepositItemList(depositItemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setSelectedAll(this);
    }

    private View.OnClickListener onPrint() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setSum() {
        sum = 0;
        for (DepositItem item : depositItemList) {
            if (item.isSelected()) {
                sum += Float.parseFloat(item.getAmount());
            }
        }

        if (sum > 0) {
            textViewSum.setText(df.format(sum));
        } else {
            textViewSum.setText("0.00");
        }
    }

    @Override
    public void onSelected(View view, int position) {
        String date = depositItemList.get(position).getDate();
        for (DepositItem item : depositItemList) {
            if (date.equals(item.getDate())) {
                item.setSelected(!item.isSelected());
            }
        }
        adapter.notifyDataSetChanged();
        setSum();
    }
}
