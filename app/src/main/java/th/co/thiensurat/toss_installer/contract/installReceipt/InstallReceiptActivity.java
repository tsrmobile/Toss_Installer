package th.co.thiensurat.toss_installer.contract.installReceipt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.datecs.api.emsr.EMSR;
import com.datecs.api.printer.Printer;
import com.datecs.api.printer.ProtocolAdapter;
import com.zj.btsdk.BluetoothService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.api.result.JobFinishItem;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.contract.ContractActivity;
import th.co.thiensurat.toss_installer.contract.signaturepad.SignatureActivity;
import th.co.thiensurat.toss_installer.detail.edit.addressinstall.InstallAddressPresenter;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.job.item.ProductItem;
import th.co.thiensurat.toss_installer.printer.bluetoothDevice.PrinterServer;
import th.co.thiensurat.toss_installer.printer.bluetoothDevice.PrinterServerListener;
import th.co.thiensurat.toss_installer.printer.documentcontroller.DocumentController;
import th.co.thiensurat.toss_installer.printer.documentcontroller.PrintTextInfo;
import th.co.thiensurat.toss_installer.printer.documentcontroller.ThemalPrintController;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.MyApplication;

import static th.co.thiensurat.toss_installer.utils.Constance.REQUEST_CONNECT_DEVICE;

public class InstallReceiptActivity extends BaseMvpActivity<InstallReceiptInterface.Presenter> implements InstallReceiptInterface.View{

    private String path;
    private String pathCustomer;
    private String pathInstaller;

    private JobItem jobItem;
    private JobFinishItem jobFinishItem;
    private List<ProductItem> productItemList = new ArrayList<ProductItem>();
    private List<AddressItem> addressItemList = new ArrayList<AddressItem>();

    private String contno;
    private StringBuilder sbAdd;
    private StringBuilder sbInAdd;
    private TextView textViewTitle;
    private String customerSignPath;
    private CustomDialog customDialog;
    private DocumentController documentController;

    private BluetoothAdapter bluetoothAdapter;
    private static BluetoothSocket bluetoothSocket;

    private String printerAddress;
    private PrinterServer printerServer;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private BluetoothService bluetoothService = null;
    private ThemalPrintController themalPrintController;

    private UUID uuid;
    private Socket mSocket;
    private Printer mPrinter;
    private IntentFilter filter;
    private Thread connectThread;
    private BluetoothDevice bluetoothDevice;
    private ProtocolAdapter mProtocolAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ProtocolAdapter.Channel mPrinterChannel;
    private DecimalFormat df = new DecimalFormat("#,###.00");

    public static InstallReceiptActivity getInstance() {
        return new InstallReceiptActivity();
    }

