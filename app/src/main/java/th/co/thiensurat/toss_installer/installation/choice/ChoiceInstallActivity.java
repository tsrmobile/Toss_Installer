package th.co.thiensurat.toss_installer.installation.choice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.Main2Activity;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.installation.InstallationActivity;
import th.co.thiensurat.toss_installer.jobinstallation.JobActivity;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.MyApplication;

public class ChoiceInstallActivity extends AppCompatActivity {

    private TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_install);

        bindView();
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.button_install_old) RelativeLayout buttonOldInstall;
    @BindView(R.id.button_install_new) RelativeLayout buttonNewInstall;
    private void bindView() {
        ButterKnife.bind(this);

        buttonOldInstall.setOnClickListener(onOldInstall());
        buttonNewInstall.setOnClickListener(onNewInstall());

        setToolbar();
    }

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        textViewTitle.setText("เลือกระบบการติดตั้ง");
        setSupportActionBar(toolbar);
    }

    private View.OnClickListener onOldInstall() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOldInstall.startAnimation(new AnimateButton().animbutton());
                MyApplication.getInstance().getPrefManager().setPreferrence("choice", "install");
                startActivityForResult(
                        new Intent(ChoiceInstallActivity.this, Main2Activity.class),
                        Constance.REQUEST_OLD_INSTALL);
            }
        };
    }

    private View.OnClickListener onNewInstall() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonNewInstall.startAnimation(new AnimateButton().animbutton());
                startActivityForResult(
                        new Intent(ChoiceInstallActivity.this, JobActivity.class),
                        Constance.REQUEST_JOB);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            setResult(RESULT_OK);
            finish();
        }
        return true;
    }
}
