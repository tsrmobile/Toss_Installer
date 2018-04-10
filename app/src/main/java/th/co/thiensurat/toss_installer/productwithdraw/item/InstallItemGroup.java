package th.co.thiensurat.toss_installer.productwithdraw.item;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by teerayut.k on 11/10/2017.
 */

public class InstallItemGroup implements Parcelable {

    private String status;
    private String message;
    private List<InstallItem> data;

    public InstallItemGroup() {

    }

    protected InstallItemGroup(Parcel in) {
        status = in.readString();
        message = in.readString();
        data = in.createTypedArrayList(InstallItem.CREATOR);
    }

    public static final Creator<InstallItemGroup> CREATOR = new Creator<InstallItemGroup>() {
        @Override
        public InstallItemGroup createFromParcel(Parcel in) {
            return new InstallItemGroup(in);
        }

        @Override
        public InstallItemGroup[] newArray(int size) {
            return new InstallItemGroup[size];
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

    public List<InstallItem> getData() {
        return data;
    }

    public void setData(List<InstallItem> data) {
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
