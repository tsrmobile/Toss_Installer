package th.co.thiensurat.toss_installer.jobinstallation.item;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import th.co.thiensurat.toss_installer.base.adapter.BaseItem;


/**
 * Created by teerayut.k on 9/25/2017.
 */

public class AddressItem extends BaseItem implements Parcelable, Serializable {

    private String addressType;
    private String addrDetail;
    private String province;
    private String district;
    private String subdistrict;
    private String zipcode;
    private String phone;
    private String mobile;
    private String office;
    private String email;

    public AddressItem() {

    }

    protected AddressItem(Parcel in) {
        super(in);
        addressType = in.readString();
        addrDetail = in.readString();
        province = in.readString();
        district = in.readString();
        subdistrict = in.readString();
        zipcode = in.readString();
        phone = in.readString();
        mobile = in.readString();
        office = in.readString();
        email = in.readString();
    }

    public String getAddressType() {
        return addressType;
    }

    public AddressItem setAddressType(String addressType) {
        this.addressType = addressType;
        return this;
    }

    public String getAddrDetail() {
        return addrDetail;
    }

    public AddressItem setAddrDetail(String addrDetail) {
        this.addrDetail = addrDetail;
        return this;
    }

    public String getProvince() {
        return province;
    }

    public AddressItem setProvince(String province) {
        this.province = province;
        return this;
    }

    public String getDistrict() {
        return district;
    }

    public AddressItem setDistrict(String district) {
        this.district = district;
        return this;
    }

    public String getSubdistrict() {
        return subdistrict;
    }

    public AddressItem setSubdistrict(String subdistrict) {
        this.subdistrict = subdistrict;
        return this;
    }

    public String getZipcode() {
        return zipcode;
    }

    public AddressItem setZipcode(String zipcode) {
        this.zipcode = zipcode;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public AddressItem setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public AddressItem setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public String getOffice() {
        return office;
    }

    public AddressItem setOffice(String office) {
        this.office = office;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AddressItem setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public BaseItem clone() throws CloneNotSupportedException {
        AddressItem addressItem = new AddressItem()
                .setAddressType(addressType)
                .setAddrDetail(addrDetail)
                .setProvince(province)
                .setDistrict(district)
                .setSubdistrict(subdistrict)
                .setZipcode(zipcode)
                .setPhone(phone)
                .setMobile(mobile)
                .setOffice(office)
                .setEmail(email);
        return addressItem;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(addressType);
        dest.writeString(addrDetail);
        dest.writeString(province);
        dest.writeString(district);
        dest.writeString(subdistrict);
        dest.writeString(zipcode);
        dest.writeString(phone);
        dest.writeString(mobile);
        dest.writeString(office);
        dest.writeString(email);
    }

    public static final Creator<AddressItem> CREATOR = new Creator<AddressItem>() {
        @Override
        public AddressItem createFromParcel(Parcel in) {
            return new AddressItem(in);
        }

        @Override
        public AddressItem[] newArray(int size) {
            return new AddressItem[size];
        }
    };
}
