package th.co.thiensurat.toss_installer.jobinstallation.item;

import android.os.Parcel;
import android.os.Parcelable;

import th.co.thiensurat.toss_installer.base.adapter.BaseItem;

/**
 * Created by teerayut.k on 11/15/2017.
 */

public class ProductItem extends BaseItem implements Parcelable {

    private String productCode;
    private String productName;
    private String productModel;
    private String productQty;
    private String productItemCode;
    private String productItemName;
    private String productItemQty;
    private String productPrice;
    private String productDiscount;
    private String productDiscountPercent;
    private String productPayType;
    private String productPackageID;
    private String productPayPeriods;
    private String productPayPerPeriods;
    private String productPaymentChannel;
    private String productID;
    private String productSerial;
    private String productStatus;
    private String productPrintContact;
    private String productPrintInstall;
    private String productPayAmount;
    private String productPayActual;

    public ProductItem() {
    }

    public ProductItem(Parcel in) {
        super(in);
        productCode             = in.readString();
        productName             = in.readString();
        productModel            = in.readString();
        productQty              = in.readString();
        productItemCode         = in.readString();
        productItemName         = in.readString();
        productItemQty          = in.readString();
        productPrice            = in.readString();
        productDiscount         = in.readString();
        productDiscountPercent  = in.readString();
        productPayType          = in.readString();
        productPackageID        = in.readString();
        productPayPeriods       = in.readString();
        productPayPerPeriods    = in.readString();
        productPaymentChannel   = in.readString();
        productID               = in.readString();
        productSerial           = in.readString();
        productStatus           = in.readString();
        productPrintContact     = in.readString();
        productPrintInstall     = in.readString();
        productPayAmount        = in.readString();
        productPayActual        = in.readString();
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

    public String getProductModel() {
        return productModel;
    }

    public ProductItem setProductModel(String productModel) {
        this.productModel = productModel;
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

    public String getProductPrice() {
        return productPrice;
    }

    public ProductItem setProductPrice(String productPrice) {
        this.productPrice = productPrice;
        return this;
    }

    public String getProductDiscount() {
        return productDiscount;
    }

    public ProductItem setProductDiscount(String productDiscount) {
        this.productDiscount = productDiscount;
        return this;
    }

    public String getProductDiscountPercent() {
        return productDiscountPercent;
    }

    public ProductItem setProductDiscountPercent(String productDiscountPercent) {
        this.productDiscountPercent = productDiscountPercent;
        return this;
    }

    public String getProductPayType() {
        return productPayType;
    }

    public ProductItem setProductPayType(String productPayType) {
        this.productPayType = productPayType;
        return this;
    }

    public String getProductPackageID() {
        return productPackageID;
    }

    public ProductItem setProductPackageID(String productPackageID) {
        this.productPackageID = productPackageID;
        return this;
    }

    public String getProductPayPeriods() {
        return productPayPeriods;
    }

    public ProductItem setProductPayPeriods(String productPayPeriods) {
        this.productPayPeriods = productPayPeriods;
        return this;
    }

    public String getProductPayPerPeriods() {
        return productPayPerPeriods;
    }

    public ProductItem setProductPayPerPeriods(String productPayPerPeriods) {
        this.productPayPerPeriods = productPayPerPeriods;
        return this;
    }

    public String getProductPaymentChannel() {
        return productPaymentChannel;
    }

    public ProductItem setProductPaymentChannel(String productPaymentChannel) {
        this.productPaymentChannel = productPaymentChannel;
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

    public String getProductPrintContact() {
        return productPrintContact;
    }

    public ProductItem setProductPrintContact(String productPrintContact) {
        this.productPrintContact = productPrintContact;
        return this;
    }

    public String getProductPrintInstall() {
        return productPrintInstall;
    }

    public ProductItem setProductPrintInstall(String productPrintInstall) {
        this.productPrintInstall = productPrintInstall;
        return this;
    }

    public String getProductPayAmount() {
        return productPayAmount;
    }

    public ProductItem setProductPayAmount(String productPayAmount) {
        this.productPayAmount = productPayAmount;
        return this;
    }

    public String getProductPayActual() {
        return productPayActual;
    }

    public ProductItem setProductPayActual(String productPayActual) {
        this.productPayActual = productPayActual;
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
        dest.writeString(productModel);
        dest.writeString(productQty);
        dest.writeString(productItemCode);
        dest.writeString(productItemName);
        dest.writeString(productItemQty);
        dest.writeString(productPrice);
        dest.writeString(productDiscount);
        dest.writeString(productDiscountPercent);
        dest.writeString(productPayType);
        dest.writeString(productPackageID);
        dest.writeString(productPayPeriods);
        dest.writeString(productPayPerPeriods);
        dest.writeString(productPaymentChannel);
        dest.writeString(productID);
        dest.writeString(productSerial);
        dest.writeString(productStatus);
        dest.writeString(productPrintContact);
        dest.writeString(productPrintInstall);
        dest.writeString(productPayAmount);
        dest.writeString(productPayActual);
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
                .setProductModel(productModel)
                .setProductQty(productQty)
                .setProductItemCode(productItemCode)
                .setProductItemName(productItemName)
                .setProductItemQty(productItemQty)
                .setProductPrice(productPrice)
                .setProductDiscount(productDiscount)
                .setProductDiscountPercent(productDiscountPercent)
                .setProductPayType(productPayType)
                .setProductPackageID(productPackageID)
                .setProductPayPeriods(productPayPeriods)
                .setProductPayPerPeriods(productPayPerPeriods)
                .setProductPaymentChannel(productPaymentChannel)
                .setProductID(productID)
                .setProductSerial(productSerial)
                .setProductStatus(productStatus)
                .setProductPrintContact(productPrintContact)
                .setProductPrintInstall(productPrintInstall)
                .setProductPayAmount(productPayAmount)
                .setProductPayActual(productPayActual);
        return productItem;
    }
}
