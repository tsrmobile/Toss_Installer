package th.co.thiensurat.toss_installer.api.request;

import java.util.List;

/**
 * Created by teerayut.k on 1/31/2018.
 */

public class UploadImage {

    private List<uploadBody> body;

    public List<uploadBody> getBody() {
        return body;
    }

    public UploadImage setBody(List<uploadBody> body) {
        this.body = body;
        return this;
    }

    public static class uploadBody {

        private String action;
        private String orderid;
        private String image64;
        private String imageType;
        private String productcode;

        public String getAction() {
            return action;
        }

        public uploadBody setAction(String action) {
            this.action = action;
            return this;
        }

        public String getOrderid() {
            return orderid;
        }

        public uploadBody setOrderid(String orderid) {
            this.orderid = orderid;
            return this;
        }

        public String getImage64() {
            return image64;
        }

        public uploadBody setImage64(String image64) {
            this.image64 = image64;
            return this;
        }

        public String getImageType() {
            return imageType;
        }

        public uploadBody setImageType(String imageType) {
            this.imageType = imageType;
            return this;
        }

        public String getProductcode() {
            return productcode;
        }

        public uploadBody setProductcode(String productcode) {
            this.productcode = productcode;
            return this;
        }
    }
}
