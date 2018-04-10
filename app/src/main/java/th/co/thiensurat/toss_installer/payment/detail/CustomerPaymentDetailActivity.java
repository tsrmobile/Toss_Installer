package th.co.thiensurat.toss_installer.payment.detail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.layernet.thaidatetimepicker.date.DatePickerDialog;
import com.layernet.thaidatetimepicker.time.RadialPickerLayout;
import com.layernet.thaidatetimepicker.time.TimePickerDialog;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItemGroup;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.payment.paymentpage.PaymentPageActivity;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;

public class CustomerPaymentDetailActivity extends BaseMvpActivity<CustomerPaymentDetailInterface.Presenter>
        implements CustomerPaymentDetailInterface.View, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    private TextView textViewTitle;
    private CustomDialog customDialog;

    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    @Override
    public CustomerPaymentDetailInterface.Presenter createPresenter() {
        return CustomerPaymentDetailPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_customer_payment_detail;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.button_schedule) Button buttonSchedule;
    @BindView(R.id.button_payment) Button buttonPayment;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupInstance() {
        customDialog = new CustomDialog(this);
    }

    @Override
    public void setupView() {
        setToolbar();
        buttonSchedule.setOnClickListener(onSchedule());
        buttonPayment.setOnClickListener(onPayment());
    }

    @Override
    public void initialize() {
        getItemFromIntent();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onLoad() {
        customDialog.dialogLoading();
    }

    @Override
    public void onDismiss() {
        customDialog.dialogDimiss();
    }

    @Override
    public void onFail(String fail) {
        customDialog.dialogFail(fail);
    }

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_clear_white_24dp));
        textViewTitle.setText("ข้อมูลใบเสร็จรับเงิน");
        setSupportActionBar(toolbar);
    }

    private void getItemFromIntent() {
        JobItem jitem = getIntent().getParcelableExtra(Constance.KEY_JOB_ITEM);
    }

    private View.OnClickListener onSchedule() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSchedule.startAnimation(new AnimateButton().animbutton());
                datePicker();
            }
        };
    }

    private View.OnClickListener onPayment() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPayment.startAnimation(new AnimateButton().animbutton());
                startActivityForResult(new Intent(CustomerPaymentDetailActivity.this, PaymentPageActivity.class), Constance.REQUEST_PAYMENT_PAGE);
            }
        };
    }

    Calendar calendarDate;
    private void datePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                CustomerPaymentDetailActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        calendarDate = Calendar.getInstance();
        dpd.setMinDate(calendarDate);
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;

        try {
            String selectDate = dayOfMonth + "/" + monthOfYear + "/"+year;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date selectedDate = dateFormat.parse(selectDate);
            Date currentDate = new Date();

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Log.e("To day", formatter.format(currentDate) + "");

            long diff = selectedDate.getTime() - currentDate.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            Log.e("Day", (days + 1) + "");

            if ((days + 1) >= 8) {
                customDialog.dialogWarning("ไม่สามารถนัดวันชำระเกินกว่ากำหนดได้");
            } else {

            }

        } catch (ParseException e) {
            Log.e("exception", e.getLocalizedMessage());
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

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
