package th.co.thiensurat.toss_installer.systemnew.detail;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.RequestBody;
import th.co.thiensurat.toss_installer.MainActivity;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.contract.item.ContactItem;
import th.co.thiensurat.toss_installer.installation.InstallationActivity;
import th.co.thiensurat.toss_installer.installation.camera.CaptureActivityPortrait;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.MyApplication;

public class NewDetailActivity extends BaseMvpActivity<NewDetailInterface.Presenter> implements NewDetailInterface.View{

    private String type;
    private String orderid;
    private String serial;
    private StringBuilder sb;
    private TextView textViewTitle;
    private ContactItem contactItem;
    private CustomDialog customDialog;
    private List<JobItem> jobItemList;
    private List<ProductItem> productItemList;
    private List<AddressItem> addressItemList;

    @Override
    public NewDetailInterface.Presenter createPresenter() {
        return NewDetailPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_detail2;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.textview_user) TextView textViewUsername;
    @BindView(R.id.textview_product) TextView textViewProduct;
    @BindView(R.id.textview_qty) TextView textViewQty;
    @BindView(R.id.textview_homeaddress) TextView textViewAddress;
    @BindView(R.id.textview_phone) TextView textViewPhone;
    @BindView(R.id.textview_mobile) TextView textViewMobile;
    @BindView(R.id.close_job) Button buttonCloseJob;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
        buttonCloseJob.setOnClickListener(onClose());
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
        getDataFromIntent();
        getPresenter().getDetail(orderid);
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
    public void setDetailToView(List<JobItem> jobItems) {
        this.jobItemList = jobItems;
        for (JobItem item : jobItemList) {
            textViewUsername.setText(item.getTitle() + item.getFirstName() + " " + item.getLastName());
            productItemList = item.getProduct();
            addressItemList = item.getAddress();
            type = item.getInstallType();
        }

        for (ProductItem productItem : productItemList) {
            textViewProduct.setText(productItem.getProductName());
            textViewQty.setText(" จำนวน " + productItem.getProductQty() + " เครื่อง/ชิ้น");
        }

        for (AddressItem addressItem : addressItemList) {
            if (addressItem.getAddressType().equals("AddressInstall")) {
                sb = new StringBuilder();
                sb.append(addressItem.getAddrDetail());
                sb.append((addressItem.getSubdistrict().equals("")) ? "" : " ต." + addressItem.getSubdistrict());
                sb.append("\n");
                sb.append((addressItem.getDistrict().equals("")) ? "" : "อ." + addressItem.getDistrict());
                sb.append((addressItem.getProvince().equals("")) ? "" : " จ." + addressItem.getProvince());
                sb.append((addressItem.getZipcode().equals("")) ? "" : " " + addressItem.getZipcode());
                textViewAddress.setText(sb.toString());

                textViewPhone.setText((addressItem.getPhone().equals("")) ? "-" : addressItem.getPhone());
                textViewMobile.setText((addressItem.getMobile().equals("")) ? "-" : addressItem.getMobile());
            }
        }
    }

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textViewTitle.setText("รายละเอียด");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getDataFromIntent() {
        orderid = getIntent().getStringExtra(Constance.KEY_ORDER_ID);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            setResult();
        }
        return true;
    }

    private View.OnClickListener onClose() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCloseJob.startAnimation(new AnimateButton().animbutton());
                dialog("");
            }
        };
    }

    private void dialog(String serial) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.DialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_custom, null);
        alertDialog.setView(dialogView);
        alertDialog.create();

        final EditText editTextRef = (EditText) dialogView.findViewById(R.id.edt_referrence);
        final RelativeLayout buttonScanBarcode = (RelativeLayout) dialogView.findViewById(R.id.button_scanbarcode);

        buttonScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(NewDetailActivity.this);
                integrator.setOrientationLocked(true);
                integrator.setCaptureActivity(CaptureActivityPortrait.class);
                integrator.initiateScan();
            }
        });

        editTextRef.setText(serial);

        alertDialog.setPositiveButton("ยืนยัน",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (!editTextRef.getText().toString().isEmpty()) {
                            contactItem = new ContactItem()
                                    .setOrderid(orderid)
                                    .setEmpid(MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID))
                                    .setType(type)
                                    .setReferrence(editTextRef.getText().toString());

                            Gson gson = new Gson();
                            String patientData = gson.toJson(contactItem);
                            RequestBody requestBody = RequestBody.create(okhttp3.MultipartBody.FORM, patientData);
                            getPresenter().updateData(requestBody);
                        } else {
                            dialog.dismiss();
                        }
                    }
                });

        alertDialog.setNegativeButton("ยกเลิก",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    public void setResult() {
        /*if (!MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_ORDER_ID).isEmpty()) {
            MyApplication.getInstance().getPrefManager().setPreferrence(Constance.KEY_ORDER_ID, "");
            startActivity(new Intent(NewDetailActivity.this, MainActivity.class));
        } else {
            MyApplication.getInstance().getPrefManager().setPreferrence(Constance.KEY_ORDER_ID, "");
            setResult(RESULT_OK);
            finish();
        }*/
        MyApplication.getInstance().getPrefManager().setPreferrence(Constance.KEY_ORDER_ID, "");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
            dialog(result.getContents());

        }
    }
}
