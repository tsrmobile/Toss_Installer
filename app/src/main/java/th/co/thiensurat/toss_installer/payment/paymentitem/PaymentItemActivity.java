package th.co.thiensurat.toss_installer.payment.paymentitem;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.layernet.thaidatetimepicker.date.DatePickerDialog;
import com.layernet.thaidatetimepicker.time.RadialPickerLayout;
import com.layernet.thaidatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.MainActivity;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItemGroup;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItemGroup;
import th.co.thiensurat.toss_installer.payment.paymentitem.adapter.PaymentItemAdapter;
import th.co.thiensurat.toss_installer.payment.paymentpage.PaymentPageActivity;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.MyApplication;

public class PaymentItemActivity extends BaseMvpActivity<PaymentItemInterface.Presenter>
        implements PaymentItemInterface.View, PaymentItemAdapter.ClickListener, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    private TextView textViewTitle;

    private JobItem jobItem;
    private AddressItem addressItem;
    private AddressItemGroup addressItemGroup;
    private ProductItemGroup productItemGroup;
    private ProductItemGroup newProductItemGroup;

    private PaymentItemAdapter adapter;
    private List<ProductItem> productItemList;
    private List<ProductItem> newProductItemList;

    private CustomDialog customDialog;
    private LinearLayoutManager layoutManager;

    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    @Override
    public PaymentItemInterface.Presenter createPresenter() {
        return PaymentItemPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_payment_item;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.button_schedule) Button buttonSchedule;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
        buttonSchedule.setOnClickListener(onSchedule());
    }

    @Override
    public void setupInstance() {
        adapter = new PaymentItemAdapter(this);
        customDialog = new CustomDialog( this);
        layoutManager = new LinearLayoutManager(this);
    }

    @Override
    public void setupView() {

    }

    @Override
    public void initialize() {
        getItemFromIntent();
        setToolbar();
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

    @Override
    public void onSuccess(String success) {
        customDialog.dialogSuccess(success);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_clear_white_24dp));
        textViewTitle.setText("รายการสินค้า #" + jobItem.getContno());
        setSupportActionBar(toolbar);
    }

    private void getItemFromIntent() {
        jobItem = getIntent().getParcelableExtra(Constance.KEY_JOB_ITEM);
        addressItemGroup = getIntent().getParcelableExtra(Constance.KEY_JOB_ADDR);
        productItemGroup = getIntent().getParcelableExtra(Constance.KEY_JOB_PRODUCT);

        productItemList = productItemGroup.getProduct();

        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter.setProductItem(jobItem, productItemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        try {
            if (!adapter.getStatus().equals("จ่ายแล้ว")) {
                Intent intent = new Intent(PaymentItemActivity.this, PaymentPageActivity.class);
                intent.putExtra(Constance.KEY_PRODUCT_CODE, adapter.getCode());
                intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
                intent.putExtra(Constance.KEY_JOB_ADDR, addressItemGroup);
                intent.putExtra(Constance.KEY_JOB_PRODUCT, productItemGroup);
                startActivityForResult(intent, Constance.REQUEST_PAYMENT);
            }
        } catch (Exception e) {
            Intent intent = new Intent(PaymentItemActivity.this, PaymentPageActivity.class);
            intent.putExtra(Constance.KEY_PRODUCT_CODE, adapter.getCode());
            intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
            intent.putExtra(Constance.KEY_JOB_ADDR, addressItemGroup);
            intent.putExtra(Constance.KEY_JOB_PRODUCT, productItemGroup);
            startActivityForResult(intent, Constance.REQUEST_PAYMENT);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        try {
            String selectDate = dayOfMonth + "/" + monthOfYear + "/"+year;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date selectedDate = dateFormat.parse(selectDate);
            Date currentDate = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatterDB = new SimpleDateFormat("yyyy-MM-dd H:m:ss");

            long diff = selectedDate.getTime() - currentDate.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            if ((days + 1) >= 8) {
                customDialog.dialogWarning("ไม่สามารถนัดวันชำระเกินกว่ากำหนดได้");
            } else {
                getPresenter().updateDueDate(jobItem.getOrderid(),
                        formatterDB.format(selectedDate), MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID));
            }
        } catch (ParseException e) {
            Log.e("exception", e.getLocalizedMessage());
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

    }

    private View.OnClickListener onSchedule() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSchedule.startAnimation(new AnimateButton().animbutton());
                datePicker();
            }
        };
    }

    Calendar calendarDate;
    private void datePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                PaymentItemActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        calendarDate = Calendar.getInstance();
        dpd.setMinDate(calendarDate);
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
        } else if (item.getItemId() == R.id.menu_home) {
            Intent intent = new Intent(PaymentItemActivity.this, MainActivity.class);
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
