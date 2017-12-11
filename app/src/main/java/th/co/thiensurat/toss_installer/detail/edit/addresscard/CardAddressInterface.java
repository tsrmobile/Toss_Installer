package th.co.thiensurat.toss_installer.detail.edit.addresscard;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.job.item.AddressItem;

/**
 * Created by teerayut.k on 12/10/2017.
 */

public class CardAddressInterface {

    public interface View extends BaseMvpInterface.View {
        void setAddressDetail(List<AddressItem> addressDetail);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<CardAddressInterface.View> {
        void getAddressDetail(Context context, String orderid);
    }
}
