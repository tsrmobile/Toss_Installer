package th.co.thiensurat.toss_installer.contract;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.datecs.api.emsr.EMSR;
import com.datecs.api.printer.Printer;
import com.datecs.api.printer.ProtocolAdapter;
import com.zj.btsdk.BluetoothService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Body;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.api.result.ContactResultGroup;
import th.co.thiensurat.toss_installer.api.result.JobFinishItem;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.contract.signaturepad.SignatureActivity;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.job.item.ProductItem;
import th.co.thiensurat.toss_installer.printer.documentcontroller.DocumentController;
import th.co.thiensurat.toss_installer.printer.documentcontroller.PrintTextInfo;
import th.co.thiensurat.toss_installer.printer.bluetoothDevice.BluetoothDeviceActivity;
import th.co.thiensurat.toss_installer.printer.bluetoothDevice.PrinterServer;
import th.co.thiensurat.toss_installer.printer.bluetoothDevice.PrinterServerListener;
import th.co.thiensurat.toss_installer.printer.documentcontroller.ThemalPrintController;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.MyApplication;
import th.co.thiensurat.toss_installer.utils.Utils;
import th.co.thiensurat.toss_installer.utils.svg.SvgDecoder;
import th.co.thiensurat.toss_installer.utils.svg.SvgDrawableTranscoder;
import th.co.thiensurat.toss_installer.utils.svg.SvgSoftwareLayerSetter;

import static th.co.thiensurat.toss_installer.utils.Constance.REQUEST_CONNECT_DEVICE;

