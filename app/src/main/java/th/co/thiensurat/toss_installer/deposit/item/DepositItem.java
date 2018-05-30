package th.co.thiensurat.toss_installer.deposit.item;

import android.os.Parcel;
import android.os.Parcelable;

import th.co.thiensurat.toss_installer.base.adapter.BaseItem;

public class DepositItem extends BaseItem implements Parcelable {

    private String id;
    private String contno;
    private String channel;
    private String date;
    private String amount;
    private String ref1;
    private String ref2;
    private boolean isSelected = false;

    public DepositItem() {

    }

    public DepositItem(Parcel in) {
        super(in);
        id             = in.readString();
        contno         = in.readString();
        channel        = in.readString();
        date           = in.readString();
        amount         = in.readString();
        ref1           = in.readString();
        ref2           = in.readString();
        isSelected     = in.readByte() != 0;
    }

    public String getId() {
        return id;
    }

    public DepositItem setId(String id) {
        this.id = id;
        return this;
    }

    public String getContno() {
        return contno;
    }

    public DepositItem setContno(String contno) {
        this.contno = contno;
        return this;
    }

    public String getChannel() {
        return channel;
    }

    public DepositItem setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public String getDate() {
        return date;
    }

    public DepositItem setDate(String date) {
        this.date = date;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public DepositItem setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getRef1() {
        return ref1;
    }

    public DepositItem setRef1(String ref1) {
        this.ref1 = ref1;
        return this;
    }

    public String getRef2() {
        return ref2;
    }

    public DepositItem setRef2(String ref2) {
        this.ref2 = ref2;
        return this;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public DepositItem setSelected(boolean selected) {
        isSelected = selected;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(id);
        dest.writeString(contno);
        dest.writeString(channel);
        dest.writeString(date);
        dest.writeString(amount);
        dest.writeString(ref1);
        dest.writeString(ref2);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    public static final Creator<DepositItem> CREATOR = new Creator<DepositItem>() {
        @Override
        public DepositItem createFromParcel(Parcel in) {
            return new DepositItem(in);
        }

        @Override
        public DepositItem[] newArray(int size) {
            return new DepositItem[size];
        }
    };

    @Override
    public BaseItem clone() throws CloneNotSupportedException {
        DepositItem depositItem = new DepositItem()
                .setId(id)
                .setContno(contno)
                .setChannel(channel)
                .setDate(date)
                .setAmount(amount)
                .setRef1(ref1)
                .setRef2(ref2)
                .setSelected(isSelected);
        return depositItem;
    }
}
