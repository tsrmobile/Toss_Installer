package th.co.thiensurat.toss_installer.jobinstallation;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.MainActivity;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.jobinstallation.fragmentmain.JobInstallFragment;
import th.co.thiensurat.toss_installer.utils.ActivityResultBus;
import th.co.thiensurat.toss_installer.utils.ActivityResultEvent;

public class JobActivity extends AppCompatActivity {

    private TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        bindView();
        setToolbar();
        loadJobFragment();
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    private void bindView() {
        ButterKnife.bind(this);
    }

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        textViewTitle.setText("รายการติดตั้ง");
        setSupportActionBar(toolbar);
    }

    private void loadJobFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.job_container, JobInstallFragment.getInstance()).addToBackStack(null).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResultBus.getInstance().postQueue(new ActivityResultEvent(requestCode, resultCode, data));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
        } else if (item.getItemId() == R.id.menu_home) {
            Intent intent = new Intent(JobActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
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
