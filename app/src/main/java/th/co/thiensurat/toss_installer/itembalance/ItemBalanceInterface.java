package th.co.thiensurat.toss_installer.itembalance;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.itemlist.item.InstallItem;

/**
 * Created by teerayut.k on 11/30/2017.
 */

public class ItemBalanceInterface {

    public interface View extends BaseMvpInterface.View {
        void setItemBalanceToAdapter(List<InstallItem> installItemList);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<ItemBalanceInterface.View> {
        void getItemBalance(Context context);
    }
}
