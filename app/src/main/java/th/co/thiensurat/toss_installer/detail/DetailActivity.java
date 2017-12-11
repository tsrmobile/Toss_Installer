package th.co.thiensurat.toss_installer.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
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
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

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
import th.co.thiensurat.toss_installer.utils.MyApplication;

public class DetailActivity extends BaseMvpActivity<DetailInterface.Presenter> implements DetailInterface.View {

    private int position;
    private JobItem jobItem;
    private JobItemGroup itemGroup;
    private StringBuilder sb;
    private TextView textViewTitle;
    private AddressItem addressItem;

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
    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupInstance() {

    }

    @Override
    public void setupView() {
        setToolbar();
        buttonNext.setOnClickListener( onNext() );
    }

    @Override
    public void initialize() {
        getItemFromIntent();
        textViewTitle.setText(jobItem.getTitle() + "" + jobItem.getFirstName() + " " + jobItem.getLastName());
        getPresenter().getAddressDetail(DetailActivity.this, jobItem.getOrderid());
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);*/
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        for(int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0);
            item.setTitle(spanString);
        }
        return true;
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
