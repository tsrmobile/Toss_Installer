package th.co.thiensurat.toss_installer.dashboard.item;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.toss_installer.api.result.DashboardItemResult;
import th.co.thiensurat.toss_installer.api.result.DashboardItemResultGroup;

/**
 * Created by teerayut.k on 11/9/2017.
 */

public class ConvertDashboardItem {

    public static DashboardItemGroup createDashboardGroupFromResult( DashboardItemResultGroup result ){
        DashboardItemGroup group = new DashboardItemGroup();
        group.setStatus( result.getStatus() );
        group.setMessage( result.getMessage() );
        group.setData( ConvertDashboardItem.createListDashboardFromResult( result.getData() ));
        return group;
    }


    public static List<DashboardItem> createListDashboardFromResult(List<DashboardItemResult> result){
        List<DashboardItem> items = new ArrayList<>();
        for( DashboardItemResult listItemResult : result ){
            DashboardItem dashboardItem = new DashboardItem()
                    .setJoblimit(listItemResult.getJoblimit())
                    .setJobnow(listItemResult.getJobnow())
                    .setJoball(listItemResult.getJoball())
                    .setJobsuccess(listItemResult.getJobsuccess())
                    .setJobcount(listItemResult.getJobcount());
            items.add( dashboardItem );
        }
        return items;
    }
}
