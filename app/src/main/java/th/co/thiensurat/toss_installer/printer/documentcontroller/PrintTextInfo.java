package th.co.thiensurat.toss_installer.printer.documentcontroller;


import android.os.Parcel;
import android.os.Parcelable;

public class PrintTextInfo implements Parcelable {
    public String text;
    public FontType fontType;
    public Boolean isBarcode;
    public Boolean isBankBarcode;
    public String language;
    public int barcodeSize;

    protected PrintTextInfo(Parcel in) {
        text = in.readString();
        byte tmpIsBarcode = in.readByte();
        isBarcode = tmpIsBarcode == 0 ? null : tmpIsBarcode == 1;
        byte tmpIsBankBarcode = in.readByte();
        isBankBarcode = tmpIsBankBarcode == 0 ? null : tmpIsBankBarcode == 1;
        language = in.readString();
        barcodeSize = in.readInt();
    }

    public static final Creator<PrintTextInfo> CREATOR = new Creator<PrintTextInfo>() {
        @Override
        public PrintTextInfo createFromParcel(Parcel in) {
            return new PrintTextInfo(in);
        }

        @Override
        public PrintTextInfo[] newArray(int size) {
            return new PrintTextInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
        parcel.writeByte((byte) (isBarcode == null ? 0 : isBarcode ? 1 : 2));
        parcel.writeByte((byte) (isBankBarcode == null ? 0 : isBankBarcode ? 1 : 2));
        parcel.writeString(language);
        parcel.writeInt(barcodeSize);
    }

    public enum FontType {
        Normal, Bold
    }

    public PrintTextInfo(String text) {
        this.text = text;
        this.fontType = FontType.Normal;
        this.isBarcode = false;
        this.isBankBarcode = false;
        this.language = "TH";
        this.barcodeSize = 0;
    }

    public PrintTextInfo(String text, String language) {
        this.text = text;
        this.fontType = FontType.Normal;
        this.isBarcode = false;
        this.isBankBarcode = false;
        this.language = language;
        this.barcodeSize = 0;
    }

    public PrintTextInfo(String text, FontType fontType) {
        this.text = text;
        this.fontType = fontType;
        this.isBarcode = false;
        this.isBankBarcode = false;
        this.language = "TH";
        this.barcodeSize = 0;
    }

    public PrintTextInfo(String text, FontType fontType, String language) {
        this.text = text;
        this.fontType = fontType;
        this.isBarcode = false;
        this.isBankBarcode = false;
        this.language = language;
        this.barcodeSize = 0;
    }

    public PrintTextInfo(String text, FontType fontType, Boolean isBarcode) {
        this.text = text;
        this.fontType = fontType;
        this.isBarcode = isBarcode;
        this.isBankBarcode = false;
        this.language = "TH";
        this.barcodeSize = 1;
    }

    public PrintTextInfo(String text, FontType fontType, Boolean isBarcode, Boolean isBankBarcode) {
        this.text = text;
        this.fontType = fontType;
        this.isBarcode = isBarcode;
        this.isBankBarcode = isBankBarcode;
        this.language = "TH";
        this.barcodeSize = 1;

    }

    public PrintTextInfo(String text, FontType fontType, Boolean isBarcode, Boolean isBankBarcode, int barcodeSize) {
        this.text = text;
        this.fontType = fontType;
        this.isBarcode = isBarcode;
        this.isBankBarcode = isBankBarcode;
        this.language = "TH";
        this.barcodeSize = barcodeSize;
    }

}
