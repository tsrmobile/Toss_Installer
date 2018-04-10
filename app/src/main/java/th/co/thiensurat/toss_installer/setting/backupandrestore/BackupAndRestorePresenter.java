package th.co.thiensurat.toss_installer.setting.backupandrestore;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.github.lizhangqu.coreprogress.ProgressHelper;
import io.github.lizhangqu.coreprogress.ProgressUIListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import th.co.thiensurat.toss_installer.api.ApiURL;
import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.base.BaseMvpPresenter;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.MyApplication;

/**
 * Created by teerayut.k on 2/18/2018.
 */

public class BackupAndRestorePresenter extends BaseMvpPresenter<BackupAndRestoreInterface.View> implements BackupAndRestoreInterface.Presenter {

    private ServiceManager serviceManager;

    public BackupAndRestorePresenter() {
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

    public static BackupAndRestoreInterface.Presenter create() {
        return new BackupAndRestorePresenter();
    }

    @Override
    public void backup(RequestBody requestBody, MultipartBody.Part file, MultipartBody.Part filezip) {
        getView().onLoad();
        serviceManager.requestBackup(requestBody, file, filezip, new ServiceManager.ServiceManagerCallback() {
            @Override
            public void onSuccess(Object result) {
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(result));
                    if ("SUCCESS".equals(jsonObject.getString("status"))) {
                        getView().onDismiss();
                        getView().onSuccess(jsonObject.getString("message"));
                        getView().onDeleteFileZip();
                    } else if ("FAIL".equals(jsonObject.getString("status"))) {
                        getView().onDismiss();
                        getView().onFail(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    Log.e("json obj", e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getView().onDismiss();
                Log.e("fail", t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void restore(String url) {
        serviceManager.requestRestore(url, new ServiceManager.ServiceManagerCallback<ResponseBody>() {
            @Override
            public void onSuccess(final ResponseBody result) {
                if (result == null) {
                    getView().onDismiss();
                    getView().onFail("ไม่มีไฟล์สำรองข้อมูล");
                } else {
                    boolean writtenToDisk = getView().writeResponseBodyToDisk(result);
                    if (writtenToDisk) {
                        //getView().onSuccess("กู้คืนข้อมูลสำเร็จ");
                        getView().downloadFile();
                    } else {
                        getView().onFail("กู้คืนข้อมูลไม่สำเร็จ");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getView().onDismiss();
                getView().onFail("ไม่มีไฟล์สำรองข้อมูล");
                Log.e("fail", t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void extractFile() {
        getView().extractFileToDevice();
        /*String url = ApiURL.BASE_FOLDER + "/" + MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID) + "/files.zip";
        serviceManager.requestRestore(url, new ServiceManager.ServiceManagerCallback<ResponseBody>() {
            @Override
            public void onSuccess(final ResponseBody result) {
                if (result == null) {
                    getView().onDismiss();
                    getView().onFail("ไม่มีไฟล์สำรองข้อมูล");
                } else {
                    boolean writtenToDisk = getView().writeResponseBodyToDisk(result);
                    if (writtenToDisk) {
                        //getView().onSuccess("กู้คืนข้อมูลสำเร็จ");
                        getView().extractFileToDevice(result);
                    } else {
                        getView().onFail("กู้คืนข้อมูลไม่สำเร็จ");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getView().onDismiss();
                getView().onFail("ไม่มีไฟล์สำรองข้อมูล");
                Log.e("fail", t.getLocalizedMessage());
            }
        });*/
    }

    @Override
    public void checkBackup(String description) {
        serviceManager.requestCheckBackup("checkpath", description, new ServiceManager.ServiceManagerCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(result));
                    if ("SUCCESS".equals(jsonObject.getString("status"))) {
                        getView().onBackupExist();
                    } else if ("FAIL".equals(jsonObject.getString("status"))) {
                        getView().onNotBackup();
                    }
                } catch (JSONException e) {
                    Log.e("json obj", e.getLocalizedMessage());
                    getView().onNotBackup();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("fail", t.getLocalizedMessage());
                getView().onNotBackup();
            }
        });
    }
}
