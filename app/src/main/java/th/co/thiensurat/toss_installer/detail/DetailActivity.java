package th.co.thiensurat.toss_installer.detail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpActivity;
import th.co.thiensurat.toss_installer.detail.edit.EditActivity;
import th.co.thiensurat.toss_installer.installation.InstallationActivity;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.JobItem;
import th.co.thiensurat.toss_installer.job.item.JobItemGroup;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.MyApplication;

public class DetailActivity extends BaseMvpActivity<DetailInterface.Presenter> implements DetailInterface.View {

    private int position;
    private JobItem jobItem;
    private JobItemGroup itemGroup;
    private StringBuilder sb;
    private TextView textViewTitle;
    private AddressItem addressItem;
    private CustomDialog customDialog;

    @Override
    public DetailInterface.Presenter createPresenter() {
        return DetailPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_detail;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.textview_homeaddress) TextView textViewAddress;
    @BindView(R.id.textview_phone) TextView textViewPhone;
    @BindView(R.id.textview_phonework) TextView textViewWork;
    @BindView(R.id.textview_mobile) TextView textViewMobile;
    @BindView(R.id.textview_email) TextView textViewEmail;
    @BindView(R.id.button_next) Button buttonNext;
    @BindView(R.id.button_cancel) Button buttonCancel;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupInstance() {
        customDialog = new CustomDialog(DetailActivity.this);
    }

    @Override
    public void setupView() {
        setToolbar();
        buttonNext.setOnClickListener( onNext() );
        buttonCancel.setOnClickListener( onCancel() );
    }

    @Override
    public void initialize() {
        getItemFromIntent();
        textViewTitle.setText(jobItem.getTitle() + "" + jobItem.getFirstName() + " " + jobItem.getLastName());
        getPresenter().getAddressDetail(DetailActivity.this, jobItem.getOrderid());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setToolbar() {
        toolbar.setTitle("");
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (item.getItemId() == R.id.menu_edit) {
            Intent intent = new Intent(DetailActivity.this, EditActivity.class);
            intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
            startActivityForResult(intent, Constance.REQUEST_EDIT_DETAIL);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setCancelSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onFail(String fail) {
        customDialog.dialogFail(fail);
    }

    @Override
    public void onLoad() {
        customDialog.dialogLoading();
    }

    @Override
    public void onDismiss() {
        customDialog.dialogDimiss();
    }

    private void getItemFromIntent() {
        JobItem jobItem = getIntent().getParcelableExtra(Constance.KEY_JOB_ITEM);
        setJobItem(jobItem);
    }

    private void setJobItem(JobItem item) {
        this.jobItem = item;
    }

    private View.OnClickListener onNext() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonNext.startAnimation(new AnimateButton().animbutton());
                Intent intent = new Intent(getApplicationContext(), InstallationActivity.class);
                intent.putExtra(Constance.KEY_JOB_ITEM, jobItem);
                startActivityForResult(intent, Constance.REQUEST_INSTALLATION);
            }
        };
    }

    private View.OnClickListener onCancel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonCancel.startAnimation(new AnimateButton().animbutton());
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailActivity.this);
                alertDialog.setTitle("ยกเลิก");
                alertDialog.setMessage("กรุณาระบุเหตุผลที่ยกเลิก");

                final EditText input = new EditText(DetailActivity.this);
                input.setBackground(getResources().getDrawable(R.drawable.border_rounded_colorprimarydark));
                input.setPadding(12, 4, 0, 4);
                input.setMaxLines(3);
                input.setHint("ระบุเหตุผล");
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(4, 0, 4, 0);

                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.ic_custom_cancel);

                alertDialog.setPositiveButton("ยืนยัน",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (!input.getText().toString().isEmpty()) {
                                    getPresenter().setCancelJob(DetailActivity.this, jobItem.getOrderid(), input.getText().toString());
                                } else {
                                    dialog.dismiss();
                                    customDialog.dialogFail("การยกเลิกไม่สำเร็จ กรุณาระบุเหตุผลการยกเเลิก");
                                }
                            }
                        });

                alertDialog.setNegativeButton("ปิด",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }
        };
    }

    @Override
    public void setAddressDetail(List<AddressItem> addressDetail) {
        for (int i = 0; i < addressDetail.size(); i++) {
            AddressItem item = addressDetail.get(i);
            if (item.getAddressType().equals("AddressInstall")) {
                sb = new StringBuilder();
                sb.append(item.getAddrDetail());
                sb.append((item.getSubdistrict().equals("")) ? "" : " ต." + item.getSubdistrict());
                sb.append("\n");
                sb.append((item.getDistrict().equals("")) ? "" : "อ." + item.getDistrict());
                sb.append((item.getProvince().equals("")) ? "" : " จ." + item.getProvince());
                sb.append((item.getZipcode().equals("")) ? "" : " " + item.getZipcode());
                textViewAddress.setText(sb.toString());

                textViewPhone.setText((item.getPhone().equals("")) ? "-" : item.getPhone());
                textViewWork.setText((item.getOffice().equals("")) ? "-" : item.getOffice());
                textViewMobile.setText((item.getMobile().equals("")) ? "-" : item.getMobile());
                textViewEmail.setText((item.getEmail().equals("")) ? "-" : item.getEmail());
            }
        }
    }
}
