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
import android.widget.Toast;

import com.layernet.thaidatetimepicker.date.DatePickerDialog;
import com.layernet.thaidatetimepicker.time.RadialPickerLayout;
import com.layernet.thaidatetimepicker.time.TimePickerDialog;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItemGroup;
import th.co.thiensurat.toss_installer.payment.paymentpage.PaymentPageActivity;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.DateFormateUtilities;
import th.co.thiensurat.toss_installer.utils.MyApplication;
import th.co.thiensurat.toss_installer.utils.ThaiBaht;

public class CustomerPaymentDetailActivity extends BaseMvpActivity<CustomerPaymentDetailInterface.Presenter>
        implements CustomerPaymentDetailInterface.View, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    private String code;
    private JobItem jobItem;
    private String receiptNumber;
    private AddressItem addressItem;
    private ProductItem productItem;
    private AddressItemGroup addressItemGroup;
    private ProductItemGroup productItemGroup;

    private List<AddressItem> addressItemList;
    private List<ProductItem> productItemList;

    private TextView textViewTitle;
    private CustomDialog customDialog;

    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    private DecimalFormat df = new DecimalFormat("#,###.00");

    @Override
    public CustomerPaymentDetailInterface.Presenter createPresenter() {
        return CustomerPaymentDetailPresenter.create(this);
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_customer_payment_detail;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.button_schedule) Button buttonSchedule;
    @BindView(R.id.button_payment) Button buttonPayment;
    @BindView(R.id.payment_date) TextView textViewPaymentDate;
    @BindView(R.id.receipt_number) TextView textViewReceiptNumber;
    @BindView(R.id.contract_number) TextView textViewContractNumber;
    @BindView(R.id.order_id) TextView textViewOderid;
    @BindView(R.id.contract_name) TextView textViewContractName;
    @BindView(R.id.contract_id_card) TextView textViewContractID;
    @BindView(R.id.contract_address) TextView textViewAddress;
    @BindView(R.id.install_address) TextView textViewInstallAddress;
    @BindView(R.id.product_name) TextView textViewProductName;
    @BindView(R.id.product_model) TextView textViewProductModel;
    @BindView(R.id.product_serial) TextView textViewProductSerial;
    @BindView(R.id.payment_current) TextView textViewCurrentPrice;
    @BindView(R.id.sum_text) TextView textViewSumText;
    @BindView(R.id.payment_balance) TextView textViewBalance;
    @BindView(R.id.title_current_preriod) TextView textViewPeriodNumber;
    @BindView(R.id.title_preriod_balance) TextView textViewPeriodBalance;
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
        getPresenter().getReceiptNumber();
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

    @Override
    public void onSuccess(String success) {
        customDialog.dialogSuccess(success);
    }

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_clear_white_24dp));
        textViewTitle.setText("ข้อมูลใบเสร็จรับเงิน");
        setSupportActionBar(toolbar);
    }

    private void getItemFromIntent() {
        jobItem = getIntent().getParcelableExtra(Constance.KEY_JOB_ITEM);
        addressItemGroup = getIntent().getParcelableExtra(Constance.KEY_JOB_ADDR);
        productItemGroup = getIntent().getParcelableExtra(Constance.KEY_JOB_PRODUCT);

        addressItemList = addressItemGroup.getData();
        productItemList = productItemGroup.getProduct();

        if (productItemList.size() > 1) {
            buttonSchedule.setVisibility(View.GONE);
        }

        try {
            code = MyApplication.getInstance().getPrefManager().getPreferrence("code");
        } catch (Exception e) {

        }

        setDetail();
    }

    private void setDetail() {
        textViewPaymentDate.setText(DateFormateUtilities.dateFormat(new Date()));
        textViewContractNumber.setText(jobItem.getContno());
        textViewOderid.setText(jobItem.getOrderid());
        textViewContractName.setText(jobItem.getTitle() + jobItem.getFirstName() + " " + jobItem.getLastName());
        textViewContractID.setText(jobItem.getIDCard());

        StringBuilder sbAdd = new StringBuilder();
        StringBuilder sbInAdd = new StringBuilder();
        for (AddressItem item : addressItemList) {
            if (item.getAddressType().equals("AddressIDCard")) {
                sbAdd.append(item.getAddrDetail());
                sbAdd.append("\n");
                sbAdd.append("ต." + item.getSubdistrict() + " อ." + item.getDistrict());
                sbAdd.append("\n");
                sbAdd.append("จ." + item.getProvince() + " " + item.getZipcode());
            }

            if (item.getAddressType().equals("AddressInstall")) {
                sbInAdd.append(item.getAddrDetail());
                sbInAdd.append("\n");
                sbInAdd.append("ต." + item.getSubdistrict() + " อ." + item.getDistrict());
                sbInAdd.append("\n");
                sbInAdd.append("จ." + item.getProvince() + " " + item.getZipcode());
            }
        }

        textViewAddress.setText(sbAdd.toString());
        textViewInstallAddress.setText(sbInAdd.toString());

        float price = 0;
        float periodPrice = 0;
        float periodPriceBalance = 0;
        StringBuilder sbModel = new StringBuilder();
        StringBuilder sbSerial = new StringBuilder();
        StringBuilder sbProduct = new StringBuilder();
        for (ProductItem productItem : productItemList) {
            if (productItem.getProductCode().equals(code)) {
                price += Float.parseFloat(productItem.getProductPrice());
                periodPrice += Float.parseFloat(productItem.getProductPayPerPeriods());
                periodPriceBalance = price - periodPrice;
                sbProduct.append(productItem.getProductName());
                sbModel.append(productItem.getProductModel());
                sbSerial.append(productItem.getProductSerial());
                if (productItemList.size() > 1) {
                    sbProduct.append("\n");
                    sbModel.append("\n");
                    sbSerial.append("\n");
                }
            }

            textViewPeriodNumber.setText("ค่างวดที่ " + jobItem.getPeriods() + "/" + productItem.getProductPayPeriods());
            textViewPeriodBalance.setText("คงเหลืองวดที่ " + (Integer.parseInt(jobItem.getPeriods()) + 1) + " ถึง " + productItem.getProductPayPeriods());
        }
        textViewProductName.setText(sbProduct.toString());
        textViewProductModel.setText(sbModel.toString());
        textViewProductSerial.setText(sbSerial.toString());
        textViewCurrentPrice.setText(df.format(periodPrice));
        textViewBalance.setText(df.format(periodPriceBalance));
        textViewSumText.setText(ThaiBaht.getText(periodPrice));
    }

    @Override
    public void setReceiptNumber(String receiptNum) {
        this.receiptNumber = receiptNum;
        textViewReceiptNumber.setText(receiptNumber);
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
                Intent intent = new Intent(CustomerPaymentDetailActivity.this, PaymentPageActivity.class);
                intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
                intent.putExtra(Constance.KEY_JOB_ADDR, addressItemGroup);
                intent.putExtra(Constance.KEY_JOB_PRODUCT, productItemGroup);
                intent.putExtra(Constance.KEY_PAYMENT_RECEIPT_NUMBER, receiptNumber);
                startActivityForResult(intent, Constance.REQUEST_PAYMENT_PAGE);
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
