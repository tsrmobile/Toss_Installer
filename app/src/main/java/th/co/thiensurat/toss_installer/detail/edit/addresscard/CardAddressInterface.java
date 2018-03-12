package th.co.thiensurat.toss_installer.detail.edit.addresscard;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.api.request.RequestUpdateAddress;
import th.co.thiensurat.toss_installer.api.result.data.DataItem;
import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItemGroup;

/**
 * Created by teerayut.k on 12/10/2017.
 */

public class CardAddressInterface {

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

    public interface Presenter extends BaseMvpInterface.Presenter<View> {
        void getAddressDetail(String orderid);
        void getInfo(String infoType, String id);
        void setAddressItemGroup(AddressItemGroup addressItemGroup);
        AddressItemGroup getAddressItemGroup();
        void setAddressItemToAdapter(AddressItemGroup addressItem);
        void updateData(String orderid, String type, List<AddressItem> addressItemList);
        void updateDataOnline(List<RequestUpdateAddress.updateBody> updateBody);
        void updateAddressSync(String orderid);
    }
}
