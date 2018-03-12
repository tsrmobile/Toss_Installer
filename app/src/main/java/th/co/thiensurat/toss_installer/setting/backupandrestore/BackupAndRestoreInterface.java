package th.co.thiensurat.toss_installer.setting.backupandrestore;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import th.co.thiensurat.toss_installer.base.BaseMvpInterface;

/**
 * Created by teerayut.k on 2/18/2018.
 */

public class BackupAndRestoreInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoad();
        void onDismiss();
        void onFail(String fail);
        void onSuccess(String success);
        boolean writeResponseBodyToDisk(ResponseBody body);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<BackupAndRestoreInterface.View> {
        void backup(RequestBody requestBody, MultipartBody.Part file);
        void restore(String url);
    }
}
