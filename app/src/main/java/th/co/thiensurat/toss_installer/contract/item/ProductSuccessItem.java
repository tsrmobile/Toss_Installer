package th.co.thiensurat.toss_installer.contract.item;

import java.util.List;

/**
 * Created by teerayut.k on 1/23/2018.
 */

public class ProductSuccessItem {

    private String productCode;
    private String productSerial;
    private List<ImageSuccessItem> images;

    public String getProductCode() {
        return productCode;
    }

    public ProductSuccessItem setProductCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public String getProductSerial() {
        return productSerial;
    }

    public ProductSuccessItem setProductSerial(String productSerial) {
        this.productSerial = productSerial;
        return this;
    }

    public List<ImageSuccessItem> getImages() {
        return images;
    }

    public ProductSuccessItem setImages(List<ImageSuccessItem> images) {
        this.images = images;
        return this;
    }
}
