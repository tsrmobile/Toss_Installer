package th.co.thiensurat.toss_installer.job.item;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.toss_installer.base.adapter.BaseItem;


/**
 * Created by teerayut.k on 9/25/2017.
 */

public class JobItemGroup implements Parcelable {

    private String status;
    private String message;
    private List<JobItem> data;

    public JobItemGroup() {

    }

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

    public List<JobItem> getData() {
        return data;
    }

    public void setData(List<JobItem> data) {
        this.data = data;
    }

    public JobItemGroup(Parcel in) {
        status  = in.readString();
        message = in.readString();
        data    = in.createTypedArrayList(JobItem.CREATOR);
    }

    public static final Creator<JobItemGroup> CREATOR = new Creator<JobItemGroup>() {
        @Override
        public JobItemGroup createFromParcel(Parcel in) {
            return new JobItemGroup(in);
        }

        @Override
        public JobItemGroup[] newArray(int size) {
            return new JobItemGroup[size];
        }
    };

    public List<BaseItem> getBaseItem() {
        List<BaseItem> baseItems = new ArrayList<>();
        for (JobItem item: data) {
            baseItems.add(item);
        }
        return baseItems;
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
