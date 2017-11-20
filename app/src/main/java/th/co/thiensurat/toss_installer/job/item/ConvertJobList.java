package th.co.thiensurat.toss_installer.job.item;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.toss_installer.api.result.JobItemResult;
import th.co.thiensurat.toss_installer.api.result.JobItemResultGroup;

/**
 * Created by teerayut.k on 9/26/2017.
 */

public class ConvertJobList {

    public static JobItemGroup creatJobItemGroup(JobItemResultGroup resultGroup) {
        JobItemGroup itemGroup = new JobItemGroup();
        itemGroup.setStatus(resultGroup.getStatus());
        itemGroup.setMessage(resultGroup.getMessage());
        itemGroup.setData(ConvertJobList.creatJobItemList( resultGroup.getData() ));
        return itemGroup;
    }

    public static List<JobItem> creatJobItemList(List<JobItemResult> jobItemResults) {
        List<JobItem> items = new ArrayList<>();
        for(JobItemResult jobResult : jobItemResults) {
            JobItem jobItem = new JobItem()
                    .setOrderid(jobResult.getOrderid())
                    .setIDCard(jobResult.getIDCard())
                    .setTitle(jobResult.getTitle())
                    .setFirstName(jobResult.getFirstName())
                    .setLastName(jobResult.getLastName())
                    .setContactphone(jobResult.getContactphone())
                    /*.setProductCode(jobResult.getProductCode())
                    .setProductName(jobResult.getProductName())
                    .setProductQty(jobResult.getProductQty())*/
                    .setInstallStartDate(jobResult.getInstallStartDate())
                    .setInstallEndDate(jobResult.getInstallEndDate())
                    .setProduct(jobResult.getProduct())
                    .setAddress(jobResult.getAddress());
            items.add(jobItem);
        }
        return items;
    }
}
