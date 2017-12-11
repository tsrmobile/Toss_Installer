package th.co.thiensurat.toss_installer.takepicture.item;

import android.os.Parcel;
import android.os.Parcelable;

import th.co.thiensurat.toss_installer.base.adapter.BaseItem;

/**
 * Created by teerayut.k on 11/16/2017.
 */

public class ImageItem extends BaseItem implements Parcelable {

    private String imageId;
    private String imageOrderId;
    private String imageSerial;
    private String imageType;
    private String imageUrl;

    public ImageItem() {

    }

    public ImageItem(Parcel in) {
        super(in);
        imageId         = in.readString();
        imageOrderId    = in.readString();
        imageSerial     = in.readString();
        imageType       = in.readString();
        imageUrl        = in.readString();
    }

    public String getImageId() {
        return imageId;
    }

    public ImageItem setImageId(String imageId) {
        this.imageId = imageId;
        return this;
    }

    public String getImageOrderId() {
        return imageOrderId;
    }

    public ImageItem setImageOrderId(String imageOrderId) {
        this.imageOrderId = imageOrderId;
        return this;
    }

    public String getImageSerial() {
        return imageSerial;
    }

    public ImageItem setImageSerial(String imageSerial) {
        this.imageSerial = imageSerial;
        return this;
    }

    public String getImageType() {
        return imageType;
    }

    public ImageItem setImageType(String imageType) {
        this.imageType = imageType;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ImageItem setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(imageId);
        dest.writeString(imageOrderId);
        dest.writeString(imageSerial);
        dest.writeString(imageType);
        dest.writeString(imageUrl);
    }

    public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
        @Override
        public ImageItem createFromParcel(Parcel in) {
            return new ImageItem(in);
        }

        @Override
        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };

    @Override
    public BaseItem clone() throws CloneNotSupportedException {
        ImageItem imageItem = new ImageItem()
                .setImageId(imageId)
                .setImageOrderId(imageOrderId)
                .setImageSerial(imageSerial)
                .setImageType(imageType)
                .setImageUrl(imageUrl);
        return imageItem;
    }
}
