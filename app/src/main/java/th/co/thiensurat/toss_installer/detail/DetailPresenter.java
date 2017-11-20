package th.co.thiensurat.toss_installer.detail;

import android.content.Context;

import com.hwangjr.rxbus.RxBus;

import java.util.List;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.job.item.AddressItem;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

/**
 * Created by teerayut.k on 11/10/2017.
 */

public class DetailPresenter extends BaseMvpPresenter<DetailInterface.View> implements DetailInterface.Presenter {

    private DBHelper dbHelper;
    private ServiceManager serviceManager;
    private List<AddressItem> addressItems;

    public static DetailInterface.Presenter create() {
        return new DetailPresenter();
    }

    public DetailPresenter() {
        serviceManager = ServiceManager.getInstance();
    }

    public void setManager( ServiceManager manager ){
        serviceManager = manager;
    }

    @Override
    public void onViewCreate() {
        RxBus.get().register( this );
    }

    @Override
    public void onViewDestroy() {
        RxBus.get().unregister( this );
    }

    @Override
    public void getAddressDetail(Context context, String orderid) {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        this.addressItems = dbHelper.getAllAddress(orderid);
        getView().setAddressDetail(addressItems);
    }
}
