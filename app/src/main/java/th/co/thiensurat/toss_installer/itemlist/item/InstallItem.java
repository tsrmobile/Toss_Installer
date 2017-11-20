package th.co.thiensurat.toss_installer.itemlist.item;

import android.os.Parcel;
import android.os.Parcelable;

import th.co.thiensurat.toss_installer.base.adapter.BaseItem;

/**
 * Created by teerayut.k on 11/10/2017.
 */

public class InstallItem extends BaseItem implements Parcelable {

    private String orderid;
    private String productCode;
    private String productRef;
    private String productName;
    private String productQty;

    public InstallItem() {

    }

    public InstallItem(Parcel in) {
        orderid     = in.readString();
        productCode = in.readString();
        productRef  = in.readString();
        productName = in.readString();
        productQty  = in.readString();
    }

    public String getOrderid() {
        return orderid;
    }

    public InstallItem setOrderid(String orderid) {
        this.orderid = orderid;
        return this;
    }

    public String getProductCode() {
        return productCode;
    }

    public InstallItem setProductCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public String getProductRef() {
        return productRef;
    }

    public InstallItem setProductRef(String productRef) {
        this.productRef = productRef;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public InstallItem setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getProductQty() {
        return productQty;
    }

    public InstallItem setProductQty(String productQty) {
        this.productQty = productQty;
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
        dest.writeString(productCode);
        dest.writeString(productRef);
        dest.writeString(productName);
        dest.writeString(productQty);
    }

    public static final Creator<InstallItem> CREATOR = new Creator<InstallItem>() {
        @Override
        public InstallItem createFromParcel(Parcel in) {
            return new InstallItem(in);
        }

        @Override
        public InstallItem[] newArray(int size) {
            return new InstallItem[size];
        }
    };

    @Override
    public BaseItem clone() throws CloneNotSupportedException {
        InstallItem InstallItem = new InstallItem()
                .setOrderid(orderid)
                .setProductCode(productCode)
                .setProductRef(productRef)
                .setProductName(productName)
                .setProductQty(productQty);
        return InstallItem;
    }
}
