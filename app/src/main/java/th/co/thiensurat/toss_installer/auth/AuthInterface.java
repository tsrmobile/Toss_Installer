package th.co.thiensurat.toss_installer.auth;

import android.content.Context;

import java.util.List;

import th.co.thiensurat.toss_installer.api.request.RequestAuth;
import th.co.thiensurat.toss_installer.base.BaseMvpInterface;


/**
 * Created by teerayut.k on 10/16/2017.
 */

public class AuthInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoad();
        void onDismiss();
        void onFail(String fail);
        void onSuccess();
        void onUpdateSuccess();
        void onNextPage();
    }

    public interface Presenter extends BaseMvpInterface.Presenter<View> {
        void auth(List<RequestAuth.authenBody> itemAuths);
        void updateFCMToken(String empid, String token);
    }
}
