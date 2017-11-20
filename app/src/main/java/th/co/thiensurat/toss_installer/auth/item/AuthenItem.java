package th.co.thiensurat.toss_installer.auth.item;

import android.os.Parcel;
import android.os.Parcelable;

import th.co.thiensurat.toss_installer.base.adapter.BaseItem;


/**
 * Created by teerayut.k on 10/17/2017.
 */

public class AuthenItem extends BaseItem implements Parcelable {

    private String employeecode;
    private String title;
    private String firstname;
    private String lastname;
    private String positionName;
    private String departmentName;

    public AuthenItem() {

    }

    public String getEmployeecode() {
        return employeecode;
    }

    public AuthenItem setEmployeecode(String employeecode) {
        this.employeecode = employeecode;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public AuthenItem setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public AuthenItem setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public AuthenItem setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getPositionName() {
        return positionName;
    }

    public AuthenItem setPositionName(String positionName) {
        this.positionName = positionName;
        return this;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public AuthenItem setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(employeecode);
        dest.writeString(title);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(positionName);
        dest.writeString(departmentName);
    }

    protected AuthenItem(Parcel in) {
        super(in);
        employeecode     = in.readString();
        title            = in.readString();
        firstname        = in.readString();
        lastname         = in.readString();
        positionName     = in.readString();
        departmentName   = in.readString();
    }

    @Override
    public BaseItem clone() throws CloneNotSupportedException {
        AuthenItem item = new AuthenItem()
                .setEmployeecode(employeecode)
                .setTitle(title)
                .setFirstname(firstname)
                .setLastname(lastname)
                .setPositionName(positionName)
                .setDepartmentName(departmentName);
        return item;
    }

    public static final Creator<AuthenItem> CREATOR = new Creator<AuthenItem>() {
        @Override
        public AuthenItem createFromParcel(Parcel in) {
            return new AuthenItem(in);
        }

        @Override
        public AuthenItem[] newArray(int size) {
            return new AuthenItem[size];
        }
    };
}
