package th.co.thiensurat.toss_installer.jobinstallation.item;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by teerayut.k on 11/16/2017.
 */

public class ProductItemGroup implements Parcelable {

    private List<ProductItem> product;

    public ProductItemGroup(Parcel in) {
        product = in.createTypedArrayList(ProductItem.CREATOR);
    }

    public static final Creator<ProductItemGroup> CREATOR = new Creator<ProductItemGroup>() {
        @Override
        public ProductItemGroup createFromParcel(Parcel in) {
            return new ProductItemGroup(in);
        }

        @Override
        public ProductItemGroup[] newArray(int size) {
            return new ProductItemGroup[size];
        }
    };

    public ProductItemGroup() {

    }

    public List<ProductItem> getProduct() {
        return product;
    }

    public void setProduct(List<ProductItem> product) {
        this.product = product;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(product);
    }
}
