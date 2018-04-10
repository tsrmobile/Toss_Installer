package th.co.thiensurat.toss_installer.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.setting.backupandrestore.BackupAndRestoreActivity;
import th.co.thiensurat.toss_installer.setting.signature.SignActivity;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;

public class ConfigurationActivity extends AppCompatActivity{

    private TextView textViewTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        bindView();
        setupView();
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.button_signature) RelativeLayout relativeLayoutButtonSign;
    @BindView(R.id.button_backup) RelativeLayout relativeLayoutButtonBack;
    private void bindView() {
        ButterKnife.bind(this);
        relativeLayoutButtonSign.setOnClickListener(onSign());
        relativeLayoutButtonBack.setOnClickListener(onBackup());
    }

    private void setupView() {
        setToolbar();
    }

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        textViewTitle.setText("ตั้งค่า");
        setSupportActionBar(toolbar);
    }

    private View.OnClickListener onSign() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutButtonSign.startAnimation(new AnimateButton().animbutton());
                startActivityForResult(new Intent(ConfigurationActivity.this, SignActivity.class), Constance.REQUEST_SIGNATURE);
            }
        };
    }

    private View.OnClickListener onBackup() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutButtonBack.startAnimation(new AnimateButton().animbutton());
                startActivityForResult(new Intent(ConfigurationActivity.this, BackupAndRestoreActivity.class), Constance.REQUEST_BACKUP);
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

    /*@Override
    public ConfigurationInterface.Presenter createPresenter() {
        return ConfigurationsPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_settings;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.button_signature) RelativeLayout relativeLayoutButtonSign;
    @BindView(R.id.button_backup) RelativeLayout relativeLayoutButtonBack;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
        relativeLayoutButtonSign.setOnClickListener(onSign());
        relativeLayoutButtonBack.setOnClickListener(onBackup());
    }

    @Override
    public void setupInstance() {

    }

    @Override
    public void setupView() {
        setToolbar();
    }

    @Override
    public void initialize() {

    }

    */
}
