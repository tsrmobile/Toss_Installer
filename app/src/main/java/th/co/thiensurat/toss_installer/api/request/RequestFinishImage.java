package th.co.thiensurat.toss_installer.api.request;

/**
 * Created by teerayut.k on 1/11/2018.
 */

public class RequestFinishImage {

    private String image;
    private String imageType;
    private String imageSerial;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageSerial() {
        return imageSerial;
    }

    public void setImageSerial(String imageSerial) {
        this.imageSerial = imageSerial;
    }
}