    @Override
    public InstallReceiptInterface.Presenter createPresenter() {
        return InstallReceiptPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_install_receipt;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.printer_status) TextView textViewPrintStatus;
    @BindView(R.id.floating_print_install_receipt) Button buttonPrintInstallReceipt;
    @BindView(R.id.floating_finish) Button buttonFinish;
    @BindView(R.id.contract_date) TextView textViewDate;
    @BindView(R.id.contract_number) TextView textViewNumber;
    @BindView(R.id.order_id) TextView textViewOrder;
    @BindView(R.id.contract_name) TextView textViewName;
    @BindView(R.id.contract_id_card) TextView textViewID;
    @BindView(R.id.contract_address) TextView textViewAddress;
    @BindView(R.id.install_address) TextView textViewInstallAddress;
    @BindView(R.id.contract_phone) TextView textViewPhone;
    @BindView(R.id.contract_product_name) TextView textViewProductName;
    @BindView(R.id.contract_product_model) TextView textViewProductModel;
    @BindView(R.id.contract_product_serial) TextView textViewProductSerial;
    @BindView(R.id.title_date) TextView textViewTitleDate;
    @BindView(R.id.title_number) TextView textViewTitleNumber;
    @BindView(R.id.title_order) TextView textViewTitleOrder;
    @BindView(R.id.title_name) TextView textViewTitleName;
    @BindView(R.id.title_id) TextView textViewTitleId;
    @BindView(R.id.title_add_id) TextView textViewTitleAddId;
    @BindView(R.id.title_add_install) TextView textViewTitleAddInstall;
    @BindView(R.id.title_phone) TextView textViewTitlePhone;
    @BindView(R.id.title_product_name) TextView textViewTitleProductName;
    @BindView(R.id.title_product_model) TextView textViewTitleProductModel;
    @BindView(R.id.hint_signature1) TextView textViewHintSignature1;
    @BindView(R.id.hint_signature2) TextView textViewHintSignature2;
    @BindView(R.id.customer_name) TextView textViewCustomerName;
    @BindView(R.id.installer_name) TextView textViewInstallerName;
    @BindView(R.id.signature_customer) ImageView imageViewCustomerSignature;
    @BindView(R.id.signature_installer) ImageView imageViewSignatureInstaller;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
        buttonPrintInstallReceipt.setOnClickListener( onPrint() );
        imageViewSignatureInstaller.setOnClickListener( onSign() );
    }

    @Override
    public void setupInstance() {
        customDialog = new CustomDialog(InstallReceiptActivity.this);
        documentController = new DocumentController(InstallReceiptActivity.this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        themalPrintController = new ThemalPrintController(InstallReceiptActivity.this);
        bluetoothService = new BluetoothService(InstallReceiptActivity.this, handler);
    }

    @Override
    public void setupView() {
        setToolbar();
    }

    @Override
    public void initialize() {
        getDataFromIntent();
        setUpContract();

        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(receiver, filter);
    }

    private void getDataFromIntent() {
        jobItem = getIntent().getParcelableExtra(Constance.KEY_JOB_ITEM);
        contno = getIntent().getStringExtra(Constance.KEY_CONTNO);
        try {
            File signFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + jobItem.getOrderid(), "signature_" + jobItem.getOrderid() + ".jpg");
            if (signFile.exists()) {
                pathCustomer = signFile.getAbsolutePath();
                setCustomerSignPath(pathCustomer);
            }

            File signInstaller = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + jobItem.getOrderid(), "signature_" + jobItem.getOrderid() + ".jpg");
            if (signInstaller.exists()) {

            }
        } catch (Exception e) {
            Log.e("sign path", e.getMessage());
        }
    }

    private void setCustomerSignPath(String path) {
        textViewHintSignature1.setVisibility(View.GONE);

        Glide.clear(imageViewCustomerSignature);
        Glide.with(InstallReceiptActivity.this)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageViewCustomerSignature);

        imageViewCustomerSignature.setOnClickListener(null);
    }

    private void setToolbar() {
        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textViewTitle.setText("ใบรับการติดตั้ง");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpContract() {
        textViewNumber.setText(contno);
        jobFinishItem = getPresenter().getFinishData(InstallReceiptActivity.this, jobItem.getOrderid(), contno);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(jobFinishItem.getInstallend());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyy");
        textViewDate.setText(timeFormat.format(myDate));
        textViewOrder.setText(jobItem.getOrderid());
        textViewName.setText(jobItem.getTitle().trim() + "" + jobItem.getFirstName().trim() + " " + jobItem.getLastName().trim());
        textViewID.setText(jobItem.getIDCard());

        getPresenter().getAddressFromSQLite(InstallReceiptActivity.this, jobItem.getOrderid());
    }

    @Override
    public void setAddessFromSQLite(List<AddressItem> itemList) {
        addressItemList = itemList;
        sbAdd = new StringBuilder();
        sbInAdd = new StringBuilder();
        sbAdd.delete(0, sbAdd.length());
        sbInAdd.delete(0, sbAdd.length());
        String phone = null;
        for (AddressItem item : addressItemList) {
            if (item.getAddressType().equals("AddressIDCard")) {
                sbAdd.append(item.getAddrDetail());
                sbAdd.append("\n");
                sbAdd.append("ต." + item.getSubdistrict() + " อ." + item.getDistrict());
                sbAdd.append("\n");
                sbAdd.append("จ." + item.getProvince() + " " + item.getZipcode());

                if (!item.getPhone().isEmpty()) {
                    phone = item.getPhone();
                } else if (!item.getMobile().isEmpty()) {
                    phone = item.getMobile();
                } else if (!item.getOffice().isEmpty()) {
                    phone = item.getOffice();
                } else {
                    phone = jobItem.getContactphone();
                }
            }

            if (item.getAddressType().equals("AddressInstall")) {
                sbInAdd.append(item.getAddrDetail());
                sbInAdd.append("\n");
                sbInAdd.append("ต." + item.getSubdistrict() + " อ." + item.getDistrict());
                sbInAdd.append("\n");
                sbInAdd.append("จ." + item.getProvince() + " " + item.getZipcode());

                if (!item.getPhone().isEmpty()) {
                    phone = item.getPhone();
                } else if (!item.getMobile().isEmpty()) {
                    phone = item.getMobile();
                } else if (!item.getOffice().isEmpty()) {
                    phone = item.getOffice();
                } else {
                    phone = jobItem.getContactphone();
                }
            }
        }

        textViewAddress.setText(sbAdd.toString());
        textViewInstallAddress.setText(sbInAdd.toString());
        textViewPhone.setText(phone);
        textViewCustomerName.setText("(" + jobItem.getTitle() + jobItem.getFirstName() + " " + jobItem.getLastName() + ")");
        textViewInstallerName.setText(
                "(" + MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_FIRSTNAME) + " " + MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_LASTNAME) + ")");
    }

    @Override
    public void jobFinish(boolean boo) {

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

    private View.OnClickListener onSign() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InstallReceiptActivity.this, SignatureActivity.class);
                intent.putExtra("KEY", "installReceipt");
                intent.putExtra(Constance.KEY_ORDER_ID, jobItem.getOrderid());
                startActivityForResult(intent, Constance.REQUEST_SIGNATURE);
            }
        };
    }

    private View.OnClickListener onPrint() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPrintInstallReceipt.startAnimation(new AnimateButton().animbutton());
                //inputStream = ContractActivity.getInputStream();
                //outputStream = ContractActivity.getOutputStream();
                printerAddress = ContractActivity.getDeviceAddress();
                Log.e("print adderss", printerAddress);
                try {
                    printerServer = new PrinterServer(new PrinterServerListener() {
                        @Override
                        public void onConnect(Socket socket) {
                            mSocket = socket;
                            try {
                                inputStream = socket.getInputStream();
                                outputStream = socket.getOutputStream();
                                printText(inputStream, outputStream);
                            } catch (IOException e) {
                                Log.e("printer server", e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void printText(InputStream in, OutputStream out) throws IOException {
        try {
            Printer.setDebug(true);
            EMSR.setDebug(true);
            mProtocolAdapter = new ProtocolAdapter(in, out);
            if (mProtocolAdapter.isProtocolEnabled()) {
                mPrinterChannel = mProtocolAdapter.getChannel(ProtocolAdapter.CHANNEL_PRINTER);
                mPrinter = new Printer(mPrinterChannel.getInputStream(), mPrinterChannel.getOutputStream());
                printInstallationReceipt();
            } else {
                mPrinter = new Printer(mProtocolAdapter.getRawInputStream(), mProtocolAdapter.getRawOutputStream());
                printInstallationReceipt();
            }
        } catch (Exception ex) {

        }
    }

    private void printInstallationReceipt() throws IOException {
        if (bluetoothService != null) {
            themalPrintController.setPrinterController(mPrinter, printerAddress);
            mPrinter.reset();
            themalPrintController.setFontNormal();
            List<List<PrintTextInfo>> documents = new ArrayList<>();
            List<PrintTextInfo> document = documentController.getTextInstallation(jobItem, addressItemList, productItemList, contno);
            documents.add(document);

            for (List<PrintTextInfo> listInfo : documents) {
                for (PrintTextInfo info : listInfo) {
                    if (info.text.equals("printShortHeader")) {
                        themalPrintController.printShortHeader();
                    } else if (info.text.equals("printHeader")) {
                        themalPrintController.printHeader();
                    } else if (info.text.equals("selectPageMode")) {
                        themalPrintController.selectPageMode();
                    } else if (info.text.equals("setContractPageRegion")) {
                        themalPrintController.setContractPageRegion();
                    } else if (info.text.equals("printContractPageTitle")) {
                        themalPrintController.printContractPageTitle();
                    } else if (info.text.equals("beginContractPage")) {
                        themalPrintController.beginContractPage();
                    } else if (info.text.equals("endContractPage")) {
                        themalPrintController.endContractPage();
                    } else if (info.text.equals("selectStandardMode")) {
                        themalPrintController.selectStandardMode();
                    } else if (info.text.contains("setPageRegion")) {
                        themalPrintController.setPageRegion(info.text);
                    } else if (info.text.contains("beginPage")) {
                        themalPrintController.beginPage(info.text);
                    } else if (info.text.contains("endPage")) {
                        themalPrintController.endPage();
                    } else if (info.text.contains("printTitleBackground")) {
                        themalPrintController.printTitleBackground(info.text);
                    } else if (info.text.contains("printFrame")) {
                        themalPrintController.printFrame(info.text);
                    } else if (info.text.equals("customerWithInstaller")) {
                        themalPrintController.printSignatureCustomer(path);
                    }/* else if (info.isBarcode) {
                        if (info.isBankBarcode) {
                            String[] parts = info.text.split("\\|");
                            themalPrintController.printBankBarcode(parts[0], parts[1], parts[2]);
                        } else {
                            themalPrintController.printBarCode128(info.text);
                        }
                    } */else {
                        if (info.language.equals("TH")) {
                            themalPrintController.sendThaiMessage(info.text);
                        } else {
                            themalPrintController.sendEnglishMessage(info.text);
                        }
                    }
                }
            }

            themalPrintController.sendThaiMessage("");
            mPrinter.feedPaper(100);
            mPrinter.flush();

            InstallReceiptActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    buttonFinish.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private Runnable Success = new Runnable() {

        @Override
        public void run() {
            textViewPrintStatus.setVisibility(View.VISIBLE);
            textViewPrintStatus.setText("เชื่อมต่อปริ้นท์เตอร์ " + bluetoothDevice.getName() + " แล้ว");
            textViewPrintStatus.setBackgroundColor(getResources().getColor(R.color.LimeGreen));
        }
    };

    private Runnable Warning = new Runnable() {

        @Override
        public void run() {
            textViewPrintStatus.setVisibility(View.VISIBLE);
            textViewPrintStatus.setText("ไม่ได้เชื่อมต่อปริ้นท์เตอร์");
            textViewPrintStatus.setBackgroundColor(getResources().getColor(R.color.Orange));
        }
    };

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            //connectBluetoothPaired();
                            if (bluetoothService.isAvailable()) {
                                runOnUiThread(Success);
                            }
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Log.e("Handle", "STATE_CONNECTING");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Log.e("Handle", "STATE_LISTEN &&  STATE_NONE");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    Log.e("Handle", "MESSAGE_CONNECTION_LOST");
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    Log.e("Handle", "MESSAGE_UNABLE_CONNECT");
                    break;
            }
        }

    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                textViewPrintStatus.setText("เชื่อมต่อปริ้นท์เตอร์ " + bluetoothDevice.getName() + " แล้ว");
                textViewPrintStatus.setBackgroundColor(getResources().getColor(R.color.LimeGreen));
            }  else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                textViewPrintStatus.setText("ไม่ได้เชื่อมต่อปริ้นท์เตอร์");
                textViewPrintStatus.setBackgroundColor(getResources().getColor(R.color.Orange));
            }
        }
    };

    /*private synchronized void waitForConnection() {
        if (bluetoothSocket == null) {
            final Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.bluetoothSettings");
            intent.setComponent(cn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
        }
        try {
            printerServer = new PrinterServer(new PrinterServerListener() {
                @Override
                public void onConnect(Socket socket) {
                    mSocket = socket;
                    try {
                        inputStream = socket.getInputStream();
                        outputStream = socket.getOutputStream();
                        printText(inputStream, outputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(receiver);
            if (bluetoothService != null) {
                bluetoothService.stop();
            }
            bluetoothService = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bt_menu, menu);
        return true;
    }
}
