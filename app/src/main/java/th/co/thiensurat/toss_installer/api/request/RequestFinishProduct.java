package th.co.thiensurat.toss_installer.api.request;

import java.util.List;

/**
 * Created by teerayut.k on 1/11/2018.
 */

public class RequestFinishProduct {

    private String productCode;
    private String productItemcode;
    private String productItemSerial;
    private List<RequestFinishImage> images;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductItemcode() {
        return productItemcode;
    }

    public void setProductItemcode(String productItemcode) {
        this.productItemcode = productItemcode;
    }

    public String getProductItemSerial() {
        return productItemSerial;
    }

    public void setProductItemSerial(String productItemSerial) {
        this.productItemSerial = productItemSerial;
    }

    public List<RequestFinishImage> getImages() {
        return images;
    }

    public void setImages(List<RequestFinishImage> images) {
        this.images = images;
    }
}
