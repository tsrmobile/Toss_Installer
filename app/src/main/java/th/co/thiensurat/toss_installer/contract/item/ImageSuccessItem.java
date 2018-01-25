package th.co.thiensurat.toss_installer.contract.item;

/**
 * Created by teerayut.k on 1/23/2018.
 */

public class ImageSuccessItem {

    private String imageType;
    private String imageBase64;
    private String imageProductCode;

    public String getImageType() {
        return imageType;
    }

    public ImageSuccessItem setImageType(String imageType) {
        this.imageType = imageType;
        return this;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public ImageSuccessItem setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
        return this;
    }

    public String getImageProductCode() {
        return imageProductCode;
    }

    public ImageSuccessItem setImageProductCode(String imageProductCode) {
        this.imageProductCode = imageProductCode;
        return this;
    }
}
