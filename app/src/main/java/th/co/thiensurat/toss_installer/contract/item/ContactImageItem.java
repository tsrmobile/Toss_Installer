package th.co.thiensurat.toss_installer.contract.item;

/**
 * Created by teerayut.k on 2/14/2018.
 */

public class ContactImageItem {
    private String imageID;
    private String imageSerial;
    private String imageType;
    private String imageUrl;
    private String imageProductCode;

    public String getImageID() {
        return imageID;
    }

    public ContactImageItem setImageID(String imageID) {
        this.imageID = imageID;
        return this;
    }

    public String getImageSerial() {
        return imageSerial;
    }

    public ContactImageItem setImageSerial(String imageSerial) {
        this.imageSerial = imageSerial;
        return this;
    }

    public String getImageType() {
        return imageType;
    }

    public ContactImageItem setImageType(String imageType) {
        this.imageType = imageType;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ContactImageItem setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getImageProductCode() {
        return imageProductCode;
    }

    public ContactImageItem setImageProductCode(String imageProductCode) {
        this.imageProductCode = imageProductCode;
        return this;
    }
}
