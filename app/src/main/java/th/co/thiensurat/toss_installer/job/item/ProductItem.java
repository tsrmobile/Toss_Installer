package th.co.thiensurat.toss_installer.job.item;

import android.os.Parcel;
import android.os.Parcelable;

import th.co.thiensurat.toss_installer.base.adapter.BaseItem;

/**
 * Created by teerayut.k on 11/15/2017.
 */

public class ProductItem extends BaseItem implements Parcelable {

    private String productCode;
    private String productName;
    private String productQty;
    private String productItemCode;
    private String productItemName;
    private String productItemQty;
    private String productID;
    private String productSerial;
    private String productStatus;

    public ProductItem() {
    }

    public ProductItem(Parcel in) {
        super(in);
        productCode         = in.readString();
        productName         = in.readString();
        productQty          = in.readString();
        productItemCode     = in.readString();
        productItemName     = in.readString();
        productItemQty      = in.readString();
        productID           = in.readString();
        productSerial       = in.readString();
        productStatus       = in.readString();
    }

    public String getProductCode() {
        return productCode;
    }

    public ProductItem setProductCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public ProductItem setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getProductQty() {
        return productQty;
    }

    public ProductItem setProductQty(String productQty) {
        this.productQty = productQty;
        return this;
    }

    public String getProductItemCode() {
        return productItemCode;
    }

    public ProductItem setProductItemCode(String productItemCode) {
        this.productItemCode = productItemCode;
        return this;
    }

    public String getProductItemName() {
        return productItemName;
    }

    public ProductItem setProductItemName(String productItemName) {
        this.productItemName = productItemName;
        return this;
    }

    public String getProductItemQty() {
        return productItemQty;
    }

    public ProductItem setProductItemQty(String productItemQty) {
        this.productItemQty = productItemQty;
        return this;
    }

    public String getProductID() {
        return productID;
    }

    public ProductItem setProductID(String productID) {
        this.productID = productID;
        return this;
    }

    public String getProductSerial() {
        return productSerial;
    }

    public ProductItem setProductSerial(String productSerial) {
        this.productSerial = productSerial;
        return this;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public ProductItem setProductStatus(String productStatus) {
        this.productStatus = productStatus;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(productCode);
        dest.writeString(productName);
        dest.writeString(productQty);
        dest.writeString(productItemCode);
        dest.writeString(productItemName);
        dest.writeString(productItemQty);
        dest.writeString(productID);
        dest.writeString(productSerial);
        dest.writeString(productStatus);
    }

    public static final Creator<ProductItem> CREATOR = new Creator<ProductItem>() {
        @Override
        public ProductItem createFromParcel(Parcel in) {
            return new ProductItem(in);
        }

        @Override
        public ProductItem[] newArray(int size) {
            return new ProductItem[size];
        }
    };

    @Override
    public BaseItem clone() throws CloneNotSupportedException {
        ProductItem productItem = new ProductItem()
                .setProductCode(productCode)
                .setProductName(productName)
                .setProductQty(productQty)
                .setProductItemCode(productItemCode)
                .setProductItemName(productItemName)
                .setProductItemQty(productItemQty)
                .setProductID(productID)
                .setProductSerial(productSerial)
                .setProductStatus(productStatus);
        return productItem;
    }
}
