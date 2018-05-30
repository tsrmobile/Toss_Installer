package th.co.thiensurat.toss_installer.deposit.channel;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.MainActivity;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.deposit.deposit.DepositActivity;
import th.co.thiensurat.toss_installer.payment.activity.PaymentActivity;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.DateFormateUtilities;
import th.co.thiensurat.toss_installer.utils.MyApplication;

public class DepositChannelActivity extends BaseMvpActivity<DepositChannelInterface.Presenter> implements DepositChannelInterface.View {

    private int pos = 0;
    private String branch;
    private String channel;
    private String receiver;
    private String ref;
    private TextView textViewTitle;
    private CustomDialog customDialog;

    @Override
    public DepositChannelInterface.Presenter createPresenter() {
        return DepositChannelPresenter.create(this);
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_deposit_channel;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.button_next) Button buttonNext;
    @BindView(R.id.head_office) RadioButton radioButtonOffice;
    @BindView(R.id.bank) RadioButton radioButtonBank;
    @BindView(R.id.office_layout) LinearLayout linearLayoutOffice;
    @BindView(R.id.counter_service_layout) LinearLayout linearLayoutCounterService;
    @BindView(R.id.counter_service) RadioButton radioButtonCountService;
    @BindView(R.id.spinner_office) Spinner spinnerBranch;
    @BindView(R.id.edittext_name) EditText editTextName;
    @BindView(R.id.edittext_referrence) EditText editTextRef;
    @BindView(R.id.edittext_branch) EditText editTextBranch;
    @BindView(R.id.edittext_referrence_service) EditText editTextRefService;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
        buttonNext.setOnClickListener(onNext());

        radioButtonOffice.setOnClickListener(ChannelOptionClickListener);
        radioButtonBank.setOnClickListener(ChannelOptionClickListener);
        radioButtonCountService.setOnClickListener(ChannelOptionClickListener);
    }

    @Override
    public void setupInstance() {
        customDialog = new CustomDialog(this);
    }

    @Override
    public void setupView() {
        setToolbar();
    }

    @Override
    public void initialize() {
        setSpinnerBranch();
    }

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        textViewTitle.setText("ช่องทางการส่งเงิน");
        setSupportActionBar(toolbar);
    }

    private void setSpinnerBranch() {
        spinnerBranch.setAdapter(initSpinner());
        spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorAccent));
                pos = position;
                if (position > 0) {
                    branch = Constance.arrBranch[position].toString();
                    Toast.makeText(DepositChannelActivity.this, branch, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private ArrayAdapter<String> initSpinner() {
        ArrayAdapter<String> arrAd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constance.arrBranch);
        arrAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrAd;
    }

    private View.OnClickListener onNext() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo();
            }
        };
    }

    private void nextActivity() {
        Intent intent = new Intent(DepositChannelActivity.this, DepositActivity.class);
        intent.putExtra(Constance.KEY_CHANNEL, channel);
        intent.putExtra(Constance.KEY_CHANNEL_NAME, branch);
        intent.putExtra(Constance.KEY_RECEIVER, receiver);
        intent.putExtra(Constance.KEY_REF, ref);
        startActivityForResult(intent, Constance.REQUEST_DEPOSTI);
    }

    private void getInfo() {
        if (!radioButtonOffice.isChecked() && !radioButtonBank.isChecked() && !radioButtonCountService.isChecked()) {
            customDialog.dialogWarning("กรุณาเลือกทางนำส่งเงิน");
        } else {
            if (radioButtonOffice.isChecked()) {
                if (pos > 0 && !editTextName.getText().toString().isEmpty()) {
                    receiver = editTextName.getText().toString();
                    ref = editTextRef.getText().toString();
                    nextActivity();
                } else {
                    customDialog.dialogWarning("กรุณาเลือกสนญ./สาขา");
                }
            } else if (radioButtonCountService.isChecked()) {
                if (!editTextBranch.getText().toString().isEmpty()) {
                    receiver = editTextBranch.getText().toString();
                    ref = editTextRefService.getText().toString();
                    nextActivity();
                } else {
                    customDialog.dialogWarning("กรุณาใส่ชื่อสาขา");
                }
            }
        }
    }

    RadioButton.OnClickListener ChannelOptionClickListener = new RadioButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (radioButtonOffice.isChecked()) {
                channel = "1";
                editTextRef.setText(genReference1());
                linearLayoutOffice.setVisibility(View.VISIBLE);
                linearLayoutCounterService.setVisibility(View.GONE);
            } else if (radioButtonCountService.isChecked()) {
                channel = "3";
                editTextRefService.setText(genReference2());
                linearLayoutOffice.setVisibility(View.GONE);
                linearLayoutCounterService.setVisibility(View.VISIBLE);
            } else {
                channel = "2";
            }
        }
    };

    private String genReference1() {
        String ret = "00000000";
        if(radioButtonOffice.isChecked()) {
            ret = "00000000";
        } else {
            ret = genReference2();
        }

        String EmpID = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID);

        ret = String.format("%s%s", ret, "0000");
        /*if(BHPreference.PositionCode().toLowerCase().contains("credit")) ret = String.format("%s%s", ret, "0000");
        if(BHPreference.PositionCode().toLowerCase().contains("sale")) ret = String.format("%s%s", ret, "0200");
        if(BHPreference.PositionCode().toLowerCase().contains("dept")) ret = String.format("%s%s", ret, "0400");
        if(BHPreference.PositionCode().toLowerCase().contains("oper")) ret = String.format("%s%s", ret, "1000");*/

        EmpID = EmpID.replace('A','1').replace('B','2').replace('C','3').replace('D','4');
        ret = String.format("%s%s", ret, EmpID);

        return ret;
    }

    private String genReference2() {
        Date dtmNow = new Date();
        String ref = getPresenter().getRef2();
        Log.e("ref", ref);
        String CurrentYear = DateFormateUtilities.dateFormat(dtmNow, "yyyy");
        String strRunningNo;
        int runningNumber;
        boolean isFirstGen = true;
        String ret = "";

        if(radioButtonOffice.isChecked()) {
            String EmpID = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID);
            ret = String.format("%s%s",EmpID.replace('A','1').replace('B','2').replace('C','3').replace('D','4'), DateFormateUtilities.dateFormat(dtmNow, "yyMMddHHmmss"));
        } else {
            if (!ref.isEmpty()) {
                if (ref.length() == 8){
                    String RefYear = ref.substring(0,4);
                    String RefRunning = ref.substring(4,8);
                    if(!RefYear.equals(CurrentYear)) {
                        RefYear = CurrentYear;
                        runningNumber = 1;
                    } else {
                        try {
                            runningNumber = Integer.parseInt(RefRunning) + 1;
                        } catch (NumberFormatException e) {
                            runningNumber = 1;
                        }
                    }
                    strRunningNo = String.format("%04d", runningNumber);
                    ret = String.format("%s%s", RefYear, strRunningNo);
                    isFirstGen = false;
                }
            }

            if (isFirstGen) {
                strRunningNo = String.format("%04d", 1);
                ret = String.format("%s%s", CurrentYear, strRunningNo);
            }
        }
        return ret;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(DepositChannelActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            Intent intent = new Intent(DepositChannelActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
