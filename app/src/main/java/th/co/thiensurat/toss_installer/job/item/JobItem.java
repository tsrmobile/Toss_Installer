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
    private String installStartDate;
    private String installEndDate;
    private String installStart;
    private String installEnd;
    private String status;
    private String presale;
    private String contno;
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
        installStartDate    = in.readString();
        installEndDate      = in.readString();
        status              = in.readString();
        presale             = in.readString();
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

    public String getInstallStart() {
        return installStart;
    }

    public JobItem setInstallStart(String installStart) {
        this.installStart = installStart;
        return this;
    }

    public String getInstallEnd() {
        return installEnd;
    }

    public JobItem setInstallEnd(String installEnd) {
        this.installEnd = installEnd;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public JobItem setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getPresale() {
        return presale;
    }

    public JobItem setPresale(String presale) {
        this.presale = presale;
        return this;
    }

    public String getContno() {
        return contno;
    }

    public JobItem setContno(String contno) {
        this.contno = contno;
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
        dest.writeString(installStartDate);
        dest.writeString(installEndDate);
        dest.writeString(installStart);
        dest.writeString(installEnd);
        dest.writeString(status);
        dest.writeString(presale);
        dest.writeString(contno);
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
                .setInstallStartDate(installStartDate)
                .setInstallEndDate(installEndDate)
                .setInstallStart(installStart)
                .setInstallEnd(installEnd)
                .setStatus(status)
                .setPresale(presale)
                .setContno(contno);
        return jobItem;
    }
}
