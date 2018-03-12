package th.co.thiensurat.toss_installer.stepview;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;
import th.co.thiensurat.toss_installer.jobinstallation.item.ProductItem;

/**
 * Created by teerayut.k on 2/14/2018.
 */

public class StepViewInterface {

    public interface View extends BaseMvpInterface.View {
        void setValueToAdapter(List<String> strings);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<StepViewInterface.View> {
        void getStepValue(String orderid);
        boolean checkStep(String orderid);
        void setProductToTable(String orderid, List<ProductItem> productItems);
    }
}
