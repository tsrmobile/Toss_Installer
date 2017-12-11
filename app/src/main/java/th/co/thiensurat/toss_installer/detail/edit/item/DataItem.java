package th.co.thiensurat.toss_installer.detail.edit.item;

import android.os.Parcel;
import android.os.Parcelable;

import th.co.thiensurat.toss_installer.base.adapter.BaseItem;

/**
 * Created by teerayut.k on 12/11/2017.
 */

public class DataItem extends BaseItem implements Parcelable {

    private String dataid;
    private String datacode;
    private String dataname;

    public DataItem() {

    }

    public DataItem(Parcel in) {
        dataid      = in.readString();
        datacode    = in.readString();
        dataname    = in.readString();
    }

    public String getDataid() {
        return dataid;
    }

    public DataItem setDataid(String dataid) {
        this.dataid = dataid;
        return this;
    }

    public String getDatacode() {
        return datacode;
    }

    public DataItem setDatacode(String datacode) {
        this.datacode = datacode;
        return this;
    }

    public String getDataname() {
        return dataname;
    }

    public DataItem setDataname(String dataname) {
        this.dataname = dataname;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(dataid);
        dest.writeString(datacode);
        dest.writeString(dataname);
    }

    public static final Creator<DataItem> CREATOR = new Creator<DataItem>() {
        @Override
        public DataItem createFromParcel(Parcel in) {
            return new DataItem(in);
        }

        @Override
        public DataItem[] newArray(int size) {
            return new DataItem[size];
        }
    };

    @Override
    public BaseItem clone() throws CloneNotSupportedException {
        DataItem dataItem = new DataItem()
                .setDataid(dataid)
                .setDatacode(datacode)
                .setDataname(dataname);
        return dataItem;
    }
}
