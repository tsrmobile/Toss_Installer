package th.co.thiensurat.toss_installer.api.result.data;

import java.util.List;

/**
 * Created by teerayut.k on 12/16/2017.
 */

public class DataResult {

    private List<DataItem> province;
    private List<DataItem> district;
    private List<DataItem> subdistrict;

    public List<DataItem> getProvince() {
        return province;
    }

    public void setProvince(List<DataItem> province) {
        this.province = province;
    }

    public List<DataItem> getDistrict() {
        return district;
    }

    public void setDistrict(List<DataItem> district) {
        this.district = district;
    }

    public List<DataItem> getSubdistrict() {
        return subdistrict;
    }

    public void setSubdistrict(List<DataItem> subdistrict) {
        this.subdistrict = subdistrict;
    }
}
