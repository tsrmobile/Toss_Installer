package th.co.thiensurat.toss_installer.detail;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.job.item.AddressItem;

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

    public interface Presenter extends BaseMvpInterface.Presenter<DetailInterface.View> {
        void getAddressDetail(Context context, String orderid);
        void setCancelJob(Context context, String orderid, String cancelnote);
    }
}
