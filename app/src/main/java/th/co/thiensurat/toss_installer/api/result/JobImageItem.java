package th.co.thiensurat.toss_installer.api.result;

/**
 * Created by teerayut.k on 1/4/2018.
 */

public class JobImageItem {

    private String orderid;
    private String productcode;
    private String imageType;
    private String imageUrl;

    public String getOrderid() {
        return orderid;
    }

    public JobImageItem setOrderid(String orderid) {
        this.orderid = orderid;
        return this;
    }

    public String getProductcode() {
        return productcode;
    }

    public JobImageItem setProductcode(String productcode) {
        this.productcode = productcode;
        return this;
    }

    public String getImageType() {
        return imageType;
    }

    public JobImageItem setImageType(String imageType) {
        this.imageType = imageType;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public JobImageItem setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
