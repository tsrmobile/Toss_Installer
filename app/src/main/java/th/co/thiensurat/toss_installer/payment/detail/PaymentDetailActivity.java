package th.co.thiensurat.toss_installer.payment.detail;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
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

import com.centerm.smartpos.aidl.printer.AidlPrinter;
import com.centerm.smartpos.aidl.sys.AidlDeviceManager;
import com.centerm.smartpos.constant.Constant;
import com.centerm.smartpos.util.LogUtil;
import com.datecs.api.emsr.EMSR;
import com.datecs.api.printer.Printer;
import com.datecs.api.printer.ProtocolAdapter;
import com.layernet.thaidatetimepicker.date.DatePickerDialog;
import com.layernet.thaidatetimepicker.time.RadialPickerLayout;
import com.layernet.thaidatetimepicker.time.TimePickerDialog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.MainActivity;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.api.request.RequestPayment;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.contract.printer.utils.PrinterServer;
import th.co.thiensurat.toss_installer.contract.printer.utils.PrinterServerListener;
import th.co.thiensurat.toss_installer.utils.ReceiptConfiguration;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItemGroup;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItemGroup;
import th.co.thiensurat.toss_installer.payment.detail.adapter.PaymentDetailAdapter;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.DateFormateUtilities;
import th.co.thiensurat.toss_installer.utils.MyApplication;
import th.co.thiensurat.toss_installer.utils.ThaiBaht;

