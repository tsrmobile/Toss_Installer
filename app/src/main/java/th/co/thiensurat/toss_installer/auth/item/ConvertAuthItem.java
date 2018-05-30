package th.co.thiensurat.toss_installer.auth.item;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.toss_installer.api.result.AuthItemResult;
import th.co.thiensurat.toss_installer.api.result.AuthItemResultGroup;


/**
 * Created by teerayut.k on 10/18/2017.
 */

public class ConvertAuthItem {

    public static AuthenItemGroup createAuthItemGroupFromResult(AuthItemResultGroup result ){
        AuthenItemGroup group = new AuthenItemGroup();
        group.setStatus( result.getStatus() );
        group.setMessage( result.getMessage() );
        group.setData( ConvertAuthItem.createListDataItemsFromResult( result.getData() ) );
        return group;
    }

    public static List<AuthenItem> createListDataItemsFromResult(List<AuthItemResult> result){
        List<AuthenItem> items = new ArrayList<>();
        for( AuthItemResult dataItemResult : result ){
            AuthenItem item = new AuthenItem()
                    .setEmployeecode(dataItemResult.getEmployeecode())
                    .setTitle(dataItemResult.getTitle())
                    .setFirstname(dataItemResult.getFirstname())
                    .setLastname(dataItemResult.getLastname())
                    .setPositionName(dataItemResult.getPositionName())
                    .setDepartmentName(dataItemResult.getDepartmentName())
                    .setEmployeeType(dataItemResult.getEmployeeType());
            items.add( item );
        }
        return items;
    }
}
