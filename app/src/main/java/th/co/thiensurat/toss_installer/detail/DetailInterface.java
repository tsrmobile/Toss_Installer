package th.co.thiensurat.toss_installer.detail;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.jobinstallation.item.AddressItem;

/**
 * Created by teerayut.k on 11/10/2017.
 */

public class DetailInterface {

    public interface View extends BaseMvpInterface.View {
        void setAddressDetail(List<AddressItem> addressDetail);
        void setCancelSuccess();
        void onFail(String fail);
        void onLoad();
        void onDismiss();
    }

    public interface Presenter extends BaseMvpInterface.Presenter<View> {
        void setAddressDetail(String orderid, List<AddressItem> addressItemList);
        void getAddressDetail(String orderid);
        void requestUpdate(String note, String status, String empid, String orderid);
        void setTableStep(String orderid);
    }
}
