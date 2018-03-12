package th.co.thiensurat.toss_installer.detail.edit.addresscard;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.api.request.RequestUpdateAddress;
import th.co.thiensurat.toss_installer.api.result.data.DataItem;
import th.co.thiensurat.toss_installer.base.BaseMvpFragment;
import th.co.thiensurat.toss_installer.detail.edit.EditActivity;
import th.co.thiensurat.toss_installer.detail.edit.adapter.SpinnerCustomAdapter;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItemGroup;
import th.co.thiensurat.toss_installer.jobinstallation.item.JobItem;
import th.co.thiensurat.toss_installer.network.ConnectionDetector;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;
import th.co.thiensurat.toss_installer.utils.MyApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardAddressFragment extends BaseMvpFragment<CardAddressInterface.Presenter> implements CardAddressInterface.View {

    private JobItem jobItem;
    private CustomDialog customDialog;
    private String province, district, subdistrict;
    private String provinceid, districtid, subdistrictid;
    private SpinnerCustomAdapter spinnerCustomAdapter;
    private List<AddressItem> addressItems = new ArrayList<AddressItem>();

    private AddressItemGroup addressItemGroup;

    public CardAddressFragment() {
        // Required empty public constructor
    }

    public static CardAddressFragment getInstance() {
        return new CardAddressFragment();
    }

    @Override
    public CardAddressInterface.Presenter createPresenter() {
        return CardAddressPresenter.create(getActivity());
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
    @BindView(R.id.button_update) Button buttonUpdate;
    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void setupInstance() {
        customDialog = new CustomDialog(getActivity());
    }

    @Override
    public void setupView() {
        buttonUpdate.setOnClickListener( onUpdate() );
    }

    @Override
    public void initialize() {
        getPresenter().getAddressDetail(MyApplication.getInstance().getPrefManager().getPreferrence("ORDERID"));
    }

    @Override
    public void OnLoad() {
        customDialog.dialogLoading();
    }

    @Override
    public void OnDismiss() {
        customDialog.dialogDimiss();
    }

    @Override
    public void OnFail(String fail) {
        customDialog.dialogFail(fail);
    }

    @Override
    public void OnSuccess(String success) {
        Toast.makeText(getActivity(), success, Toast.LENGTH_LONG).show();
        getPresenter().updateAddressSync(jobItem.getOrderid());
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

        getPresenter().getInfo("province", "");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("AddressIDCard", getPresenter().getAddressItemGroup());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        getPresenter().setAddressItemGroup((AddressItemGroup) savedInstanceState.getParcelable("AddressIDCard"));
    }

    @Override
    public void restoreView(Bundle savedInstanceState) {
        super.restoreView(savedInstanceState);
        getPresenter().setAddressItemToAdapter(getPresenter().getAddressItemGroup());
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
                getPresenter().getInfo("district", dataItems.get(i).getDataId());
            }
        }

        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    //((TextView) view.findViewById(R.id.row_item)).setTextColor(getResources().getColor(R.color.Black));
                    DataItem item = dataItems.get(position);
                    province = item.getDataName();
                    provinceid = item.getDataId();
                    getPresenter().getInfo("district", item.getDataId());
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
                getPresenter().getInfo("subdistrict", dataItems.get(i).getDataId());
            }
        }

        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    //((TextView) view.findViewById(R.id.row_item)).setTextColor(getResources().getColor(R.color.Black));
                    DataItem item = dataItems.get(position);
                    district = item.getDataName();
                    districtid = item.getDataId();
                    getPresenter().getInfo("subdistrict", item.getDataId());
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
                    subdistrict = item.getDataName();
                    subdistrictid = item.getDataId();
                    getPresenter().getInfo("zipcode", item.getDataId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void setZipcode(String zipcode) {
        editTextZipcode.setText(zipcode);
    }

    private View.OnClickListener onUpdate() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonUpdate.startAnimation(new AnimateButton().animbutton());
                updateData();
            }
        };
    }

    private void updateData() {
        List<AddressItem> itemListLocal = new ArrayList<AddressItem>();
        AddressItem addressItemLocal = new AddressItem()
                .setAddrDetail(editTextDetial.getText().toString())
                .setProvince(province)
                .setDistrict(district)
                .setSubdistrict(subdistrict)
                .setZipcode(editTextZipcode.getText().toString())
                .setPhone(editTextPhone.getText().toString())
                .setOffice(editTextWork.getText().toString())
                .setMobile(editTextMobile.getText().toString())
                .setEmail(editTextEmail.getText().toString());
        itemListLocal.add(addressItemLocal);
        getPresenter().updateData(jobItem.getOrderid(), "AddressIDCard", itemListLocal);
    }

    @Override
    public void updateLocalSuccess(boolean b) {
        if (b) {
            boolean isNetworkAvailable = ConnectionDetector.isConnectingToInternet(getActivity());
            if (!isNetworkAvailable) {

            } else {
                List<RequestUpdateAddress.updateBody> updateBodyList = new ArrayList<>();
                updateBodyList.add(new RequestUpdateAddress.updateBody()
                        .setOrderid(jobItem.getOrderid())
                        .setAddressType("AddressIDCard")
                        .setAddrDetail(editTextDetial.getText().toString())
                        .setProvince(provinceid)
                        .setDistrict(districtid)
                        .setSubdistrict(subdistrictid)
                        .setZipcode(editTextZipcode.getText().toString())
                        .setPhone(editTextPhone.getText().toString())
                        .setOffice(editTextWork.getText().toString())
                        .setMobile(editTextMobile.getText().toString())
                        .setEmail(editTextEmail.getText().toString()));

                getPresenter().updateDataOnline(updateBodyList);
            }
        } else {
            customDialog.dialogFail("พบข้อผิดพลาดระหว่างการอัพเดท!");
        }
    }
}
