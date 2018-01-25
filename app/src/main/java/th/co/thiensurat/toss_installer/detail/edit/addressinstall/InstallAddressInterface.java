package th.co.thiensurat.toss_installer.detail.edit.addressinstall;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import th.co.thiensurat.toss_installer.api.request.RequestUpdateAddress;
import th.co.thiensurat.toss_installer.api.result.data.DataItem;
import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.job.item.AddressItemGroup;

/**
 * Created by teerayut.k on 12/10/2017.
 */

public class InstallAddressInterface {

    public interface View extends BaseMvpInterface.View {
        void setAddressDetail(List<AddressItem> addressDetail);
        void setInfoToAdapter(String infoType, List<DataItem> dataItemList);
        void setZipcode(String zipcode);
        void updateLocalSuccess(boolean b);
        void OnLoad();
        void OnDismiss();
        void OnFail(String fail);
        void OnSuccess(String success);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<InstallAddressInterface.View> {
        void getAddressDetail(Context context, String orderid);
        void getInfo(Context context, String infoType, String id);
        void setAddressItemGroup(AddressItemGroup addressItemGroup);
        AddressItemGroup getAddressItemGroup();
        void setAddressItemToAdapter(AddressItemGroup addressItem);
        void updateData(Context context, String orderid, String type, List<AddressItem> addressItemList);
        void updateDataOnline(List<RequestUpdateAddress.updateBody> updateBody);
    }
}