public class PaymentDetailActivity extends BaseMvpActivity<PaymentDetailInterface.Presenter>
        implements PaymentDetailInterface.View, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    private String code;
    private JobItem jobItem;
    private String payType;
    private String payReceive;
    private String receiptNumber;
    private AddressItem addressItem;
    private ProductItem productItem;
    private AddressItemGroup addressItemGroup;
    private ProductItemGroup productItemGroup;

    private List<AddressItem> addressItemList;
    private List<ProductItem> productItemList;
    private List<ProductItem> productItemListTest;
    private List<RequestPayment.paymentBody> paymentBodies = new ArrayList<>();

    private TextView textViewTitle;
    private CustomDialog customDialog;
    private LinearLayoutManager layoutManager;
    private PaymentDetailAdapter adapter;
    private ReceiptConfiguration receiptConfiguration;

    private Printer mPrinter;
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
    public AidlDeviceManager manager = null;

    private int i = 0;
    private float price = 0;
    private float periodPrice = 0;
    private float periodPriceBalance = 0;

    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    private DecimalFormat df = new DecimalFormat("#,###.00");

    @Override
    public PaymentDetailInterface.Presenter createPresenter() {
        return PaymentDetailPresenter.create(this);
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_payment_detail;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.button_payment) Button buttonPayment;
    @BindView(R.id.button_duedate) Button buttonDueDate;
    @BindView(R.id.payment_date) TextView textViewPaymentDate;
    @BindView(R.id.receipt_number) TextView textViewReceiptNumber;
    @BindView(R.id.contract_number) TextView textViewContractNumber;
    @BindView(R.id.order_id) TextView textViewOderid;
    @BindView(R.id.contract_name) TextView textViewContractName;
    @BindView(R.id.contract_id_card) TextView textViewContractID;
    @BindView(R.id.contract_address) TextView textViewAddress;
    @BindView(R.id.install_address) TextView textViewInstallAddress;
    @BindView(R.id.payment_current) TextView textViewCurrentPrice;
    @BindView(R.id.sum_text) TextView textViewSumText;
    @BindView(R.id.payment_balance) TextView textViewBalance;
    @BindView(R.id.title_current_preriod) TextView textViewPeriodNumber;
    @BindView(R.id.title_preriod_balance) TextView textViewPeriodBalance;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.printer_status) TextView textViewPrintStatus;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupInstance() {
        customDialog = new CustomDialog(this);
        layoutManager = new LinearLayoutManager(this);
        adapter = new PaymentDetailAdapter(this);
        receiptConfiguration = new ReceiptConfiguration(PaymentDetailActivity.this);
    }

    @Override
    public void setupView() {
        setToolbar();
        setRecyclerView();
        buttonPayment.setOnClickListener(onPayment());
        buttonDueDate.setOnClickListener(onNextDue());
    }

    @Override
    public void initialize() {
        getItemFromIntent();
        getPresenter().getReceiptNumber(jobItem.getContno());
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);

        connectBluetoothPaired();
        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(receiver, filter);

        bindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unbindService(conn);
        closeBluetoothConnection();
    }

    @Override
    public void onPrint() {
        customDialog.dialogPrinting();
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
        if (success.equals("print")) {
            getPresenter().addContractPayment(paymentBodies);
        } else {
            getPresenter().addToLocalDB(paymentBodies);
            if (jobItem.getPeriods().equals("1")) {
                for (ProductItem productItem : productItemList) {
                    if (productItem.getProductPayType().equals("2")) {
                        buttonDueDate.setVisibility(View.VISIBLE);
                        buttonPayment.setVisibility(View.GONE);
                    } else {
                        customDialog.dialogSuccess(success);
                    }
                }
            } else {
                buttonDueDate.setVisibility(View.GONE);
                buttonPayment.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDueSuccess(String success) {
        customDialog.dialogSuccess(success);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bt_menu, menu);
        return true;
    }

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_clear_white_24dp));
        textViewTitle.setText("ข้อมูลใบเสร็จรับเงิน");
        setSupportActionBar(toolbar);
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getItemFromIntent() {
        jobItem = getIntent().getParcelableExtra(Constance.KEY_JOB_ITEM);
        addressItemGroup = getIntent().getParcelableExtra(Constance.KEY_JOB_ADDR);
        productItemGroup = getIntent().getParcelableExtra(Constance.KEY_JOB_PRODUCT);
        payReceive = getIntent().getStringExtra(Constance.KEY_PRODUCT_RECEIVE);
        payType = getIntent().getStringExtra(Constance.KEY_PRODUCT_PAYTYPE);

        addressItemList = addressItemGroup.getData();
        productItemList = productItemGroup.getProduct();

        setDetailAddress();
        adapter.setProductList(productItemList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void setDetail(ProductItem productItem) {
        if (productItem.getProductPayType().equals("2")) {
            if (payReceive.equals("3")) {
                price = Float.parseFloat(productItem.getProductPayActual());
                textViewPeriodNumber.setText("ราคาสุทธิ");
                textViewCurrentPrice.setText(df.format(price));
                textViewSumText.setText(ThaiBaht.getText(price));
                textViewBalance.setVisibility(View.GONE);
                textViewPeriodBalance.setVisibility(View.GONE);
            } else {
                price = Float.parseFloat(productItem.getProductPrice());
                periodPrice = Float.parseFloat(productItem.getProductPayActual());
                periodPriceBalance = price - periodPrice;
                textViewPeriodNumber.setText("ค่างวดที่ " + jobItem.getPeriods() + "/" + productItem.getProductPayPeriods());
                textViewPeriodBalance.setText("คงเหลืองวดที่ " + (Integer.parseInt(jobItem.getPeriods()) + 1) + " ถึง " + productItem.getProductPayPeriods());
                textViewCurrentPrice.setText(df.format(periodPrice));
                textViewBalance.setText(df.format(periodPriceBalance));
                textViewSumText.setText(ThaiBaht.getText(periodPrice));
            }
        } else {
            price = Float.parseFloat(productItem.getProductPayActual());
            textViewPeriodNumber.setText("ราคาสุทธิ");
            textViewCurrentPrice.setText(df.format(price));
            textViewSumText.setText(ThaiBaht.getText(price));
            textViewBalance.setVisibility(View.GONE);
            textViewPeriodBalance.setVisibility(View.GONE);
        }
    }

    private void setDetailAddress() {
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
    }

    @Override
    public void setReceiptNumber(String receiptNum) {
        this.receiptNumber = receiptNum;
        textViewReceiptNumber.setText(receiptNumber);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat dateFormatDB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date duedate = null;
        String paydate = "";
        try {
            paydate = dateFormatDB.format(new Date());
            duedate = dateFormatDB.parse(jobItem.getDuedate());
        } catch(Exception ex){
            ex.printStackTrace();
        }
        for (ProductItem productItem : productItemList) {
            setDetail(productItem);
            Log.e("product code", productItem.getProductCode());
            if (productItem.getProductPayType().equals("2")) {
                paymentBodies.add(new RequestPayment.paymentBody()
                        .setContno(jobItem.getContno())
                        .setProductcode(productItem.getProductCode())
                        .setPeriod(jobItem.getPeriods())
                        .setPaymenttype(payType)
                        .setPaymentreceive(payReceive)
                        .setDuedate((jobItem.getDuedate().isEmpty()) ? paydate : jobItem.getDuedate())
                        .setPaydate(paydate)
                        .setEmpid(MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID))
                        .setAmount(productItem.getProductPayAmount())
                        .setActual(productItem.getProductPayActual())
                        .setReceiptno(receiptNumber)
                        .setReceiptdate(dateFormatDB.format(new Date()))
                );
            } else {
                paymentBodies.add(new RequestPayment.paymentBody()
                        .setContno(jobItem.getContno())
                        .setProductcode(productItem.getProductCode())
                        .setPeriod(jobItem.getPeriods())
                        .setPaymenttype(payType)
                        .setPaymentreceive(payReceive)
                        .setDuedate((jobItem.getDuedate().isEmpty()) ? paydate : jobItem.getDuedate())
                        .setPaydate(paydate)
                        .setEmpid(MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID))
                        .setAmount(productItem.getProductPayAmount())
                        .setActual(productItem.getProductPayActual())
                        .setReceiptno(receiptNumber)
                        .setReceiptdate(dateFormatDB.format(new Date()))
                );
            }
        }
    }

    private View.OnClickListener onPayment() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPayment.startAnimation(new AnimateButton().animbutton());
                printText();
            }
        };
    }

    private View.OnClickListener onNextDue() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonDueDate.startAnimation(new AnimateButton().animbutton());
                datePicker();
            }
        };
    }

    Calendar calendarDate;
    private void datePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                PaymentDetailActivity.this,
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
        } else if (item.getItemId() == R.id.menu_home) {
            Intent intent = new Intent(PaymentDetailActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else if (item.getItemId() == R.id.menu_bt) {
            connectBluetoothPaired();
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

    protected void initPrinter(InputStream inputStream, OutputStream outputStream) throws IOException {
        Log.e("initPrinter", "Initialize printer...");
        Printer.setDebug(true);
        EMSR.setDebug(true);
        mProtocolAdapter = new ProtocolAdapter(inputStream, outputStream);
        if (mProtocolAdapter.isProtocolEnabled()) {
            Log.e("Protocol", "Protocol mode is enabled");
            mPrinterChannel = mProtocolAdapter.getChannel(ProtocolAdapter.CHANNEL_PRINTER);
            mPrinter = new Printer(mPrinterChannel.getInputStream(), mPrinterChannel.getOutputStream());
        } else {
            mPrinter = new Printer(mProtocolAdapter.getRawInputStream(), mProtocolAdapter.getRawOutputStream());
        }

        mPrinter.setConnectionListener(new Printer.ConnectionListener() {
            @Override
            public void onDisconnect() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing()) {
                            waitForConnection();
                        }
                    }
                });
            }
        });
    }

    private void connectBluetoothPaired() {
        closePrinterServer();
        closePrinterConnection();
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            customDialog.dialogLoading();
            pairedDevices = bluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (final BluetoothDevice device : pairedDevices) {
                    Log.e("device name", device.getName());
                    final Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            bluetoothAdapter.cancelDiscovery();
                            try {
                                uuid = UUID.fromString(Constance.UUID);
                                bluetoothDevice = bluetoothAdapter.getRemoteDevice(device.getAddress());
                                try {
                                    mBtSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                                    mBtSocket.connect();
                                    printAddress = device.getAddress();
                                    inputStream = mBtSocket.getInputStream();
                                    outputStream = mBtSocket.getOutputStream();
                                } catch (IOException ie) {
                                    ie.printStackTrace();
                                    return;
                                }

                                try {
                                    initPrinter(inputStream, outputStream);
                                    customDialog.dialogDimiss();
                                } catch (IOException e) {
                                    Log.e("FAILED to initiallize: ", e.getMessage());
                                    return;
                                }

                            } finally {
                                customDialog.dialogDimiss();
                            }
                        }
                    });
                    thread.start();
                }
            } else {
                waitForConnection();
            }
        } else {
            Intent intentOpenBluetoothSettings = new Intent();
            intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
            startActivityForResult(intentOpenBluetoothSettings, Constance.REQUEST_BLUETOOTH_SETTINGS);
        }
    }
    /***********************************************************************************************/

    /************************************Clear device connection***********************************/
    private synchronized void closeBluetoothConnection() {
        BluetoothSocket s = mBtSocket;
        mBtSocket = null;
        if (s != null) {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void closePrinterServer() {
        PrinterServer ps = mPrinterServer;
        mPrinterServer = null;
        if (ps != null) {
            try {
                ps.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void closePrinterConnection() {
        if (mPrinter != null) {
            mPrinter.close();
        }

        if (mProtocolAdapter != null) {
            mProtocolAdapter.close();
        }

        try {
            inputStream = null;
            outputStream = null;
        } catch (Exception ex) {
            Log.e("closePrinterConnection", ex.getMessage());
        }
    }
    /***********************************************************************************************/

    private synchronized void waitForConnection() {
        try {
            mPrinterServer = new PrinterServer(new PrinterServerListener() {
                @Override
                public void onConnect(Socket socket) {
                    try {
                        inputStream = socket.getInputStream();
                        outputStream = socket.getOutputStream();
                        initPrinter(inputStream, outputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("FAILED to initialize: ", e.getMessage());
                        waitForConnection();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                textViewPrintStatus.setText("เชื่อมต่อปริ้นท์เตอร์ " + bluetoothDevice.getName() + " แล้ว");
                textViewPrintStatus.setBackgroundColor(getResources().getColor(R.color.LimeGreen));
            }  else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                Log.e("Bluetooth connection", action);
                textViewPrintStatus.setText("ไม่ได้เชื่อมต่อปริ้นท์เตอร์");
                textViewPrintStatus.setBackgroundColor(getResources().getColor(R.color.Orange));
                closePrinterServer();
                closePrinterConnection();
                closeBluetoothConnection();
                waitForConnection();
            }
        }
    };

    private void printText() {
        printTask(new PrinterRunnable() {
            @Override
            public void print(CustomDialog customDialog, Printer printer) {
                receiptConfiguration.setReceiptInfoActivity(bluetoothDevice.getName(), jobItem, receiptNumber, productItemList, addressItemList);
                try {
                    if (bluetoothDevice.getName().equals("Virtual Bluetooth Printer")) {
                        printDev.setPrinterGray(0x03);
                        printDev.printBmpFastSync(receiptConfiguration.printReceipt(), Constant.ALIGN.CENTER);
                        printDev.spitPaper(100);
                    } else {
                        Bitmap bitmap = receiptConfiguration.printReceipt();
                        final int width = bitmap.getWidth();
                        final int height = bitmap.getHeight();
                        final int[] argb = new int[width * height];
                        bitmap.getPixels(argb, 0, width, 0, 0, width, height);
                        bitmap.recycle();

                        mPrinter.printCompressedImage(argb, width, height, Printer.ALIGN_CENTER, true);
                        mPrinter.reset();
                        mPrinter.feedPaper(100);
                        mPrinter.flush();
                    }

                    PaymentDetailActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            onSuccess("print");
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Log.e("printText error (TOP)", e.getLocalizedMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("printText error I/O", e.getLocalizedMessage());
                }
            }
        });
    }

    private void printTask(final PrinterRunnable runnable) {
        onPrint();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.print(customDialog, mPrinter);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("I/O error occurs: ", e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                    PaymentDetailActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            customDialog.dialogFail("พบข้อผิดพลาด\nกรุณาติดต่อผู้พัฒนา");
                        }
                    });

                    Log.e("Critical error occurs: ", e.getMessage());
                } finally {
                    onDismiss();
                }
            }
        });
        thread.start();
    }

    public void bindService() {
        Intent intent = new Intent();
        intent.setPackage("com.centerm.smartposservice");
        intent.setAction("com.centerm.smartpos.service.MANAGER_SERVICE");
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    public ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            manager = null;
            LogUtil.print("Service connection fail.");
            LogUtil.print("manager = " + manager);
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            manager = AidlDeviceManager.Stub.asInterface(service);
            LogUtil.print("Sevice connected.");
            LogUtil.print("manager = " + manager);
            if (null != manager) {
                onDeviceConnected(manager);
            }
        }
    };

    private void onDeviceConnected(final AidlDeviceManager deviceManager) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    printDev = AidlPrinter.Stub.asInterface(deviceManager.getDevice(Constant.DEVICE_TYPE.DEVICE_TYPE_PRINTERDEV));
                    printDev.initPrinter();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private interface PrinterRunnable {
        void print(CustomDialog customDialog, Printer printer) throws IOException;
    }
}
