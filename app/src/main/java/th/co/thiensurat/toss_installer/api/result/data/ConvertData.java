package th.co.thiensurat.toss_installer.api.result.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by teerayut.k on 12/16/2017.
 */

public class ConvertData {

    public static List<DataItem> creatObjectList(List<DataResult> dataResultList, String type) {
        for (DataResult result : dataResultList) {
            if (type.equals("province")) {
                return result.getProvince();
            } else if (type.equals("district")) {
                return result.getDistrict();
            } else if (type.equals("subdistrict")) {
                return result.getSubdistrict();
            }
        }
        return null;
    }
}