public class ContractActivity extends BaseMvpActivity<ContractInterface.Presenter>
        implements ContractInterface.View {

    private JobItem jobItem;
    private JobFinishItem jobFinishItem;
    private List<ProductItem> productItemList = new ArrayList<ProductItem>();
    private List<AddressItem> addressItemList = new ArrayList<AddressItem>();

    private String name;
    private String path;
    private String serial;
    private String number;
    private String printType;
    private String printerAddr;
    private StringBuilder sbAdd;
    private StringBuilder sbInAdd;
    private TextView textViewTitle;
    private CustomDialog customDialog;
    private DocumentController documentController;

    private BluetoothAdapter bluetoothAdapter;
    private static BluetoothSocket bluetoothSocket;

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
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

    public static ContractActivity getInstance() {
        return new ContractActivity();
    }

    @Override
    public ContractInterface.Presenter createPresenter() {
        return ContractPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_contract;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.printer_status) TextView textViewPrintStatus;
    @BindView(R.id.floating_print) Button buttonPrintContact;
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
    @BindView(R.id.contract_product_price) TextView textViewPrice;
    @BindView(R.id.contract_product_discount) TextView textViewDiscount;
    @BindView(R.id.contract_product_net_price) TextView textViewNetPrice;
    @BindView(R.id.contract_product_month) TextView textViewMonth;
    @BindView(R.id.contract_price_per_month) TextView textViewPerMonth;
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
    @BindView(R.id.title_price) TextView textViewTitlePrice;
    @BindView(R.id.title_discount) TextView textViewTitleDiscount;
    @BindView(R.id.title_net_price) TextView textViewTitleNetPrice;
    @BindView(R.id.title_per_month) TextView textViewTitlePerMonth;
    @BindView(R.id.hint_signature) TextView textViewHintSignature;
    @BindView(R.id.customer_name) TextView textViewCustomerName;
    @BindView(R.id.customer_signature) ImageView imageViewCustomerSignature;
    @BindView(R.id.signature_1_name) TextView textViewSignature1;
    @BindView(R.id.signature_2_name) TextView textViewSignature2;
    @BindView(R.id.layout_sign_bottom) LinearLayout linearLayoutSignBottom;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
        textViewPrintStatus.setVisibility(View.GONE);
        buttonFinish.setOnClickListener( onFinish() );
        buttonPrintContact.setOnClickListener( onPrint() );
        imageViewCustomerSignature.setOnClickListener( onSign() );
        buttonPrintInstallReceipt.setOnClickListener( onPrintInstallationReceipt() );
    }

    @Override
    public void setupInstance() {
        customDialog = new CustomDialog(ContractActivity.this);
        documentController = new DocumentController(ContractActivity.this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        themalPrintController = new ThemalPrintController(ContractActivity.this);
        bluetoothService = new BluetoothService(ContractActivity.this, handler);
    }

    @Override
    public void setupView() {
        setToolbar();
    }

    @Override
    public void initialize() {
        getDataFromIntent();
        setUpContract();
        getPresenter().getContactNumber();
        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(receiver, filter);

        if (jobItem.getStatus().equals("01")) {
            buttonFinish.setVisibility(View.GONE);
            buttonPrintContact.setVisibility(View.VISIBLE);
            buttonPrintInstallReceipt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeBlutoothConnection();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(receiver, filter);
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

    private void setToolbar() {
        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textViewTitle.setText("สัญญาเช่าซื้อ/ซื้อขาย");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getDataFromIntent() {
        jobItem = getIntent().getParcelableExtra(Constance.KEY_JOB_ITEM);
        serial = getIntent().getStringExtra(Constance.KEY_SERIAL_ITEM);
    }

    private void setUpContract() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());
        textViewDate.setText(Utils.ConvertDateFormat(currentDate));
        textViewNumber.setText("");
        textViewOrder.setText(jobItem.getOrderid());
        name = jobItem.getTitle().trim() + "" + jobItem.getFirstName().trim() + " " + jobItem.getLastName().trim();
        textViewName.setText(name);
        textViewID.setText(jobItem.getIDCard());

        getPresenter().getAddressFromSQLite(ContractActivity.this, jobItem.getOrderid());
        getPresenter().getProductFromSQLite(ContractActivity.this, jobItem.getOrderid());
    }

    @Override
    public void setAddessFromSQLite(List<AddressItem> itemList) {
        this.addressItemList = itemList;
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
        textViewSignature1.setText("(" + jobItem.getPresale() + ")");
        textViewSignature2.setText(
                "(" + MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_FIRSTNAME) + " " + MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_LASTNAME) + ")");
    }

    @Override
    public void setContactNumber(String number) {
        this.number = number;
        textViewNumber.setText(number);
    }

    @Override
    public void setProductFromSQLite(List<ProductItem> productItemList) {
        this.productItemList = productItemList;
        String temp = "";
        String temp2 = "";
        String productname = "";
        String prodcutmodel = "";
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();

        for (ProductItem item : productItemList) {
            if (temp.isEmpty()) {
                temp = item.getProductName();
            } else {
                temp2 = temp;
                temp = item.getProductName();
            }

            if (!temp2.equals(temp) || temp2.isEmpty()) {
                sb1.append(item.getProductName());
                sb1.append("\n");
                sb2.append(item.getProductModel());
                sb2.append("\n");
                sb3.append(item.getProductSerial());
                sb3.append("\n");
            } else {
                sb1.append(item.getProductName());
                sb1.append("\n");
                sb2.append(item.getProductModel());
                sb2.append("\n");
                sb3.append(item.getProductSerial());
                sb3.append("\n");
            }

            if (item.getProductPayType().equals("1")) {
                textViewMonth.setVisibility(View.GONE);
                textViewPerMonth.setVisibility(View.GONE);
                linearLayoutSignBottom.setVisibility(View.GONE);
            }
        }

        textViewProductName.setText(sb1.toString());
        textViewProductModel.setText(sb2.toString());
        textViewProductSerial.setText(sb3.toString());

        float qty = 0;
        float normalPrice = 0;
        float discountPrice = 0;
        float grandTotalPrice = 0;
        for (int i = 0; i < productItemList.size(); i++) {
            ProductItem item = productItemList.get(i);
            qty += Float.parseFloat(item.getProductQty());
            normalPrice = Float.parseFloat(item.getProductPrice());
            discountPrice = Float.parseFloat(item.getProductDiscount());
        }
        grandTotalPrice = (qty * normalPrice) - (qty * discountPrice);
        textViewPrice.setText(df.format((qty * normalPrice)) + " บาท");
        textViewDiscount.setText(df.format((qty * discountPrice)) + " บาท");
        textViewNetPrice.setText(df.format(grandTotalPrice) + " บาท");
    }

    private View.OnClickListener onPrint() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPrintContact.startAnimation(new AnimateButton().animbutton());
                printType = "contract";
                try {
                    printText(inputStream, outputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private View.OnClickListener onPrintInstallationReceipt() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPrintInstallReceipt.startAnimation(new AnimateButton().animbutton());
                printType = "install";
                try {
                    printText(inputStream, outputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private View.OnClickListener onSign() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContractActivity.this, SignatureActivity.class);
                intent.putExtra(Constance.KEY_ORDER_ID, jobItem.getOrderid());
                intent.putExtra(Constance.KEY_CUSTOMER_NAME, String.valueOf(jobItem.getFirstName() + "_" + jobItem.getLastName()));
                startActivityForResult(intent, Constance.REQUEST_SIGNATURE);
            }
        };
    }

    private View.OnClickListener onFinish() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonFinish.startAnimation(new AnimateButton().animbutton());
                getPresenter().jobFinish(ContractActivity.this, jobItem.getOrderid(), number);
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (item.getItemId() == R.id.menu_bt) {
            connectBluetoothPaired();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constance.REQUEST_BLUETOOTH_SETTINGS) {
                Intent serverIntent = new Intent(ContractActivity.this, BluetoothDeviceActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            }

            if (requestCode == REQUEST_CONNECT_DEVICE) {
                if (resultCode == RESULT_OK) {
                    String address = data.getStringExtra(Constance.EXTRA_DEVICE_ADDRESS);
                    this.printerAddr = address;
                    bluetoothSocket = BluetoothDeviceActivity.getSocket();
                    /*try {
                        if (BluetoothAdapter.checkBluetoothAddress(printerAddr)) {
                            establishBluetoothConnection(printerAddr);
                        } else {
                            establishNetworkConnection(printerAddr);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Bluetooth Connect Failed", Toast.LENGTH_SHORT).show();
                    }*/
                }
            }

            if (requestCode == Constance.REQUEST_SIGNATURE) {
                if (resultCode == RESULT_OK) {
                    String status = data.getStringExtra("status");
                    if(status.equalsIgnoreCase("done")){
                        path = data.getStringExtra("path");
                        setSignToImageView(path);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSignToImageView(String pathBMP) {
        textViewHintSignature.setVisibility(View.GONE);
        Glide.clear(imageViewCustomerSignature);
        Glide.with(ContractActivity.this)
                .load(pathBMP)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageViewCustomerSignature);
    }

    private void connectBluetoothPaired() {
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                customDialog.dialogLoading();
                pairedDevices = bluetoothAdapter.getBondedDevices();
                if (pairedDevices.size() > 0) {
                    for (final BluetoothDevice device : pairedDevices) {
                        connectThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                bluetoothAdapter.cancelDiscovery();

                                try {
                                    uuid = UUID.fromString(Constance.UUID);
                                    bluetoothDevice = bluetoothAdapter.getRemoteDevice(device.getAddress());
                                    try {
                                        bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                                        bluetoothSocket.connect();
                                        runOnUiThread(Success);
                                        customDialog.dialogDimiss();
                                        printerAddr = device.getAddress();
                                        inputStream = bluetoothSocket.getInputStream();
                                        outputStream = bluetoothSocket.getOutputStream();
                                        Log.e("print address", printerAddr);
                                    } catch (IOException ie) {
                                        runOnUiThread(Warning);
                                        Log.e("error bluetooth paired", ie.getMessage());
                                        ie.printStackTrace();
                                        return;
                                    }
                                    runOnUiThread(Success);
                                } finally {
                                    customDialog.dialogDimiss();
                                }
                            }
                        });
                        connectThread.start();
                    }
                } else {
                    waitForConnection();
                }
            } else {
                Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intentBtEnabled, Constance.REQUEST_BLUETOOTH_SETTINGS);
            }
        }
    }

    private void establishBluetoothConnection(final String address) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        connectThread = new Thread(new Runnable() {
            @Override
            public void run() {
                bluetoothAdapter.cancelDiscovery();

                try {
                    uuid = UUID.fromString(Constance.UUID);
                    bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);

                    try {
                        bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                        bluetoothSocket.connect();
                        printerAddr = address;
                        inputStream = bluetoothSocket.getInputStream();
                        outputStream = bluetoothSocket.getOutputStream();
                    } catch (IOException ie) {
                        runOnUiThread(Warning);
                        waitForConnection();
                        Log.e("error bluetooth", ie.getMessage());
                        return;
                    }
                    runOnUiThread(Success);
                } finally {

                }
            }
        });
        connectThread.start();
    }

    private static final int DEFAULT_NETWORK_PORT = 9100;
    private void establishNetworkConnection(final String address) {
        connectThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = null;
                    try {
                        String[] url = address.split(":");
                        int port = DEFAULT_NETWORK_PORT;
                        try {
                            if (url.length > 1) {
                                port = Integer.parseInt(url[1]);
                            }

                        } catch (NumberFormatException e) {
                        }
                        socket = new Socket(url[0], port);
                        socket.setKeepAlive(true);
                        socket.setTcpNoDelay(true);
                    } catch (UnknownHostException e) {
                        waitForConnection();
                        return;
                    } catch (IOException e) {
                        waitForConnection();
                        return;
                    }

                    try {
                        mSocket = socket;
                        inputStream = mSocket.getInputStream();
                        outputStream = mSocket.getOutputStream();
                    } catch (IOException e) {
                        waitForConnection();
                        return;
                    }
                    runOnUiThread(Success);
                } finally {

                }
            }
        });
        connectThread.start();
    }

    private void printText(InputStream in, OutputStream out) throws IOException {
        try {
            Printer.setDebug(true);
            EMSR.setDebug(true);
            mProtocolAdapter = new ProtocolAdapter(in, out);
            if (mProtocolAdapter.isProtocolEnabled()) {
                mPrinterChannel = mProtocolAdapter.getChannel(ProtocolAdapter.CHANNEL_PRINTER);
                mPrinter = new Printer(mPrinterChannel.getInputStream(), mPrinterChannel.getOutputStream());
                if (printType.equals("contract")) {
                    printContract();
                } else {
                    printInstallationReceipt();
                }
            } else {
                mPrinter = new Printer(mProtocolAdapter.getRawInputStream(), mProtocolAdapter.getRawOutputStream());
                if (printType.equals("contract")) {
                    printContract();
                } else {
                    printInstallationReceipt();
                }
            }
        } catch (Exception ex) {

        }
    }

    private void printContract() throws IOException {
        if (bluetoothService != null) {
            themalPrintController.setPrinterController(mPrinter, printerAddr);
            mPrinter.reset();
            themalPrintController.setFontNormal();

            List<List<PrintTextInfo>> documents = new ArrayList<>();
            List<PrintTextInfo> document = documentController.getTextContract(jobItem, addressItemList, productItemList, number);
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
                    } else if (info.text.equals("signature")) {
                        themalPrintController.printSignature("");
                    } else if (info.text.equals("customersignature")) {
                        themalPrintController.printSignatureCustomer(path);
                    } else if (info.isBarcode) {
                        if (info.isBankBarcode) {
                            String[] parts = info.text.split("\\|");
                            themalPrintController.printBankBarcode(parts[0], parts[1], parts[2]);

                        } else {
                            themalPrintController.printBarCode128(info.text);
                        }
                    } else {
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

            ContractActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    //Toast.makeText(getApplicationContext(), "พิมพ์สัญญาแล้ว", Toast.LENGTH_SHORT).show();
                    buttonPrintInstallReceipt.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void printInstallationReceipt() throws IOException {
        if (bluetoothService != null) {
            themalPrintController.setPrinterAddress(mPrinter);
            mPrinter.reset();
            themalPrintController.setFontNormal();

            List<List<PrintTextInfo>> documents = new ArrayList<>();
            List<PrintTextInfo> document = documentController.getTextInstallation(jobItem, addressItemList, productItemList, number);
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
                    } else if (info.text.equals("signature")) {
                        themalPrintController.printSignature("");
                    } else if (info.isBarcode) {
                        if (info.isBankBarcode) {
                            String[] parts = info.text.split("\\|");
                            themalPrintController.printBankBarcode(parts[0], parts[1], parts[2]);
                        } else {
                            themalPrintController.printBarCode128(info.text);
                        }
                    } else {
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

            ContractActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "พิมพ์ใบรับการติดตั้งแล้ว", Toast.LENGTH_SHORT).show();
                    buttonPrintInstallReceipt.setVisibility(View.GONE);
                    buttonPrintContact.setVisibility(View.GONE);
                    buttonFinish.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void jobFinish(boolean boo) {
        if (boo) {
            this.jobFinishItem = getPresenter().getFinishData(ContractActivity.this, jobItem.getOrderid());
            getPresenter().requestUpdateJobFinish(jobFinishItem);
        } else {
            customDialog.dialogFail("พบข้อผิดพลาดระหว่างการอัพเดท!");
        }
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
            textViewPrintStatus.setBackgroundColor(getResources().getColor(R.color.Red));
        }
    };

    private synchronized void closeBlutoothConnection() {
        BluetoothSocket s = bluetoothSocket;
        bluetoothSocket = null;
        if (s != null) {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                textViewPrintStatus.setText("ไม่ได้เชื่อมต่อปริ้นท์เตอร์");
                textViewPrintStatus.setBackgroundColor(getResources().getColor(R.color.Red));
            }
        }
    };

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            //Toast.makeText(getApplicationContext(), "Connect successful",Toast.LENGTH_SHORT).show();
                            //Log.d(LOG_TAG, "STATE_CONNECTED");
                            connectBluetoothPaired();
                            if (bluetoothService.isAvailable()) {
                                //if (mJob != null) doJob(mJob, R.string.bluetooth_printing);
                                /*try {
                                    printText(inputStream, outputStream);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }*/
                                runOnUiThread(Success);
                            }
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            //Log.d(LOG_TAG, "STATE_CONNECTING");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            //Log.d(LOG_TAG, "STATE_LISTEN &&  STATE_NONE");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    Log.d("Handle", "MESSAGE_CONNECTION_LOST");
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    //Toast.makeText(getApplicationContext(), "Unable to connect device", Toast.LENGTH_SHORT).show();
                    //Log.d(LOG_TAG, "MESSAGE_UNABLE_CONNECT");
                    //mJob = null;
                    break;
            }
        }

    };

    private synchronized void waitForConnection() {
        if (bluetoothSocket == null) {
            startActivityForResult(new Intent(ContractActivity.this, BluetoothDeviceActivity.class), REQUEST_CONNECT_DEVICE);
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
    }
}
