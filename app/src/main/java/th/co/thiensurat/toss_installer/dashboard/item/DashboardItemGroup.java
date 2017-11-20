package th.co.thiensurat.toss_installer.dashboard.item;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by teerayut.k on 11/9/2017.
 */

public class DashboardItemGroup implements Parcelable {

    private String status;
    private String message;
    private List<DashboardItem> data;

    public DashboardItemGroup() {

    }

    protected DashboardItemGroup(Parcel in) {
        status = in.readString();
        message = in.readString();
        data = in.createTypedArrayList(DashboardItem.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(message);
        dest.writeTypedList(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DashboardItemGroup> CREATOR = new Creator<DashboardItemGroup>() {
        @Override
        public DashboardItemGroup createFromParcel(Parcel in) {
            return new DashboardItemGroup(in);
        }

        @Override
        public DashboardItemGroup[] newArray(int size) {
            return new DashboardItemGroup[size];
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

    public List<DashboardItem> getData() {
        return data;
    }

    public void setData(List<DashboardItem> data) {
        this.data = data;
    }
}
