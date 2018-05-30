package th.co.thiensurat.toss_installer.deposit.deposit;

import android.content.Context;

import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.db.DBHelper;

public class DepositPresenter extends BaseMvpPresenter<DepositInterface.View> implements DepositInterface.Presenter {

    private static Context context;
    private DBHelper dbHelper;

    public static DepositInterface.Presenter create(Context activity) {
        context = activity;
        return new DepositPresenter();
    }

    @Override
    public void getAllActual() {
        dbHelper = new DBHelper(context,  Constance.DBNAME, null, Constance.DB_CURRENT_VERSION);
        getView().setActualToAdapter(dbHelper.getAllPayment());
    }
}
