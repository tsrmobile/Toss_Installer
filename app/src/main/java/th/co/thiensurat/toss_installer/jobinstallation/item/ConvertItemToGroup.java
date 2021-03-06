package th.co.thiensurat.toss_installer.jobinstallation.item;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.toss_installer.api.result.JobItemResult;
import th.co.thiensurat.toss_installer.api.result.JobItemResultGroup;

/**
 * Created by teerayut.k on 9/26/2017.
 */

public class ConvertItemToGroup {

    public static JobItemGroup creatJobItemGroup(JobItemResultGroup resultGroup) {
        JobItemGroup itemGroup = new JobItemGroup();
        itemGroup.setStatus(resultGroup.getStatus());
        itemGroup.setMessage(resultGroup.getMessage());
        itemGroup.setData(ConvertItemToGroup.creatJobItemList( resultGroup.getData() ));
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
                    .setInstallStartDate(jobResult.getInstallStartDate())
                    .setInstallEndDate(jobResult.getInstallEndDate())
                    .setInstallStart(jobResult.getInstallStart())
                    .setInstallEnd(jobResult.getInstallEnd())
                    .setStatus(jobResult.getStatus())
                    .setPresale(jobResult.getPresale())
                    .setContno(jobResult.getContno())
                    .setCloseDate(jobResult.getCloseDate())
                    .setProduct(jobResult.getProduct())
                    .setAddress(jobResult.getAddress())
                    .setDuedate(jobResult.getDuedate())
                    .setPeriods(jobResult.getPeriods())
                    .setSendType(jobResult.getSendType())
                    .setInstallType(jobResult.getInstallType());
            items.add(jobItem);
        }
        return items;
    }
}
