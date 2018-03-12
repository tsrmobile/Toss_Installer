package th.co.thiensurat.toss_installer.contract.item;

import android.net.Uri;

/**
 * Created by teerayut.k on 2/16/2018.
 */

public class ObjectImage {

    private String type;
    private String imageName;
    private String productCode;
    //private Uri imageUri;

    public String getType() {
        return type;
    }

    public ObjectImage setType(String type) {
        this.type = type;
        return this;
    }

    public String getImageName() {
        return imageName;
    }

    public ObjectImage setImageName(String imageName) {
        this.imageName = imageName;
        return this;
    }

    public String getProductCode() {
        return productCode;
    }

    public ObjectImage setProductCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    /*public Uri getImageName() {
        return imageUri;
    }

    public ObjectImage setImageName(Uri imageUri) {
        this.imageUri = imageUri;
        return this;
    }*/
}
