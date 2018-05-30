package th.co.thiensurat.toss_installer.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;

import org.json.JSONException;
import org.json.JSONObject;

import th.co.thiensurat.toss_installer.api.ServiceManager;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.MyApplication;


/**
 * Created by teerayut.k on 1/12/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    private ServiceManager serviceManager;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        try {
            serviceManager = ServiceManager.getInstance();
            serviceManager.updateFCMToken(MyApplication.getInstance().getPrefManager().getPreferrence(Constance.KEY_EMPID),
                    token, new ServiceManager.ServiceManagerCallback() {
                        @Override
                        public void onSuccess(Object result) {
                            Gson gson = new Gson();
                            try {
                                JSONObject jsonObject = new JSONObject(gson.toJson(result));
                                if ("SUCCESS".equals(jsonObject.getString("status"))) {
                                    //getView().onDismiss();
                                    //getView().onSuccess();
                                } else if ("FAIL".equals(jsonObject.getString("status"))) {
                                    Log.e(TAG, jsonObject.getString("message"));
                                } else {
                                    //getView().onDismiss();
                                    //getView().onFail(jsonObject.getString("message"));
                                    Log.e(TAG, jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "json obj : " + e.getLocalizedMessage());
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Log.e(TAG, "fail : " + t.getLocalizedMessage());
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "update : " + e.getLocalizedMessage());
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        RxBus.get().register( this );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister( this );
    }
}
