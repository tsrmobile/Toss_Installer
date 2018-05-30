package th.co.thiensurat.toss_installer.payment.paymentpage;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.layernet.thaidatetimepicker.date.DatePickerDialog;
import com.layernet.thaidatetimepicker.time.RadialPickerLayout;
import com.layernet.thaidatetimepicker.time.TimePickerDialog;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import th.co.thiensurat.toss_installer.payment.detail.PaymentDetailActivity;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.MyApplication;

public class PaymentPageActivity extends BaseMvpActivity<PaymentPageInterface.Presenter>
        implements PaymentPageInterface.View, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    private TextView textViewTitle;
    private CustomDialog customDialog;

    /*private Printer mPrinter;
    private String printAddress;
    private BluetoothSocket mBtSocket;
    private PrinterServer mPrinterServer;
    private ProtocolAdapter mProtocolAdapter;
    private ProtocolAdapter.Channel mPrinterChannel;

    private UUID uuid;
    private IntentFilter filter;
    private BluetoothDevice bluetoothDevice;
    private Set<BluetoothDevice> pairedDevices;
    private static InputStream inputStream = null;
    private static OutputStream outputStream = null;

    private AidlPrinter printDev = null;
    public AidlDeviceManager manager = null;*/

    private JobItem jobItem;
    /*private String receiptNumber;
    private AddressItem addressItem;*/
    private ProductItem productItem;
    private AddressItemGroup addressItemGroup;
    private ProductItemGroup productItemGroup;
    private ProductItemGroup newProductItemGroup;

    private List<AddressItem> addressItemList;
    private List<ProductItem> productItemList;
    private List<ProductItem> newProductItemList;

    private String payType;
    private String payReceive;
    private String bankName = "";
    private String productcode = "";

    private float price = 0;
    private float periodPrice = 0;
    private float payActual = 0;
    private DecimalFormat df = new DecimalFormat("#,###.00");

    //List<RequestPayment.paymentBody> paymentBodies;

    String[] arr = { "กรุณาเลือกธนาคาร"
            , "ธนาคารกรุงเทพ(BBL)"
            , "ธนาคารกสิกรไทย(KBANK)"
            , "ธนาคารกรุงไทย(KTB)"
            , "ธนาคารทหารไทย(TMB)"
            , "ธนาคารไทยพาณิชย์(SCB)"
            , "ธนาคารกรุงศรีอยุธยา(BAY)"
            , "ธนาคารเกียรตินาคิน(KKP)"
            , "ธนาคารซีไอเอ็มบีไทย(CIMB)"
            , "ธนาคารทิสโก้(TISCO)"
            , "ธนาคารธนชาต(TBANK)"
            , "ธนาคารยูโอบี(UOB)"
            , "ธนาคารสแตนดาร์ดชาร์เตอร์ด (ไทย)(SCBT)"};

    @Override
    public PaymentPageInterface.Presenter createPresenter() {
        return PaymentPagePresenter.create(this);
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_payment_page;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.total_amount) EditText editTextTotalAmount;
    @BindView(R.id.normal_price) RadioButton radioButtonNormal;
    @BindView(R.id.some_price) RadioButton radioButtonSome;
    @BindView(R.id.cut_loss) RadioButton radioButtonCut;
    @BindView(R.id.button_payment) Button buttonPayment;
    @BindView(R.id.button_duedate) Button buttonDuedate;
    @BindView(R.id.layout_border) LinearLayout linearLayoutBorder;
    @BindView(R.id.cash) RadioButton radioButtonCash;
    @BindView(R.id.creditcard) RadioButton radioButtonCredit;
    @BindView(R.id.check) RadioButton radioButtonCheck;
    @BindView(R.id.promtpay) RadioButton radioButtonPromtpay;
    @BindView(R.id.spinner_bank) Spinner spinnerBank;
    @BindView(R.id.creditcard_number) EditText editTextCreditNumber;
    @BindView(R.id.approve_code) EditText editTextApproveCode;
    @BindView(R.id.creditcard_layout) LinearLayout linearLayoutCreditCard;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupInstance() {
        customDialog = new CustomDialog(this);
        newProductItemGroup = new ProductItemGroup();
        newProductItemList = new ArrayList<ProductItem>();
    }

    @Override
    public void setupView() {
        setToolbar();
        editTextTotalAmount.setEnabled(false);
        linearLayoutBorder.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_rounded_gray));
        buttonPayment.setOnClickListener(onPayment());
        buttonDuedate.setOnClickListener(onSchedule());
        radioButtonCut.setOnClickListener(paymentTypeOptionClickListener);
        radioButtonSome.setOnClickListener(paymentTypeOptionClickListener);
        radioButtonNormal.setOnClickListener(paymentTypeOptionClickListener);

        radioButtonCash.setOnClickListener(paymentChannelOptionClickListener);
        radioButtonCheck.setOnClickListener(paymentChannelOptionClickListener);
        radioButtonCredit.setOnClickListener(paymentChannelOptionClickListener);
        radioButtonPromtpay.setEnabled(false);

        radioButtonNormal.setChecked(true);
        if (radioButtonNormal.isChecked()) {
            payReceive = "1";
            payActual = periodPrice;
        }
    }

    @Override
    public void initialize() {
        getItemFromIntent();
        validateRadioButton();

        editTextTotalAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextTotalAmount.getWindowToken(), 0);
                    payActual = Float.parseFloat(editTextTotalAmount.getText().toString());
                    validateRadioButton();
                    return true;
                }
                return false;
            }
        });

        spinnerBank.setAdapter(initSpinnerSwith());
        spinnerBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorAccent));
                if (position > 0) {
                    bankName = arr[position].toString();
                    Toast.makeText(PaymentPageActivity.this, bankName, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editTextApproveCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextTotalAmount.getWindowToken(), 0);
                    validateRadioButton();
                    return true;
                }
                return false;
            }
        });

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

    @Override
    public void onDueSuccess(String success) {
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
        textViewTitle.setText("ชำระเงิน");
        setSupportActionBar(toolbar);
    }

    private void getItemFromIntent() {
        try {
            productcode = getIntent().getStringExtra(Constance.KEY_PRODUCT_CODE);
            jobItem = getIntent().getParcelableExtra(Constance.KEY_JOB_ITEM);
            addressItemGroup = getIntent().getParcelableExtra(Constance.KEY_JOB_ADDR);
            productItemGroup = getIntent().getParcelableExtra(Constance.KEY_JOB_PRODUCT);
            if (productcode.isEmpty()) {
                buttonDuedate.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            jobItem = getIntent().getParcelableExtra(Constance.KEY_JOB_ITEM);
            addressItemGroup = getIntent().getParcelableExtra(Constance.KEY_JOB_ADDR);
            productItemGroup = getIntent().getParcelableExtra(Constance.KEY_JOB_PRODUCT);
        }

        addressItemList = addressItemGroup.getData();
        productItemList = productItemGroup.getProduct();

        if (productItemList.size() > 1) {
            buttonDuedate.setVisibility(View.GONE);
        }

        try {
            for (ProductItem productItem : productItemList) {
                if (productcode.equals(productItem.getProductCode())) {
                    if (productItem.getProductPayType().equals("2")) {
                        price += Float.parseFloat(productItem.getProductPrice());
                        periodPrice += Float.parseFloat(productItem.getProductPayPerPeriods());
                        payActual = periodPrice;
                        editTextTotalAmount.setText(df.format(payActual));
                        //creatNewProductItem(productItem);
                    } else if (productItem.getProductPayType().equals("1")) {
                        price += Float.parseFloat(productItem.getProductPrice());
                        payActual = price;
                        editTextTotalAmount.setText(df.format(payActual));
                        //creatNewProductItem(productItem);
                        disableRadioButton();
                    }
                }
            }
        } catch (Exception ex) {
            for (ProductItem productItem : productItemList) {
                productcode = productItem.getProductCode();
                if (productItem.getProductPayType().equals("2")) {
                    price += Float.parseFloat(productItem.getProductPrice());
                    periodPrice += Float.parseFloat(productItem.getProductPayPerPeriods());
                    payActual = periodPrice;
                    editTextTotalAmount.setText(df.format(payActual));
                    //creatNewProductItem(productItem);
                } else if (productItem.getProductPayType().equals("1")) {
                    price += Float.parseFloat(productItem.getProductPrice());
                    payActual = price;
                    editTextTotalAmount.setText(df.format(payActual));
                    //creatNewProductItem(productItem);
                    disableRadioButton();
                }
            }
        }
        newProductItemGroup.setProduct(newProductItemList);
    }

    private void disableRadioButton() {
        radioButtonNormal.setEnabled(false);
        radioButtonSome.setEnabled(false);
        radioButtonCut.setEnabled(false);
        radioButtonCut.setBackground(getResources().getDrawable(R.drawable.radio_disable));
        radioButtonSome.setBackground(getResources().getDrawable(R.drawable.radio_disable));
        radioButtonNormal.setBackground(getResources().getDrawable(R.drawable.radio_disable));
    }

    private void creatNewProductItem(ProductItem item) {
        productItem = new ProductItem();
        if (item.getProductPayType().equals("2")) {
            productItem.setProductCode(productcode);
            productItem.setProductName(item.getProductName());
            productItem.setProductModel(item.getProductModel());
            productItem.setProductSerial(item.getProductSerial());
            productItem.setProductPrice(String.valueOf(price));
            productItem.setProductPayType(item.getProductPayType());
            productItem.setProductPayPerPeriods(item.getProductPayPerPeriods());
            productItem.setProductPayPeriods(item.getProductPayPeriods());
            productItem.setProductPayAmount(String.valueOf(periodPrice));
            productItem.setProductPayActual(String.valueOf(payActual).replace(",", ""));
        } else if (item.getProductPayType().equals("1")) {
            productItem.setProductCode(productcode);
            productItem.setProductName(item.getProductName());
            productItem.setProductModel(item.getProductModel());
            productItem.setProductSerial(item.getProductSerial());
            productItem.setProductPrice(String.valueOf(price));
            productItem.setProductPayType(item.getProductPayType());
            productItem.setProductPayPeriods(item.getProductPayPeriods());
            productItem.setProductPayAmount(String.valueOf(price));
            productItem.setProductPayActual(String.valueOf(payActual).replace(",", ""));
        }

        newProductItemList.add(productItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }  else if (item.getItemId() == R.id.menu_home) {
            Intent intent = new Intent(PaymentPageActivity.this, MainActivity.class);
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

    RadioButton.OnClickListener paymentTypeOptionClickListener = new RadioButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (radioButtonSome.isChecked()) {
                /**
                 * Payment some money
                 */
                payReceive = "2";
                editTextTotalAmount.setEnabled(true);
                editTextTotalAmount.setText("");
                editTextTotalAmount.requestFocus();
                validateRadioButton();
                linearLayoutBorder.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_rounded_colorprimarydark));
            } else if (radioButtonCut.isChecked()) {
                /**
                 * Close order by cash (Total amount)
                 */
                payReceive = "3";
                editTextTotalAmount.setEnabled(false);
                editTextTotalAmount.setText(df.format(price));
                payActual = price;
                validateRadioButton();
                linearLayoutBorder.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_rounded_gray));
            }else {
                /**
                 * Full payment
                 */
                payReceive = "1";
                editTextTotalAmount.setEnabled(false);
                editTextTotalAmount.setText(df.format(periodPrice));
                payActual = periodPrice;
                validateRadioButton();
                linearLayoutBorder.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_rounded_gray));
            }
        }
    };

    RadioButton.OnClickListener paymentChannelOptionClickListener = new RadioButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (radioButtonCredit.isChecked()) {
                payType = "2";
                linearLayoutCreditCard.setVisibility(View.VISIBLE);
            } else {
                linearLayoutCreditCard.setVisibility(View.GONE);
            }

            if (radioButtonCash.isChecked()) {
                payType = "1";
            }

            if (radioButtonCheck.isChecked()) {
                payType = "3";
            }

            validateRadioButton();
        }
    };

    private View.OnClickListener onPayment() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPayment.startAnimation(new AnimateButton().animbutton());
                if (!radioButtonCash.isChecked() && !radioButtonCredit.isChecked() && !radioButtonCheck.isChecked()) {
                    customDialog.dialogFail("กรุณาเลืิอกประเภทการชำระเงิน");
                } else {
                    newProductItemList.clear();
                    for (ProductItem productItem : productItemList) {
                        creatNewProductItem(productItem);
                    }
                    Intent intent = new Intent(PaymentPageActivity.this, PaymentDetailActivity.class);
                    intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
                    intent.putExtra(Constance.KEY_JOB_ADDR, addressItemGroup);
                    intent.putExtra(Constance.KEY_JOB_PRODUCT, newProductItemGroup);
                    intent.putExtra(Constance.KEY_PRODUCT_RECEIVE, payReceive);
                    intent.putExtra(Constance.KEY_PRODUCT_PAYTYPE, payType);
                    startActivityForResult(intent, Constance.REQUEST_PAYMENT_DETAIL);
                }

            }
        };
    }

    private ArrayAdapter<String> initSpinnerSwith() {
        ArrayAdapter<String> arrAd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr);
        arrAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrAd;
    }

    private void validateRadioButton() {
        if (radioButtonSome.isChecked()) {
            if (editTextTotalAmount.getText().toString().isEmpty()) {
                buttonPayment.setEnabled(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    buttonPayment.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.DarkGray));
                }
            } else {
                if (!radioButtonCash.isChecked() && !radioButtonCredit.isChecked() && !radioButtonCheck.isChecked()) {
                    buttonPayment.setEnabled(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        buttonPayment.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.DarkGray));
                    }
                } else {
                    buttonPayment.setEnabled(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        buttonPayment.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryDark));
                    }
                }
            }
        } else {
            if (!radioButtonCash.isChecked() && !radioButtonCredit.isChecked() && !radioButtonCheck.isChecked()) {
                buttonPayment.setEnabled(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    buttonPayment.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.DarkGray));
                }
            } else if (radioButtonCredit.isChecked()) {
                if (bankName.isEmpty() && editTextCreditNumber.getText().toString().isEmpty() && editTextApproveCode.getText().toString().isEmpty()) {
                    buttonPayment.setEnabled(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        buttonPayment.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.DarkGray));
                    }
                } else {
                    buttonPayment.setEnabled(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        buttonPayment.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryDark));
                    }
                }
            } else {
                buttonPayment.setEnabled(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    buttonPayment.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryDark));
                }
            }
        }
    }

    private View.OnClickListener onSchedule() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        };
    }

    Calendar calendarDate;
    private void datePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                PaymentPageActivity.this,
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
}
