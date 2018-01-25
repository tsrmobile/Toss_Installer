package th.co.thiensurat.toss_installer.api.request;

import java.util.List;

/**
 * Created by teerayut.k on 1/3/2018.
 */

public class RequestUpdateAddress {

    private List<updateBody> body;

    public List<updateBody> getBody() {
        return body;
    }

    public RequestUpdateAddress setBody(List<updateBody> body) {
        this.body = body;
        return this;
    }

    public static class updateBody {
        private String orderid;
        private String addressType;
        private String addrDetail;
        private String province;
        private String district;
        private String subdistrict;
        private String zipcode;
        private String phone;
        private String mobile;
        private String office;
        private String email;

        public String getOrderid() {
            return orderid;
        }

        public updateBody setOrderid(String orderid) {
            this.orderid = orderid;
            return this;
        }

        public String getAddressType() {
            return addressType;
        }

        public updateBody setAddressType(String addressType) {
            this.addressType = addressType;
            return this;
        }

        public String getAddrDetail() {
            return addrDetail;
        }

        public updateBody setAddrDetail(String addrDetail) {
            this.addrDetail = addrDetail;
            return this;
        }

        public String getProvince() {
            return province;
        }

        public updateBody setProvince(String province) {
            this.province = province;
            return this;
        }

        public String getDistrict() {
            return district;
        }

        public updateBody setDistrict(String district) {
            this.district = district;
            return this;
        }

        public String getSubdistrict() {
            return subdistrict;
        }

        public updateBody setSubdistrict(String subdistrict) {
            this.subdistrict = subdistrict;
            return this;
        }

        public String getZipcode() {
            return zipcode;
        }

        public updateBody setZipcode(String zipcode) {
            this.zipcode = zipcode;
            return this;
        }

        public String getPhone() {
            return phone;
        }

        public updateBody setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public String getMobile() {
            return mobile;
        }

        public updateBody setMobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public String getOffice() {
            return office;
        }

        public updateBody setOffice(String office) {
            this.office = office;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public updateBody setEmail(String email) {
            this.email = email;
            return this;
        }
    }
}
