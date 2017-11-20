package th.co.thiensurat.toss_installer.job.item;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import th.co.thiensurat.toss_installer.base.adapter.BaseItem;


/**
 * Created by teerayut.k on 9/22/2017.
 */

public class JobItem extends BaseItem implements Parcelable {
    private String orderid;
    private String IDCard;
    private String title;
    private String firstName;
    private String lastName;
    private String contactphone;
    /*private String productCode;
    private String productName;
    private String productQty;*/
    private String installStartDate;
    private String installEndDate;
    private List<ProductItem> product;
    private List<AddressItem> address;

    public JobItem() {
    }

    public JobItem(Parcel in) {
        super(in);
        orderid             = in.readString();
        IDCard              = in.readString();
        title               = in.readString();
        firstName           = in.readString();
        lastName            = in.readString();
        contactphone        = in.readString();
        /*productCode         = in.readString();
        productName         = in.readString();
        productQty          = in.readString();*/
        installStartDate    = in.readString();
        installEndDate      = in.readString();
    }

    public String getOrderid() {
        return orderid;
    }

    public JobItem setOrderid(String orderid) {
        this.orderid = orderid;
        return this;
    }

    public String getIDCard() {
        return IDCard;
    }

    public JobItem setIDCard(String IDCard) {
        this.IDCard = IDCard;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public JobItem setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public JobItem setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public JobItem setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getContactphone() {
        return contactphone;
    }

    public JobItem setContactphone(String contactphone) {
        this.contactphone = contactphone;
        return this;
    }

    /*public String getProductCode() {
        return productCode;
    }

    public JobItem setProductCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public JobItem setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getProductQty() {
        return productQty;
    }

    public JobItem setProductQty(String productQty) {
        this.productQty = productQty;
        return this;
    }*/

    public String getInstallStartDate() {
        return installStartDate;
    }

    public JobItem setInstallStartDate(String installStartDate) {
        this.installStartDate = installStartDate;
        return this;
    }

    public String getInstallEndDate() {
        return installEndDate;
    }

    public JobItem setInstallEndDate(String installEndDate) {
        this.installEndDate = installEndDate;
        return this;
    }

    public List<ProductItem> getProduct() {
        return product;
    }

    public JobItem setProduct(List<ProductItem> product) {
        this.product = product;
        return this;
    }

    public List<AddressItem> getAddress() {
        return address;
    }

    public JobItem setAddress(List<AddressItem> address) {
        this.address = address;
        return this;
    }

    /*public AddressItem getAddress() {
        return address;
    }

    public JobItem setAddress(AddressItem address) {
        this.address = address;
        return this;
    }*/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(orderid);
        dest.writeString(IDCard);
        dest.writeString(title);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(contactphone);
        /*dest.writeString(productCode);
        dest.writeString(productName);
        dest.writeString(productQty);*/
        dest.writeString(installStartDate);
        dest.writeString(installEndDate);
    }

    public static final Creator<JobItem> CREATOR = new Creator<JobItem>() {
        @Override
        public JobItem createFromParcel(Parcel in) {
            return new JobItem(in);
        }

        @Override
        public JobItem[] newArray(int size) {
            return new JobItem[size];
        }
    };

    @Override
    public BaseItem clone() throws CloneNotSupportedException {
        JobItem jobItem = new JobItem()
                .setOrderid(orderid)
                .setIDCard(IDCard)
                .setTitle(title)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setContactphone(contactphone)
                /*.setProductCode(productCode)
                .setProductName(productName)
                .setProductQty(productQty)*/
                .setInstallStartDate(installStartDate)
                .setInstallEndDate(installEndDate);
        return jobItem;
    }
}
