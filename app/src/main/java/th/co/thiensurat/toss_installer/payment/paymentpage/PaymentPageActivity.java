package th.co.thiensurat.toss_installer.payment.paymentpage;

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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.centerm.smartpos.aidl.printer.AidlPrinter;
import com.centerm.smartpos.aidl.sys.AidlDeviceManager;
import com.centerm.smartpos.constant.Constant;
import com.centerm.smartpos.util.LogUtil;
import com.datecs.api.emsr.EMSR;
import com.datecs.api.printer.Printer;
import com.datecs.api.printer.ProtocolAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.contract.ContractActivity;
import th.co.thiensurat.toss_installer.contract.printer.utils.PrinterServer;
import th.co.thiensurat.toss_installer.contract.printer.utils.PrinterServerListener;
import th.co.thiensurat.toss_installer.contract.utils.ReceiptConfiguration;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;

public class PaymentPageActivity extends BaseMvpActivity<PaymentPageInterface.Presenter> implements PaymentPageInterface.View {

    private TextView textViewTitle;
    private CustomDialog customDialog;

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

    private ReceiptConfiguration receiptConfiguration;

    @Override
    public PaymentPageInterface.Presenter createPresenter() {
        return PaymentPagePresenter.create();
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
    @BindView(R.id.printer_status) TextView textViewPrintStatus;
    @BindView(R.id.layout_border) LinearLayout linearLayoutBorder;
    @BindView(R.id.cash) RadioButton radioButtonCash;
    @BindView(R.id.creditcard) RadioButton radioButtonCredit;
    @BindView(R.id.check) RadioButton radioButtonCheck;
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
        receiptConfiguration = new ReceiptConfiguration(PaymentPageActivity.this);
    }

    @Override
    public void setupView() {
        setToolbar();
        editTextTotalAmount.setEnabled(false);
        linearLayoutBorder.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_rounded_gray));
        buttonPayment.setOnClickListener(onPayment());
        radioButtonCut.setOnClickListener(paymentTypeOptionClickListener);
        radioButtonSome.setOnClickListener(paymentTypeOptionClickListener);
        radioButtonNormal.setOnClickListener(paymentTypeOptionClickListener);

        radioButtonCash.setOnClickListener(paymentChannelOptionClickListener);
        radioButtonCheck.setOnClickListener(paymentChannelOptionClickListener);
        radioButtonCredit.setOnClickListener(paymentChannelOptionClickListener);
    }

    @Override
    public void initialize() {
        connectBluetoothPaired();
        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(receiver, filter);

        bindService();
    }

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_clear_white_24dp));
        textViewTitle.setText("ชำระเงิน");
        setSupportActionBar(toolbar);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unbindService(conn);
        closeBluetoothConnection();
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

    RadioButton.OnClickListener paymentTypeOptionClickListener = new RadioButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (radioButtonSome.isChecked()) {
                editTextTotalAmount.setEnabled(true);
                linearLayoutBorder.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_rounded_colorprimarydark));
            } else if (radioButtonCut.isChecked()) {
                // get total price of order
                editTextTotalAmount.setEnabled(false);
                linearLayoutBorder.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_rounded_gray));
            }else {
                editTextTotalAmount.setEnabled(false);
                linearLayoutBorder.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_rounded_gray));
            }
        }
    };

    RadioButton.OnClickListener paymentChannelOptionClickListener = new RadioButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (radioButtonCredit.isChecked()) {
                linearLayoutCreditCard.setVisibility(View.VISIBLE);
            } else {
                linearLayoutCreditCard.setVisibility(View.GONE);
            }
        }
    };

    private View.OnClickListener onPayment() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPayment.startAnimation(new AnimateButton().animbutton());
                printText();
            }
        };
    }

    private void printText() {
        printTask(new PrinterRunnable() {
            @Override
            public void print(CustomDialog customDialog, Printer printer) {
                //receiptConfiguration = new ReceiptConfiguration(PaymentPageActivity.this, bluetoothDevice.getName());
                /*receiptConfiguration.setPathSignature(empSign.getAbsolutePath(), pathCustomer, path);*/
                try {
                    if (bluetoothDevice.getName().equals("Virtual Bluetooth Printer")) {
                        printDev.setPrinterGray(0x03);
                        printDev.printBmpFastSync(receiptConfiguration.printReceipt(), Constant.ALIGN.CENTER);
                        printDev.spitPaper(100);
                    } /*else {
                        Bitmap bitmap = receiptConfiguration.print(printType);
                        final int width = bitmap.getWidth();
                        final int height = bitmap.getHeight();
                        final int[] argb = new int[width * height];
                        bitmap.getPixels(argb, 0, width, 0, 0, width, height);
                        bitmap.recycle();

                        mPrinter.printCompressedImage(argb, width, height, Printer.ALIGN_CENTER, true);
                        mPrinter.reset();
                        mPrinter.feedPaper(100);
                        mPrinter.flush();
                    }*/

                    PaymentPageActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            //onSuccess("");
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Log.e("printText error (TOP)", e.getLocalizedMessage());
                } /*catch (IOException e) {
                    e.printStackTrace();
                    Log.e("printText error I/O", e.getLocalizedMessage());
                }*/
            }
        });
    }

    private void printTask(final PrinterRunnable runnable) {
        customDialog.dialogLoading();
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
                    PaymentPageActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            customDialog.dialogFail("พบข้อผิดพลาด\nกรุณาติดต่อผู้พัฒนา");
                        }
                    });

                    Log.e("Critical error occurs: ", e.getMessage());
                } finally {
                    customDialog.dialogDimiss();
                }
            }
        });
        thread.start();
    }

    /*class MyBroadCastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("base", "action:" +intent.getAction());
        }
    }*/

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
