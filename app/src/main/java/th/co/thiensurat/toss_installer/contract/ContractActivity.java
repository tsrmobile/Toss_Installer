package th.co.thiensurat.toss_installer.contract;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.gson.Gson;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import th.co.thiensurat.toss_installer.MainActivity;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.contract.adapter.ContactItemAdapter;
import th.co.thiensurat.toss_installer.contract.item.ContactItem;
import th.co.thiensurat.toss_installer.contract.item.ObjectImage;
import th.co.thiensurat.toss_installer.contract.printer.documentcontroller.DocumentController;
import th.co.thiensurat.toss_installer.contract.printer.documentcontroller.PrintTextInfo;
import th.co.thiensurat.toss_installer.contract.printer.documentcontroller.ThemalPrintController;
import th.co.thiensurat.toss_installer.contract.printer.utils.PrinterServer;
import th.co.thiensurat.toss_installer.contract.printer.utils.PrinterServerListener;
import th.co.thiensurat.toss_installer.contract.signaturepad.SignatureActivity;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.ImageConfiguration;
import th.co.thiensurat.toss_installer.utils.MyApplication;
import th.co.thiensurat.toss_installer.utils.Utils;

public class ContractActivity extends BaseMvpActivity<ContractInterface.Presenter>
        implements ContractInterface.View {

    private JobItem jobItem;
    private ContactItem contactItem;
    private List<ProductItem> productItemList = new ArrayList<ProductItem>();
    private List<AddressItem> addressItemList = new ArrayList<AddressItem>();

    private String name;
    private String path;
    private String serial;
    private String number;
    private String printType;
    private String printAddress;
    private String pathCustomer;
    private StringBuilder sbAdd;
    private StringBuilder sbInAdd;
    private TextView textViewTitle;
    private CustomDialog customDialog;
    private ImageConfiguration imageConfiguration;
    private DocumentController documentController;
    private ThemalPrintController themalPrintController;

    private Printer mPrinter;
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

    private String empid;
    private File empSign;
    private File witnessPath;
    private File customerPath;
    /*private StringBuilder sb1;
    private StringBuilder sb2;
    private StringBuilder sb3;
    private DecimalFormat df = new DecimalFormat("#,###.00");*/
    private ContactItemAdapter adapter;
    private LinearLayoutManager layoutManager;

    public static ContractActivity getInstance() {
        return new ContractActivity();
    }

    @Override
    public ContractInterface.Presenter createPresenter() {
        return ContractPresenter.create(ContractActivity.this);
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_contract;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.printer_status) TextView textViewPrintStatus;
    @BindView(R.id.floating_print) Button buttonPrintContact;
    @BindView(R.id.floating_finish) Button buttonFinish;
    @BindView(R.id.floating_print_install_receipt) Button buttonInstallReceipt;
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
    @BindView(R.id.textview_1) TextView textView1;
    @BindView(R.id.textview_2) TextView textView2;
    @BindView(R.id.customer_name) TextView textViewCustomerName;
    @BindView(R.id.customer_signature) ImageView imageViewCustomerSignature;
    @BindView(R.id.signature_1) ImageView imageViewSignature1;
    @BindView(R.id.signature_2) ImageView imageViewSignature2;
    @BindView(R.id.signature_1_name) TextView textViewSignature1;
    @BindView(R.id.signature_2_name) TextView textViewSignature2;
    @BindView(R.id.layout_sign_bottom) LinearLayout linearLayoutSignBottom;
    @BindView(R.id.signature_k_viruch) ImageView imageViewSignatureKViruch;
    @BindView(R.id.layout_periods) LinearLayout linearLayoutPeriods;
    @BindView(R.id.layout_permonth) LinearLayout linearLayoutPermonth;

    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
        buttonFinish.setOnClickListener( onFinish() );
        buttonPrintContact.setOnClickListener( onPrint() );
        imageViewCustomerSignature.setOnClickListener( onSign() );
        buttonInstallReceipt.setOnClickListener( onPrintInstallReceipt() );
    }

    @Override
    public void setupInstance() {
        adapter = new ContactItemAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        customDialog = new CustomDialog(ContractActivity.this);
        imageConfiguration = new ImageConfiguration(ContractActivity.this);
        documentController = new DocumentController(ContractActivity.this);
    }

    @Override
    public void setupView() {
        setToolbar();
        setRecyclerView();
    }

    @Override
    public void initialize() {
        getDataFromIntent();
        setUpContract();

        //connectBluetoothPaired();
        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(receiver, filter);
    }

    private void setToolbar() {
        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textViewTitle.setText("สัญญาเช่าซื้อ/ซื้อขาย");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getDataFromIntent() {
        jobItem = getIntent().getParcelableExtra(Constance.KEY_JOB_ITEM);

        try {
            File signFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + jobItem.getOrderid(), "signature_" + jobItem.getOrderid() + ".jpg");
            if (signFile.exists()) {
                path = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + jobItem.getOrderid(), "signature_" + jobItem.getOrderid() + "_contact.jpg").getAbsolutePath();
                pathCustomer = signFile.getAbsolutePath();
                setSignToImageView(pathCustomer);
            }


            empid = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID);
            empSign = new File(imageConfiguration.getAlbumStorageDir(empid), String.format("signature_%s.jpg", empid));
            Glide.clear(imageViewSignature1);
            Glide.with(ContractActivity.this)
                    .load(empSign.getAbsolutePath())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageViewSignature1);

            witnessPath = new File(imageConfiguration.getAlbumStorageDir(empid), String.format("signature_witness.jpg"));

            textViewSignature1.setText("(" +
                    MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_TITLE) + MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_FIRSTNAME)
                    + " " + MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_LASTNAME) + ")");

            textViewSignature2.setText("(" +jobItem.getPresale() + ")");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = true;
            AssetManager assetManager = getAssets();
            Bitmap bitmap = BitmapFactory.decodeStream(assetManager.open("witness.png"), null, options);
            imageViewSignature2.setImageBitmap(bitmap);

        } catch (IOException e) {
            Log.e("sign path", e.getMessage());
        }


        getPresenter().getContactNumber(jobItem.getOrderid());
    }

    private void setUpContract() {
        textViewOrder.setText(jobItem.getOrderid());
        name = jobItem.getTitle().trim() + "" + jobItem.getFirstName().trim() + " " + jobItem.getLastName().trim();
        textViewName.setText(name);
        textViewID.setText(jobItem.getIDCard());
        textViewSignature2.setText(jobItem.getPresale());

        getPresenter().getAddressFromSQLite(jobItem.getOrderid());
        getPresenter().getProductFromSQLite(jobItem.getOrderid());
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
    }

    @Override
    public void setContactNumber(String num) {
        String[] str = num.split("\\|");
        try {
            this.number = str[0];
            textViewNumber.setText(number);
            textViewDate.setText(Utils.ConvertDateFormat(str[1]));
        } catch (Exception ex) {
            Log.e("get contact", ex.getMessage());
        }
        getPresenter().updateContactNumber(jobItem.getOrderid(), number);
    }

    @Override
    public void setProductFromSQLite(List<ProductItem> productItemList) {
        this.productItemList = productItemList;
        adapter.setContactItem(productItemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        /*String temp = "";
        String temp2 = "";
        sb1 = new StringBuilder();
        sb2 = new StringBuilder();
        sb3 = new StringBuilder();*/

        /*StringBuilder stringBuilder = new StringBuilder();
        int i = 0;*/
        for (ProductItem item : productItemList) {

            /*if (temp.isEmpty()) {
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
            }*/

            if (item.getProductPayType().equals("1")) {
                textView1.setText("ผู้ขาย");
                textView2.setText("ผู้ซื้อ");
                linearLayoutSignBottom.setVisibility(View.GONE);
                linearLayoutPeriods.setVisibility(View.GONE);
                linearLayoutPermonth.setVisibility(View.GONE);
            } else {
                textView1.setText("ผู้ให้เช่าซื้อ");
                textView2.setText("ผู้เช่าซื้อ");
            }

            try {
                if (item.getProductPrintContact().equals("0")
                        && item.getProductPrintInstall().equals("0") && jobItem.getStatus().equals("22")) {
                    buttonPrintContact.setVisibility(View.VISIBLE);
                    buttonInstallReceipt.setVisibility(View.GONE);
                    buttonFinish.setVisibility(View.GONE);
                } else if (item.getProductPrintContact().equals("1")
                        && item.getProductPrintInstall().equals("0") && jobItem.getStatus().equals("22")) {
                    buttonInstallReceipt.setVisibility(View.VISIBLE);
                    buttonFinish.setVisibility(View.GONE);
                } else if (item.getProductPrintContact().equals("1")
                        && item.getProductPrintInstall().equals("1") && jobItem.getStatus().equals("22")) {
                    buttonPrintContact.setVisibility(View.GONE);
                    buttonInstallReceipt.setVisibility(View.GONE);
                    buttonFinish.setVisibility(View.VISIBLE);
                } else if (item.getProductPrintContact().equals("1")
                        && item.getProductPrintInstall().equals("1") && jobItem.getStatus().equals("01")) {
                    buttonPrintContact.setVisibility(View.VISIBLE);
                    buttonInstallReceipt.setVisibility(View.GONE);
                    buttonFinish.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                connectBluetoothPaired();
            }

            //i++;
        }

        /*textViewProductName.setText(sb1.toString());
        textViewProductModel.setText(sb2.toString());
        textViewProductSerial.setText(sb3.toString());

        String preriod = "";
        String permonth = "";
        float qty = 0;
        float normalPrice = 0;
        float discountPrice = 0;
        float grandTotalPrice = 0;
        for (int i = 0; i < productItemList.size(); i++) {
            ProductItem item = productItemList.get(i);
            qty += Float.parseFloat(item.getProductQty());
            normalPrice = Float.parseFloat(item.getProductPrice());
            discountPrice = Float.parseFloat(item.getProductDiscount());
            preriod = item.getProductPayPeriods();
            permonth = item.getProductPayPerPeriods();
        }
        grandTotalPrice = (qty * normalPrice) - (qty * discountPrice);*/


        /*textViewPrice.setText(df.format((qty * normalPrice)) + " บาท");
        textViewDiscount.setText(df.format((qty * discountPrice)) + " บาท");
        textViewNetPrice.setText(df.format(grandTotalPrice) + " บาท");
        textViewMonth.setText(preriod + " บาท");
        textViewPerMonth.setText(permonth + " บาท");*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
        } else if (item.getItemId() == R.id.menu_bt) {
            connectBluetoothPaired();
        } else if (item.getItemId() == R.id.menu_home) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoad() {
        customDialog.dialogLoading();
    }

    @Override
    public void onLongLoad() {
        customDialog.dialogLongLoading();
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
        try {
            if (printType.equals("contact") && !jobItem.getStatus().equals("01")) {
                getPresenter().updatePrintStatus(jobItem.getOrderid(), Constance.printContactStatus);
                buttonInstallReceipt.setVisibility(View.VISIBLE);
            } else if (printType.equals("installreceipt")) {
                getPresenter().updatePrintStatus(jobItem.getOrderid(), Constance.printInstallStatus);
                buttonInstallReceipt.setVisibility(View.GONE);
                buttonFinish.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e("job finish exception", e.getMessage());
        }
    }

    @Override
    public void onJobClosed() {
        startActivity(new Intent(ContractActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onUploadSuccess(String updated) {
        customDialog.dialogSuccess(updated);

    }

    public void addStep() {
        getPresenter().updateStep(jobItem.getOrderid(), Constance.STEP_7);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constance.REQUEST_BLUETOOTH_SETTINGS) {
            connectBluetoothPaired();
        }

        if (requestCode == Constance.REQUEST_SIGNATURE) {
            if (resultCode == RESULT_OK) {
                String status = data.getStringExtra("status");
                if(status.equalsIgnoreCase("done")){
                    path = data.getStringExtra("pathSignContact");
                    pathCustomer = data.getStringExtra("pathCustomer");
                    setSignToImageView(pathCustomer);
                }
            }
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

        imageViewCustomerSignature.setOnClickListener( onSign() );
    }

    private View.OnClickListener onPrint() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPrintContact.startAnimation(new AnimateButton().animbutton());
                printType = "contact";
                printText();
            }
        };
    }

    private View.OnClickListener onPrintInstallReceipt() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonInstallReceipt.startAnimation(new AnimateButton().animbutton());
                printType = "installreceipt";
                printText();
            }
        };
    }

    private View.OnClickListener onSign() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContractActivity.this, SignatureActivity.class);
                intent.putExtra(Constance.KEY_ORDER_ID, jobItem.getOrderid());
                startActivityForResult(intent, Constance.REQUEST_SIGNATURE);
            }
        };
    }

    private View.OnClickListener onFinish() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonFinish.startAnimation(new AnimateButton().animbutton());
                onGetImage(jobItem.getOrderid());
            }
        };
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
                    ContractActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            customDialog.dialogFail("การเชื่อมต่อไม่สมบูรณ์\nกรุณาปิดและเปิดปริ้นท์เตอร์\nเพื่อเชื่อมต่อใหม่");
                            connectBluetoothPaired();
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

    private interface PrinterRunnable {
        void print(CustomDialog customDialog, Printer printer) throws IOException;
    }

    private void printText() {
        printTask(new PrinterRunnable() {
            @Override
            public void print(CustomDialog customDialog, Printer printer) throws IOException {
                themalPrintController = new ThemalPrintController(ContractActivity.this, printer, printAddress);
                List<List<PrintTextInfo>> documents = new ArrayList<>();
                List<PrintTextInfo> document;
                if (printType.equals("contact")) {
                    document = documentController.getTextContract(jobItem, addressItemList, productItemList, number);
                } else {
                    document = documentController.getTextInstallation(jobItem, addressItemList, productItemList, number);
                }
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
                        } else if (info.text.equals("k_viruchWithCustomer")) {
                            themalPrintController.printSignatureKViruchWithCustomer(path);
                        } else if (info.text.equals("customerWithInstaller")) {
                            themalPrintController.printSignatureInstallerWithCustomer(generateSignForPrintReceiptInstallation());
                        } else if (info.text.equals("signatureWitness")) {
                            themalPrintController.printSignatureKViruchWithCustomer(witnessPath.getAbsolutePath());
                        } else {
                            if (info.language.equals("TH")) {
                                themalPrintController.sendThaiMessage(info.text);
                            } else {
                                themalPrintController.sendEnglishMessage(info.text);
                            }
                        }
                    }
                }

                printer.reset();
                printer.feedPaper(110);
                printer.flush();
                ContractActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        onSuccess("");
                    }
                });
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        closeBluetoothConnection();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bt_menu, menu);
        return true;
    }

    /*********************************Connection bluetooth device**********************************/
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
        closeBluetoothConnection();
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

    /*********************************Generate signature installer with customer**********************************/
    private String generateSignForPrintReceiptInstallation() {
        File installationPath = new File(imageConfiguration.getAlbumStorageDir(jobItem.getOrderid()), String.format("signature_%s_install.jpg", jobItem.getOrderid()));
        try {
            empid = MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID);
            empSign = new File(imageConfiguration.getAlbumStorageDir(empid), String.format("signature_%s.jpg", empid));
            customerPath = new File(imageConfiguration.getAlbumStorageDir(jobItem.getOrderid()), String.format("signature_%s.jpg", jobItem.getOrderid()));

            Bitmap bmp1 = BitmapFactory.decodeFile(customerPath.getAbsolutePath());
            Bitmap bmp2 = BitmapFactory.decodeFile(empSign.getAbsolutePath());
            createSingleImageFromMultipleImages(getResizedBitmap(bmp1, 210, 71), getResizedBitmap(bmp2, 210, 71), installationPath);
        } catch (Exception e) {
        }

        return installationPath.getAbsolutePath();
    }

    private void createSingleImageFromMultipleImages(Bitmap firstImage, Bitmap secondImage, File path) throws IOException {
        Bitmap result = Bitmap.createBitmap((firstImage.getWidth() + secondImage.getWidth()), firstImage.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(firstImage, 0, 0, null);
        canvas.drawBitmap(secondImage, firstImage.getWidth(), 0, null);
        OutputStream stream = new FileOutputStream(path);
        result.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        stream.close();
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
    /***********************************************************************************************/

    /***********************************Upload to server********************************************/
    @Override
    public void onGetImage(String orderid) {
        getPresenter().getAllImage(orderid);
    }

    @Override
    public void setImageToContactBody(List<ObjectImage> objectImages) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        List<ObjectImage> objectImageList = new ArrayList<>();
        for (int i = 0; i < objectImages.size(); i++) {
            ObjectImage image = objectImages.get(i);
            File f = new File(image.getImageName());
            Uri uri = Uri.fromFile(f);
            File file = FileUtils.getFile(ContractActivity.this, uri);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file[]", file.getName(), requestFile);
            parts.add(body);

            objectImageList.add(new ObjectImage()
                    .setType(image.getType())
                    .setImageName(file.getName())
                    .setProductCode(image.getProductCode())
            );
        }

        contactItem = new ContactItem()
                .setOrderid(jobItem.getOrderid())
                .setEmpid(MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID))
                .setInstalldate(getPresenter().getInstallDate(jobItem.getOrderid()))
                .setInstallend(getPresenter().getInstallEnd(jobItem.getOrderid()))
                .setImages(objectImageList);

        String patientData = jobItem.getOrderid();
        RequestBody requestBody = RequestBody.create(okhttp3.MultipartBody.FORM, patientData);

        getPresenter().uploadImageToServer(requestBody, parts);
    }

    @Override
    public void uploadData() {
        Gson gson = new Gson();
        String patientData = gson.toJson(contactItem);
        RequestBody requestBody = RequestBody.create(okhttp3.MultipartBody.FORM, patientData);
        getPresenter().uploadDataToServer(requestBody);
    }
    /***********************************************************************************************/
}