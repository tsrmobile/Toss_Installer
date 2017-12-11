package th.co.thiensurat.toss_installer.installation.step;

import th.co.thiensurat.toss_installer.base.BaseMvpInterface;

/**
 * Created by teerayut.k on 12/10/2017.
 */

public class StepViewInterface {

    public interface View extends BaseMvpInterface.View {
        void setStatusStep1();
        void setStatusStep2();
        void setStatusStep3();
        void setStatusStep4();
        void setStatusStep5();
        void setStatusStep6();
        void setStatusStep7();
        void setStatusStep8();
    }

    public interface Presenter extends BaseMvpInterface.Presenter<StepViewInterface.View> {
        void checkStep1();
        void checkStep2();
        void checkStep3();
        void checkStep4();
        void checkStep5();
        void checkStep6();
        void checkStep7();
        void checkStep8();
    }
}
