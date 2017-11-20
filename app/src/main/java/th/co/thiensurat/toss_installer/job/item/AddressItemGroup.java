package th.co.thiensurat.toss_installer.job.item;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by teerayut.k on 9/27/2017.
 */

public class AddressItemGroup implements Parcelable {

    private String status;
    private String message;
    private List<AddressItem> data;

    public AddressItemGroup() {

    }

    protected AddressItemGroup(Parcel in) {
        status = in.readString();
        message = in.readString();
        data = in.createTypedArrayList(AddressItem.CREATOR);
    }

    public static final Creator<AddressItemGroup> CREATOR = new Creator<AddressItemGroup>() {
        @Override
        public AddressItemGroup createFromParcel(Parcel in) {
            return new AddressItemGroup(in);
        }

        @Override
        public AddressItemGroup[] newArray(int size) {
            return new AddressItemGroup[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AddressItem> getData() {
        return data;
    }

    public void setData(List<AddressItem> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(status);
        parcel.writeString(message);
        parcel.writeTypedList(data);
    }
}
