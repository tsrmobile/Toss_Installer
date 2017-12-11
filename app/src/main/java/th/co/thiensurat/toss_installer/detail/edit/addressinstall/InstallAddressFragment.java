package th.co.thiensurat.toss_installer.detail.edit.addressinstall;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.base.BaseMvpFragment;
import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.detail.edit.EditActivity;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.JobItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstallAddressFragment extends BaseMvpFragment<InstallAddressInterface.Presenter> implements InstallAddressInterface.View {

    private JobItem jobItem;
    private List<AddressItem> addressItems = new ArrayList<AddressItem>();

    public InstallAddressFragment() {
        // Required empty public constructor
    }

    public static InstallAddressFragment getInstance() {
        return new InstallAddressFragment();
    }

    @Override
    public InstallAddressInterface.Presenter createPresenter() {
        return InstallAddressPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_install_address;
    }

    @BindView(R.id.addr_detial) EditText editTextDetial;
    @BindView(R.id.spinner_province) Spinner spinnerProvince;
    @BindView(R.id.spinner_district) Spinner spinnerDistrict;
    @BindView(R.id.spinner_sub_district) Spinner spinnerSubDistrict;
    @BindView(R.id.addr_zipcode) EditText editTextZipcode;
    @BindView(R.id.addr_phone) EditText editTextPhone;
    @BindView(R.id.addr_phonework) EditText editTextWork;
    @BindView(R.id.addr_mobile) EditText editTextMobile;
    @BindView(R.id.addr_email) EditText editTextEmail;
    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void setupInstance() {

    }

    @Override
    public void setupView() {

    }

    @Override
    public void initialize() {
        jobItem = ((EditActivity)getActivity()).getJobItem();
        getPresenter().getAddressDetail(getActivity(), jobItem.getOrderid());
    }

    @Override
    public void setAddressDetail(List<AddressItem> addressDetail) {
        for (int i = 0; i < addressDetail.size(); i++) {
            AddressItem item = addressDetail.get(i);
            if ("AddressInstall".equals(item.getAddressType())) {
                editTextDetial.setText(item.getAddrDetail());

                editTextZipcode.setText(item.getZipcode());
                editTextPhone.setText((item.getPhone().equals("")) ? "-" : item.getPhone());
                editTextWork.setText((item.getOffice().equals("")) ? "-" : item.getOffice());
                editTextMobile.setText((item.getMobile().equals("")) ? "-" : item.getMobile());
                editTextEmail.setText((item.getEmail().equals("")) ? "-" : item.getEmail());
            }
        }
    }
}
