package th.co.thiensurat.toss_installer.deposit.channel;

import android.content.Context;

import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

public class DepositChannelPresenter extends BaseMvpPresenter<DepositChannelInterface.View> implements DepositChannelInterface.Presenter  {

    private static Context context;
    private DBHelper dbHelper;

    public static DepositChannelInterface.Presenter create(Context activity) {
        context = activity;
        return new DepositChannelPresenter();
    }

    @Override
    public String getRef2() {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        return dbHelper.getDepositRef2();
    }
}
