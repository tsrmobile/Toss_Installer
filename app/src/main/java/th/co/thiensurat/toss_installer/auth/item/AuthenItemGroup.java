package th.co.thiensurat.toss_installer.auth.item;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.toss_installer.base.adapter.BaseItem;


/**
 * Created by teerayut.k on 10/17/2017.
 */

public class AuthenItemGroup implements Parcelable {

    private String status;
    private String message;
    private List<AuthenItem> data;

    public AuthenItemGroup() {

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

    public List<AuthenItem> getData() {
        return data;
    }

    public void setData(List<AuthenItem> data) {
        this.data = data;
    }

    public List<BaseItem> getBaseItems(){
        List<BaseItem> baseItems = new ArrayList<>(  );
        for( AuthenItem item : data ){
            baseItems.add(item);
        }
        return baseItems;
    }

    public AuthenItemGroup(Parcel in) {
        status = in.readString();
        message = in.readString();
        data = in.createTypedArrayList(AuthenItem.CREATOR);
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

    public static final Creator<AuthenItemGroup> CREATOR = new Creator<AuthenItemGroup>() {
        @Override
        public AuthenItemGroup createFromParcel(Parcel in) {
            return new AuthenItemGroup(in);
        }

        @Override
        public AuthenItemGroup[] newArray(int size) {
            return new AuthenItemGroup[size];
        }
    };
}
