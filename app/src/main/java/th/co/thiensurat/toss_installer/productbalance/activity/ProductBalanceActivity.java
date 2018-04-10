package th.co.thiensurat.toss_installer.productbalance.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.productbalance.fragment.ItemBalanceFragment;

public class ProductBalanceActivity extends AppCompatActivity {

    private TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_balance);
        bindView();
        setToolbar();
        loadBalanceFragment();
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    private void bindView() {
        ButterKnife.bind(this);
    }

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        textViewTitle.setText("สินค้าคงเหลือ");
        setSupportActionBar(toolbar);
    }

    private void loadBalanceFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.balance_container, ItemBalanceFragment.getInstance()).addToBackStack(null).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            setResult(RESULT_OK);
            finish();
        }
        return true;
    }
}
