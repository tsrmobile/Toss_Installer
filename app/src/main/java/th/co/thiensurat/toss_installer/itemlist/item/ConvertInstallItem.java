package th.co.thiensurat.toss_installer.itemlist.item;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.toss_installer.api.result.InstallItemResult;
import th.co.thiensurat.toss_installer.api.result.InstallItemResultGroup;

/**
 * Created by teerayut.k on 11/10/2017.
 */

public class ConvertInstallItem {

    public static InstallItemGroup creatinstallItemGroup(InstallItemResultGroup resultGroup) {
        InstallItemGroup itemGroup = new InstallItemGroup();
        itemGroup.setStatus(resultGroup.getStatus());
        itemGroup.setMessage(resultGroup.getMessage());
        itemGroup.setData(ConvertInstallItem.creatinstallItemList( resultGroup.getData() ));
        return itemGroup;
    }

    public static List<InstallItem> creatinstallItemList(List<InstallItemResult> InstallItemResults) {
        List<InstallItem> items = new ArrayList<>();
        for(InstallItemResult itemResult : InstallItemResults) {
            InstallItem jobItem = new InstallItem()
                    .setOrderid(itemResult.getOrderid())
                    .setProductCode(itemResult.getProductCode())
                    .setProductRef(itemResult.getProductRef())
                    .setProductName(itemResult.getProductName())
                    .setProductQty(itemResult.getProductQty());
            items.add(jobItem);
        }
        return items;
    }
}
