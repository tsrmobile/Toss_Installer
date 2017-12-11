package th.co.thiensurat.toss_installer.itemlist.item;

import android.os.Parcel;
import android.os.Parcelable;

import th.co.thiensurat.toss_installer.base.adapter.BaseItem;

/**
 * Created by teerayut.k on 11/10/2017.
 */

public class InstallItem extends BaseItem implements Parcelable {

    private String PrintTakeStockID;
    private String Product_SerialNum;
    private String Ref_Date;
    private String Product_Code;
    private String Product_Name;
    private String AStockStatus;

    public InstallItem() {

    }

    public InstallItem(Parcel in) {
        PrintTakeStockID        = in.readString();
        Product_SerialNum       = in.readString();
        Ref_Date                = in.readString();
        Product_Code            = in.readString();
        Product_Name            = in.readString();
        AStockStatus            = in.readString();
    }

    public String getPrintTakeStockID() {
        return PrintTakeStockID;
    }

    public InstallItem setPrintTakeStockID(String printTakeStockID) {
        PrintTakeStockID = printTakeStockID;
        return this;
    }

    public String getProduct_SerialNum() {
        return Product_SerialNum;
    }

    public InstallItem setProduct_SerialNum(String product_SerialNum) {
        Product_SerialNum = product_SerialNum;
        return this;
    }

    public String getRef_Date() {
        return Ref_Date;
    }

    public InstallItem setRef_Date(String ref_Date) {
        Ref_Date = ref_Date;
        return this;
    }

    public String getProduct_Code() {
        return Product_Code;
    }

    public InstallItem setProduct_Code(String product_Code) {
        Product_Code = product_Code;
        return this;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public InstallItem setProduct_Name(String product_Name) {
        Product_Name = product_Name;
        return this;
    }

    public String getAStockStatus() {
        return AStockStatus;
    }

    public InstallItem setAStockStatus(String AStockStatus) {
        this.AStockStatus = AStockStatus;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(PrintTakeStockID);
        dest.writeString(Product_SerialNum);
        dest.writeString(Ref_Date);
        dest.writeString(Product_Code);
        dest.writeString(Product_Name);
        dest.writeString(AStockStatus);
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
                .setPrintTakeStockID(PrintTakeStockID)
                .setProduct_SerialNum(Product_SerialNum)
                .setRef_Date(Ref_Date)
                .setProduct_Code(Product_Code)
                .setProduct_Name(Product_Name)
                .setAStockStatus(AStockStatus);
        return InstallItem;
    }
}
