package th.co.thiensurat.toss_installer.detail.edit.addresscard;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.api.result.data.DataItem;
import th.co.thiensurat.toss_installer.base.BaseMvpFragment;
import th.co.thiensurat.toss_installer.detail.edit.EditActivity;
import th.co.thiensurat.toss_installer.detail.edit.adapter.SpinnerCustomAdapter;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.JobItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardAddressFragment extends BaseMvpFragment<CardAddressInterface.Presenter> implements CardAddressInterface.View {

    private JobItem jobItem;
    private String province, district, subdistrict;
    private SpinnerCustomAdapter spinnerCustomAdapter;
    private List<AddressItem> addressItems = new ArrayList<AddressItem>();

    public CardAddressFragment() {
        // Required empty public constructor
    }

    public static CardAddressFragment getInstance() {
        return new CardAddressFragment();
    }

    @Override
    public CardAddressInterface.Presenter createPresenter() {
        return CardAddressPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_card_address;
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
            if ("AddressIDCard".equals(item.getAddressType())) {
                editTextDetial.setText(item.getAddrDetail());

                province = item.getProvince();
                district = item.getDistrict();
                subdistrict = item.getSubdistrict();

                editTextZipcode.setText(item.getZipcode());
                editTextPhone.setText((item.getPhone().equals("")) ? "-" : item.getPhone());
                editTextWork.setText((item.getOffice().equals("")) ? "-" : item.getOffice());
                editTextMobile.setText((item.getMobile().equals("")) ? "-" : item.getMobile());
                editTextEmail.setText((item.getEmail().equals("")) ? "-" : item.getEmail());
            }
        }
        getPresenter().getInfo(getActivity(), "province", "");
    }

    @Override
    public void setInfoToAdapter(String infoType, List<DataItem> dataItemList) {
        switch (infoType) {
            case "province":
                setSpinnerProvince(dataItemList);
                break;
            case "district" :
                setSpinnerDistrict(dataItemList);
                break;
            case "subdistrict" :
                setSpinnerSubDistrict(dataItemList);
                break;
        }
    }

    private void setSpinnerProvince(final List<DataItem> dataItems) {
        spinnerCustomAdapter = new SpinnerCustomAdapter(
                getActivity(), R.layout.spinner_item, dataItems, getActivity().getResources(), "province");
        spinnerProvince.setAdapter(spinnerCustomAdapter);
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = dataItems.get(i);
            if (province.equals(item.getDataName())) {
                spinnerProvince.setSelection(i);
                getPresenter().getInfo(getActivity(), "district", dataItems.get(i).getDataId());
            }
        }

        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    //((TextView) view.findViewById(R.id.row_item)).setTextColor(getResources().getColor(R.color.Black));
                    DataItem item = dataItems.get(position);
                    getPresenter().getInfo(getActivity(), "district", item.getDataId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSpinnerDistrict(final List<DataItem> dataItems) {
        spinnerCustomAdapter = new SpinnerCustomAdapter(
                getActivity(), R.layout.spinner_item, dataItems, getActivity().getResources(), "district");
        spinnerDistrict.setAdapter(spinnerCustomAdapter);
        for (int i = 0; i < dataItems.size(); i++) {
            if (district.equals(dataItems.get(i).getDataName())) {
                spinnerDistrict.setSelection(i);
                getPresenter().getInfo(getActivity(), "subdistrict", dataItems.get(i).getDataId());
            }
        }

        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    //((TextView) view.findViewById(R.id.row_item)).setTextColor(getResources().getColor(R.color.Black));
                    DataItem item = dataItems.get(position);
                    getPresenter().getInfo(getActivity(), "subdistrict", item.getDataId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSpinnerSubDistrict(final List<DataItem> dataItems) {
        spinnerCustomAdapter = new SpinnerCustomAdapter(
                getActivity(), R.layout.spinner_item, dataItems, getActivity().getResources(), "subdistrict");
        spinnerSubDistrict.setAdapter(spinnerCustomAdapter);
        for (int i = 0; i < dataItems.size(); i++) {
            if (subdistrict.equals(dataItems.get(i).getDataName())) {
                spinnerSubDistrict.setSelection(i);
            }
        }

        spinnerSubDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    //((TextView) view.findViewById(R.id.row_item)).setTextColor(getResources().getColor(R.color.Black));
                    DataItem item = dataItems.get(position);
                    editTextZipcode.setText(item.getDataCode());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
